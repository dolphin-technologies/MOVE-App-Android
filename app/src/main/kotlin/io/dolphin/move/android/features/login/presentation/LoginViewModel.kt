package io.dolphin.move.android.features.login.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.isEmailValid
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.domain.usecase.WebAgreementInteractor
import io.dolphin.move.android.features.login.domain.RequestLoginInteractor
import io.dolphin.move.android.features.login.domain.dto.LoginUserDto
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val requestLoginInteractor: RequestLoginInteractor,
    private val webInteractor: WebAgreementInteractor,
) : BaseViewModel(), LoginView {

    private val eventState: MutableSharedFlow<LoginEvent> = MutableSharedFlow()
    val events: SharedFlow<LoginEvent> = eventState.asSharedFlow()

    private val liveState = MutableLiveData(LoginViewState())
    private var viewState: LoginViewState by liveState.delegate()

    val loginViewState = liveState.mapDistinct { it }

    init {
        webInteractor.getAccepted()
            .onEach {
                viewState = viewState.copy(
                    isTermsAccepted = it.contains(Agreement.TERMS_OF_USE),
                    isPolicyAccepted = it.contains(Agreement.PRIVACY_POLICY),
                )
            }
            .launchIn(viewModelScope)
    }

    override fun requestLogin() {
        val dataIsValid = validateEnteredValues()
        if (!dataIsValid) return
        val loginDto = mapToLoginDto()
        viewModelScope.launch {
            showProgress()
            when (val dataState = requestLoginInteractor(loginDto)) {
                is State.Data -> {
                    hideProgress()
                    eventState.offerEvent(LoginEvent.Success())
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(LoginEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                LoginEvent.Common(message = dataState.error.responseMessage)
                            )
                        }
                    }
                }
                State.Loading,
                State.None -> { /* do nothing */
                }
            }
        }
    }

    override fun onEmailChanged(value: String) {
        viewState = viewState.copy(email = value)
    }

    override fun onPasswordChanged(value: String) {
        viewState = viewState.copy(password = value)
    }

    override fun removeAgreement(agreement: Agreement) {
        viewModelScope.launch { webInteractor.removeAccepted(agreement) }
    }

    override fun acceptAgreement(agreement: Agreement) {
        viewModelScope.launch { webInteractor.acceptAgreement(agreement) }
    }

    private fun validateEnteredValues(): Boolean {
        if (!isEmailValid(viewState.email)) {
            eventState.offerEvent(LoginEvent.InvalidEmail())
            return false
        }
        if (viewState.password.isBlank()) {
            eventState.offerEvent(LoginEvent.WrongPassword())
            return false
        }
        if (!viewState.isTermsAccepted) {
            eventState.offerEvent(LoginEvent.NoTerms())
            return false
        }

        if (!viewState.isPolicyAccepted) {
            eventState.offerEvent(LoginEvent.NoPolicy())
            return false
        }
        return true
    }

    private fun mapToLoginDto(): LoginUserDto {
        return LoginUserDto(
            email = viewState.email,
            password = viewState.password,
        )
    }
}