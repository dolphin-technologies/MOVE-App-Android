package io.dolphin.move.android.features.onboarding.presentation

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
import io.dolphin.move.android.features.onboarding.domain.RegisterInteractor
import io.dolphin.move.android.features.onboarding.domain.dto.UserDto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val registerInteractor: RegisterInteractor,
    private val webInteractor: WebAgreementInteractor,
) : BaseViewModel(), OnboardingView {

    private val eventState: MutableSharedFlow<OnboardingEvent> = MutableSharedFlow()
    val events: SharedFlow<OnboardingEvent> = eventState.asSharedFlow()

    private val liveState = MutableLiveData(OnboardingViewState())
    private var viewState: OnboardingViewState by liveState.delegate()

    val onboardingViewState = liveState.mapDistinct { it }

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

    override fun onIdentitySelected(value: UserIdentity) {
        viewState = viewState.copy(identity = value)
    }

    override fun onFirstNameChanged(value: String) {
        viewState = viewState.copy(firstName = value)
    }

    override fun onLastNameChanged(value: String) {
        viewState = viewState.copy(lastName = value)
    }

    override fun onEmailChanged(value: String) {
        viewState = viewState.copy(email = value)
    }

    override fun onPhoneChanged(value: String) {
        viewState = viewState.copy(phone = value)
    }

    override fun onCompanyChanged(value: String) {
        viewState = viewState.copy(companyName = value)
    }

    override fun onPasswordChanged(value: String) {
        viewState = viewState.copy(password = value)
    }

    override fun onRepeatPasswordChanged(value: String) {
        viewState = viewState.copy(repeatPassword = value)
    }

    override fun onRegisterUserRequested() {
        val dataIsValid = validateEnteredValues()
        if (!dataIsValid) return
        val user = mapToUserDto()
        viewModelScope.launch {
            showProgress()
            when (val dataState = registerInteractor(user)) {
                is State.Data -> {
                    hideProgress()
                    eventState.offerEvent(OnboardingEvent.Success())
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(OnboardingEvent.Error())
                        }
                        is MoveApiError.ServiceError.UserExists -> {
                            eventState.offerEvent(OnboardingEvent.EmailAlreadyInUse())
                        }
                        else -> eventState.offerEvent(OnboardingEvent.Error())
                    }
                }
                State.Loading,
                State.None -> { /* do nothing*/
                }
            }
        }
    }

    override fun acceptAgreement(agreement: Agreement) {
        viewModelScope.launch { webInteractor.acceptAgreement(agreement) }
    }

    override fun removeAgreement(agreement: Agreement) {
        viewModelScope.launch { webInteractor.removeAccepted(agreement) }
    }

    private fun validateEnteredValues(): Boolean {
        if (viewState.identity == UserIdentity.NONE) {
            eventState.offerEvent(OnboardingEvent.SelectGender())
            return false
        }
        if (!isRequiredFieldsValid()) {
            eventState.offerEvent(OnboardingEvent.RequiredFields())
            return false
        }
        if (!viewState.isTermsAccepted) {
            eventState.offerEvent(OnboardingEvent.NoTerms())
            return false
        }
        if (!viewState.isPolicyAccepted) {
            eventState.offerEvent(OnboardingEvent.NoPolicy())
            return false
        }
        if (!isEmailValid(viewState.email)) {
            eventState.offerEvent(OnboardingEvent.InvalidEmail())
            return false
        }
        if (viewState.password.isBlank() || viewState.password != viewState.repeatPassword) {
            eventState.offerEvent(OnboardingEvent.PasswordMismatch())
            return false
        }
        return true
    }

    private fun isRequiredFieldsValid(): Boolean {
        var isValid = true
        isValid = isValid && viewState.firstName.isNotBlank()
        isValid = isValid && viewState.lastName.isNotBlank()
        isValid = isValid && viewState.email.isNotBlank()
        isValid = isValid && viewState.password.isNotBlank()
        isValid = isValid && viewState.repeatPassword.isNotBlank()
        return isValid
    }

    private fun mapToUserDto(): UserDto {
        return UserDto(
            identity = viewState.identity,
            firstName = viewState.firstName.trim(),
            lastName = viewState.lastName.trim(),
            email = viewState.email.trim(),
            phone = viewState.phone.trim(),
            companyName = viewState.companyName.trim(),
            password = viewState.password.trim(),
            isTermsAccepted = viewState.isTermsAccepted,
            isPolicyAccepted = viewState.isPolicyAccepted,
        )
    }
}