package io.dolphin.move.android.features.forgotpassword.domain

import io.dolphin.move.android.basedata.network.responses.ApiStatus
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.forgotpassword.data.ForgotPasswordRepository
import io.dolphin.move.android.features.forgotpassword.domain.dto.DropPasswordDto
import javax.inject.Inject

class RequestDropPasswordInteractor @Inject constructor(
    private val forgotPasswordRepository: ForgotPasswordRepository,
) {
    suspend operator fun invoke(changePasswordDto: DropPasswordDto): State<ApiStatus> {
        return forgotPasswordRepository.requestDropPassword(changePasswordDto)
    }
}