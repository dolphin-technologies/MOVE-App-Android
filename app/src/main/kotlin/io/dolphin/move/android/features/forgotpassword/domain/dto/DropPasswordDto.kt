package io.dolphin.move.android.features.forgotpassword.domain.dto

import io.dolphin.move.android.basedata.network.responses.ApiRequestResetPasswordRequest

data class DropPasswordDto(
    val email: String,
)

fun DropPasswordDto.toDropPasswordRequest(): ApiRequestResetPasswordRequest {
    return ApiRequestResetPasswordRequest(email = email)
}
