package io.dolphin.move.android.features.onboarding.domain

import io.dolphin.move.android.basedata.network.responses.ApiLogin
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.onboarding.data.RegistrationRepository
import io.dolphin.move.android.features.onboarding.domain.dto.UserDto
import javax.inject.Inject

class RegisterInteractor @Inject constructor(
    private val registrationRepository: RegistrationRepository,
) {
    suspend operator fun invoke(userDto: UserDto): State<ApiLogin> {
        return registrationRepository.registerUser(userDto)
    }
}