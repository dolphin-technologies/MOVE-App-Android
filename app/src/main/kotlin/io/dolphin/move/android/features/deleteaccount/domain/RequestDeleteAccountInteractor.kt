package io.dolphin.move.android.features.deleteaccount.domain

import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.deleteaccount.data.DeleteAccountRepository
import javax.inject.Inject

class RequestDeleteAccountInteractor @Inject constructor(
    private val deleteAccountRepository: DeleteAccountRepository,
) {
    suspend operator fun invoke(password: String): State<Boolean> {
        return deleteAccountRepository.requestDeleteAccount(password)
    }
}