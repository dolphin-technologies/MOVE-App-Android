package io.dolphin.move.android.features.messages.domain

import io.dolphin.move.android.basedata.local.database.messages.MoveMessage
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.messages.data.MessagesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class MessagesInteractor @Inject constructor(
    private val messagesRepository: MessagesRepository,
) {
    suspend fun requestMessages(): State<Unit> {
        return messagesRepository.requestMessages()
    }

    fun getMessagesFlow(): Flow<List<MoveMessage>> {
        return messagesRepository.getMessagesFlow()
    }
}