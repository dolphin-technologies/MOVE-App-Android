package io.dolphin.move.android.features.login.domain

import io.dolphin.move.android.basedata.network.responses.ApiLogin
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.login.data.LoginRepository
import io.dolphin.move.android.features.login.domain.dto.LoginUserDto
import javax.inject.Inject

class RequestLoginInteractor @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    suspend operator fun invoke(loginDto: LoginUserDto): State<ApiLogin> {
        return loginRepository.requestLogin(loginDto)
    }
}