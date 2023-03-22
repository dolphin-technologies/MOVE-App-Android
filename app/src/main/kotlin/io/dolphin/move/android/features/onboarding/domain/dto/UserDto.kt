package io.dolphin.move.android.features.onboarding.domain.dto

import io.dolphin.move.android.basedata.network.responses.ApiContract
import io.dolphin.move.android.basedata.network.responses.ApiRegisterUserRequest
import io.dolphin.move.android.basedata.network.responses.ApiUserConsent
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.features.onboarding.presentation.UserIdentity

data class UserDto(
    val identity: UserIdentity? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val companyName: String? = null,
    val password: String? = null,
    val isTermsAccepted: Boolean? = null,
    val isPolicyAccepted: Boolean? = null,
)

fun UserDto.toUserRequest(): ApiRegisterUserRequest {
    val apiUserConsents = buildList<ApiUserConsent> {
        if (isTermsAccepted == true) {
            add(createUserConsent(Agreement.TERMS_OF_USE))
        }
        if (isPolicyAccepted == true) {
            add(createUserConsent(Agreement.PRIVACY_POLICY))
        }
    }
    return ApiRegisterUserRequest(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        gender = identity?.type,
        password = password,
        company = companyName,
        consents = apiUserConsents,
    )
}

fun UserDto.toContractRequest(): ApiContract {
    return ApiContract(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        gender = identity?.type,
        company = companyName,
    )
}

fun createUserConsent(agreement: Agreement): ApiUserConsent {
    return ApiUserConsent(
        type = agreement.type,
        state = true,
        webUrl = agreement.url,
    )
}