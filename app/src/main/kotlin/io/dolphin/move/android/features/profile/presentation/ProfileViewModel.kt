package io.dolphin.move.android.features.profile.presentation

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
import io.dolphin.move.android.domain.usecase.GetUserInteractor
import io.dolphin.move.android.domain.usecase.WebAgreementInteractor
import io.dolphin.move.android.features.onboarding.presentation.UserIdentity
import io.dolphin.move.android.features.profile.domain.ProfileInteractor
import io.dolphin.move.android.features.profile.domain.dto.ProfileDto
import io.dolphin.move.android.sdk.domain.MoveSdkManagerInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val userInteractor: GetUserInteractor,
    private val moveSdkInteractor: MoveSdkManagerInteractor,
    private val webInteractor: WebAgreementInteractor,
) : BaseViewModel(), ProfileView {

    private val eventState: MutableSharedFlow<ProfileEvent> = MutableSharedFlow()
    val events: SharedFlow<ProfileEvent> = eventState.asSharedFlow()

    private val liveState = MutableLiveData(ProfileViewState())
    private var viewState: ProfileViewState by liveState.delegate()

    val profileViewState = liveState.mapDistinct { it }

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
        val showPassword = value != viewState.oldEmail
        viewState = viewState.copy(email = value, showPassword = showPassword)
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

    private fun isRequiredFieldsValid(): Boolean {
        var isValid = true
        isValid = isValid && viewState.firstName.isNotBlank()
        isValid = isValid && viewState.lastName.isNotBlank()
        isValid = isValid && viewState.email.isNotBlank()
        if (viewState.email != viewState.oldEmail) {
            isValid = isValid && viewState.password.isNotBlank()
        }
        return isValid
    }

    private fun validateEnteredValues(): Boolean {
        if (!isRequiredFieldsValid()) {
            eventState.offerEvent(ProfileEvent.RequiredFields())
            return false
        }
        if (!isEmailValid(viewState.email)) {
            eventState.offerEvent(ProfileEvent.InvalidEmail())
            return false
        }
        return true
    }

    override fun onSaveUserRequested() {
        val dataIsValid = validateEnteredValues()
        if (!dataIsValid) return
        viewModelScope.launch {
            showProgress()
            when (val dataState = profileInteractor.requestSave(profileData = mapToProfileDto())) {
                is State.Data -> {
                    hideProgress()
                    if (viewState.email == viewState.oldEmail) {
                        // no changes to the email -> we are done
                        eventState.offerEvent(ProfileEvent.SaveSuccess())
                    } else {
                        // user also changed the email
                        requestChangeEmail()
                    }
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(ProfileEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                ProfileEvent.Common(message = dataState.error.responseMessage)
                            )
                        }
                    }
                    eventState.offerEvent(ProfileEvent.Error())
                }
                State.Loading,
                State.None -> { /* do nothing*/
                }
            }
        }
    }

    private fun requestChangeEmail() {
        viewModelScope.launch {
            showProgress()
            when (val dataState = profileInteractor.requestEmailChange(profileData = mapToProfileDto())) {
                is State.Data -> {
                    hideProgress()
                    eventState.offerEvent(ProfileEvent.CheckEmail())
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(ProfileEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                ProfileEvent.Common(message = dataState.error.responseMessage)
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

    fun getContract() {
        viewModelScope.launch {
            showProgress()
            when (val dataState = profileInteractor.getContractData()) {
                is State.Data -> {
                    hideProgress()
                    viewState = viewState.copy(
                        identity = UserIdentity.getIdentityEnum(dataState.data.gender?:"none"),
                        firstName = dataState.data.firstName ?: "",
                        lastName = dataState.data.lastName ?: "",
                        email = dataState.data.email ?: "",
                        phone = dataState.data.phone ?: "",
                        companyName = dataState.data.company ?: "",
                        oldEmail = dataState.data.email ?: "",
                    )
                }
                is State.Error -> {
                    hideProgress()
                    // if the request fails show the stored contract data instead
                    val contractData = userInteractor.getContract()
                    contractData?.let {
                        viewState = viewState.copy(
                            identity = UserIdentity.getIdentityEnum(contractData.gender?:"none"),
                            firstName = contractData.firstName ?: "",
                            lastName = contractData.lastName ?: "",
                            email = contractData.email ?: "",
                            phone = contractData.phone ?: "",
                            companyName = contractData.company ?: "",
                            oldEmail = contractData.email ?: "",
                        )
                    }
                    eventState.offerEvent(ProfileEvent.Error())
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
                    moveSdkInteractor.getMoveSdk()?.shutdown()
                    userInteractor.deleteUserData()
                    webInteractor.removeAccepted(Agreement.TERMS_OF_USE)
                    webInteractor.removeAccepted(Agreement.PRIVACY_POLICY)
                    eventState.offerEvent(ProfileEvent.LogoutSuccess())
                }
                is State.Error -> {
                    hideProgress()
                    when (dataState.error) {
                        is MoveApiError.HttpError -> {
                            eventState.offerEvent(ProfileEvent.Error())
                        }
                        is MoveApiError.ServiceError -> {
                            eventState.offerEvent(
                                ProfileEvent.Common(message = dataState.error.responseMessage)
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

    private fun mapToProfileDto(): ProfileDto {
        return ProfileDto(
            firstName = viewState.firstName,
            lastName = viewState.lastName,
            identity = viewState.identity,
            password = viewState.password,
            email = viewState.email,
            company = viewState.companyName,
            phone = viewState.phone,
        )
    }

}