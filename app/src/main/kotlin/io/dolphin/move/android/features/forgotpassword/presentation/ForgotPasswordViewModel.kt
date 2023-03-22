package io.dolphin.move.android.features.forgotpassword.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.isEmailValid
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.forgotpassword.domain.RequestDropPasswordInteractor
import io.dolphin.move.android.features.forgotpassword.domain.dto.DropPasswordDto
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val requestDropPasswordInteractor: RequestDropPasswordInteractor,
) : BaseViewModel(), ForgotPasswordView {

    private val eventState: MutableSharedFlow<ForgotPasswordEvent> = MutableSharedFlow()
    val events: SharedFlow<ForgotPasswordEvent> = eventState.asSharedFlow()

    private val liveState = MutableLiveData(ForgotPasswordViewState())
    private var viewState: ForgotPasswordViewState by liveState.delegate()

    val forgotPasswordViewState = liveState.mapDistinct { it }

    override fun onEmailChanged(value: String) {
        viewState = viewState.copy(email = value)
    }

    override fun requestDropPassword() {
        if (!isEmailValid(viewState.email)) {
            eventState.offerEvent(ForgotPasswordEvent.InvalidEmail())
            return
        }

        val changePasswordDto = mapToDropPasswordDto()
        viewModelScope.launch {
            showProgress()
            when (val dataState = requestDropPasswordInteractor(changePasswordDto)) {
                is State.Data -> {
                    hideProgress()
                    eventState.offerEvent(ForgotPasswordEvent.PasswordDropped())
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(ForgotPasswordEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                ForgotPasswordEvent.Common(message = dataState.error.responseMessage)
                            )
                        }
                    }
                }
                State.Loading,
                State.None -> { /* do nothing*/
                }
            }
        }
    }

    private fun mapToDropPasswordDto(): DropPasswordDto {
        return DropPasswordDto(
            email = viewState.email,
        )
    }
}