package io.dolphin.move.android.features.profile.domain.dto

import io.dolphin.move.android.basedata.network.responses.ApiChangeContractDataRequest
import io.dolphin.move.android.features.onboarding.presentation.UserIdentity

data class ProfileDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val identity: UserIdentity? = null,
    val password: String? = null,
    val email: String? = null,
    val company: String? = null,
    val phone: String? = null,
)

fun ProfileDto.toChangeContractDataRequest(): ApiChangeContractDataRequest {
    return ApiChangeContractDataRequest(
        firstName = firstName,
        lastName = lastName,
        gender = identity?.type,
        password = password,
        email = email,
        company = company,
        phone = phone,
    )
}
