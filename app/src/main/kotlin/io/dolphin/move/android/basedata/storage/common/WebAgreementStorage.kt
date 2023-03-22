package io.dolphin.move.android.basedata.storage.common

import io.dolphin.move.android.basedata.storage.AppMemory
import io.dolphin.move.android.basedata.storage.set
import io.dolphin.move.android.domain.entities.Agreement
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface WebAgreementStorage {
    suspend fun removeAccepted(source: Agreement)
    suspend fun acceptAgreement(source: Agreement)
    fun getAccepted(): SharedFlow<Set<Agreement>>
}

class WebAgreementInMemoryStorage @Inject constructor(
    appMemory: AppMemory,
) : WebAgreementStorage {

    private val acceptedSet: MutableSet<Agreement> by appMemory.set()
    private val acceptedFlow: MutableSharedFlow<MutableSet<Agreement>> =
        MutableSharedFlow(replay = 1)

    override suspend fun removeAccepted(source: Agreement) {
        acceptedSet.remove(source)
        acceptedFlow.emit(acceptedSet)
    }

    override suspend fun acceptAgreement(source: Agreement) {
        acceptedSet.add(source)
        acceptedFlow.emit(acceptedSet)
    }

    override fun getAccepted(): SharedFlow<Set<Agreement>> {
        return acceptedFlow
    }
}