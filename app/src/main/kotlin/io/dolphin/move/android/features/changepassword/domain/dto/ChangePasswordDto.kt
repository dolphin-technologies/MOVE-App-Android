package io.dolphin.move.android.features.changepassword.domain.dto

import io.dolphin.move.android.basedata.network.responses.ApiChangePasswordRequest

data class ChangePasswordDto(
    val password: String? = null,
    val newPassword: String? = null,
)

fun ChangePasswordDto.toChangePasswordRequest(): ApiChangePasswordRequest {
    return ApiChangePasswordRequest(
        password = password,
        newPassword = newPassword
    )
}
