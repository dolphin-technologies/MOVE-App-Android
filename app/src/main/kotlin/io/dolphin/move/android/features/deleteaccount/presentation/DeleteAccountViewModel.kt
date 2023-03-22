package io.dolphin.move.android.features.deleteaccount.presentation

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
import io.dolphin.move.android.features.deleteaccount.domain.RequestDeleteAccountInteractor
import io.dolphin.move.android.sdk.domain.MoveSdkManagerInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val requestDeleteAccountInteractor: RequestDeleteAccountInteractor,
    private val userInteractor: GetUserInteractor,
    private val moveSdkInteractor: MoveSdkManagerInteractor,
    private val webInteractor: WebAgreementInteractor,
) : BaseViewModel(), DeleteAccountView {

    private val eventState: MutableSharedFlow<DeleteAccountEvent> = MutableSharedFlow()
    val events: SharedFlow<DeleteAccountEvent> = eventState.asSharedFlow()

    private val liveState = MutableLiveData(DeleteAccountViewState())
    private var viewState: DeleteAccountViewState by liveState.delegate()

    val deleteAccountViewState = liveState.mapDistinct { it }

    override fun onPasswordChanged(value: String) {
        viewState = viewState.copy(password = value)
    }

    private fun validateEnteredValues(): Boolean {
        if (!isRequiredFieldsValid()) {
            eventState.offerEvent(DeleteAccountEvent.RequiredFields())
            return false
        }
        return true
    }

    private fun isRequiredFieldsValid(): Boolean {
        var isValid = true
        isValid = isValid && viewState.password.isNotBlank()
        return isValid
    }

    override fun requestDeleteAccount() {
        val dataIsValid = validateEnteredValues()
        if (!dataIsValid) return
        viewModelScope.launch {
            showProgress()
            when (val dataState = requestDeleteAccountInteractor(viewState.password)) {
                is State.Data -> {
                    hideProgress()
                    moveSdkInteractor.getMoveSdk()?.shutdown()
                    userInteractor.deleteUserData()
                    webInteractor.removeAccepted(Agreement.TERMS_OF_USE)
                    webInteractor.removeAccepted(Agreement.PRIVACY_POLICY)
                    eventState.offerEvent(DeleteAccountEvent.Success())
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(DeleteAccountEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                DeleteAccountEvent.Common(message = dataState.error.responseMessage)
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
}