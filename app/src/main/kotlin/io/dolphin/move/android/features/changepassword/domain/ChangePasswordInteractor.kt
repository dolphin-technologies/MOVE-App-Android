package io.dolphin.move.android.features.changepassword.domain

import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.changepassword.data.ChangePasswordRepository
import io.dolphin.move.android.features.changepassword.domain.dto.ChangePasswordDto
import javax.inject.Inject

class ChangePasswordInteractor @Inject constructor(
    private val changePasswordRepository: ChangePasswordRepository,
) {
    suspend fun requestChangePassword(changePasswordDto: ChangePasswordDto): State<Boolean> {
        return changePasswordRepository.requestChangePassword(changePasswordData = changePasswordDto)
    }

}