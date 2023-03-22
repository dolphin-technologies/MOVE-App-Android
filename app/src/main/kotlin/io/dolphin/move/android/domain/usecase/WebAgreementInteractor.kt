package io.dolphin.move.android.domain.usecase

import io.dolphin.move.android.basedata.storage.common.WebAgreementStorage
import io.dolphin.move.android.domain.entities.Agreement
import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow

class WebAgreementInteractor @Inject constructor(
    private val webAgreementStorage: WebAgreementStorage,
) {

    suspend fun removeAccepted(source: Agreement) {
        webAgreementStorage.removeAccepted(source)
    }

    suspend fun acceptAgreement(source: Agreement) {
        webAgreementStorage.acceptAgreement(source)
    }

    fun getAccepted(): SharedFlow<Set<Agreement>> {
        return webAgreementStorage.getAccepted()
    }
}