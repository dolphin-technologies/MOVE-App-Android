package io.dolphin.move.android.features.changepassword.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.domain.usecase.GetUserInteractor
import io.dolphin.move.android.domain.usecase.WebAgreementInteractor
import io.dolphin.move.android.features.changepassword.domain.ChangePasswordInteractor
import io.dolphin.move.android.features.changepassword.domain.dto.ChangePasswordDto
import io.dolphin.move.android.features.profile.domain.ProfileInteractor
import io.dolphin.move.android.sdk.domain.MoveSdkManagerInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordInteractor: ChangePasswordInteractor,
    private val profileInteractor: ProfileInteractor,
    private val userInteractor: GetUserInteractor,
    private val moveSdkInteractor: MoveSdkManagerInteractor,
    private val webInteractor: WebAgreementInteractor,
) : BaseViewModel(), ChangePasswordView {

    private val eventState: MutableSharedFlow<ChangePasswordEvent> = MutableSharedFlow()
    val events: SharedFlow<ChangePasswordEvent> = eventState.asSharedFlow()

    private val liveState = MutableLiveData(ChangePasswordViewState())
    private var viewState: ChangePasswordViewState by liveState.delegate()

    val changePasswordViewState = liveState.mapDistinct { it }

    override fun onCurrentPasswordChanged(value: String) {
        viewState = viewState.copy(currentPassword = value)
    }

    override fun onNewPasswordChanged(value: String) {
        viewState = viewState.copy(newPassword = value)
    }

    override fun onRepeatPasswordChanged(value: String) {
        viewState = viewState.copy(repeatPassword = value)
    }

    private fun isRequiredFieldsValid(): Boolean {
        var isValid = true
        isValid = isValid && viewState.currentPassword.isNotBlank()
        isValid = isValid && viewState.newPassword.isNotBlank()
        isValid = isValid && viewState.repeatPassword.isNotBlank()
        return isValid
    }

    private fun validateEnteredValues(): Boolean {
        if (!isRequiredFieldsValid()) {
            eventState.offerEvent(ChangePasswordEvent.RequiredFields())
            return false
        }
        if (viewState.newPassword != viewState.repeatPassword) {
            eventState.offerEvent(ChangePasswordEvent.PasswordMismatch())
            return false
        }
        return true
    }

    override fun onSaveNewPassword() {
        val dataIsValid = validateEnteredValues()
        if (!dataIsValid) return
        viewModelScope.launch {
            showProgress()
            when (val dataState = changePasswordInteractor.requestChangePassword(
                changePasswordDto = mapToChangePasswordDto())
            ) {
                is State.Data -> {
                    hideProgress()
                    eventState.offerEvent(ChangePasswordEvent.Success())
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(ChangePasswordEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                ChangePasswordEvent.Common(message = dataState.error.responseMessage)
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

    override fun onLogoutRequested() {
        viewModelScope.launch {
            showProgress()
            when (val dataState = profileInteractor.requestLogout() ) {
                is State.Data -> {
                    hideProgress()
                    eventState.offerEvent(ChangePasswordEvent.LogoutSuccess())
                    moveSdkInteractor.getMoveSdk()?.shutdown()
                    userInteractor.deleteUserData()
                    webInteractor.removeAccepted(Agreement.TERMS_OF_USE)
                    webInteractor.removeAccepted(Agreement.PRIVACY_POLICY)
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(ChangePasswordEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                ChangePasswordEvent.Common(message = dataState.error.responseMessage)
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

    private fun mapToChangePasswordDto(): ChangePasswordDto {
        return ChangePasswordDto(
            password = viewState.currentPassword,
            newPassword = viewState.newPassword
        )
    }

}