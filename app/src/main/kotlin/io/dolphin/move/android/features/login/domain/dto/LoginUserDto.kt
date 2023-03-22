package io.dolphin.move.android.features.login.domain.dto

import io.dolphin.move.android.basedata.network.responses.ApiLoginRequest

data class LoginUserDto(
    val email: String? = null,
    val password: String? = null,
)

fun LoginUserDto.toLoginRequest(): ApiLoginRequest {
    return ApiLoginRequest(
        email = email,
        password = password,
    )
}
