package io.dolphin.move.android.features.messages.domain

import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.messages.data.MessagesRepository
import io.dolphin.move.android.features.messages.domain.dto.UpdateMessageDto
import javax.inject.Inject

class RemoveMessageInteractor @Inject constructor(
    private val messagesRepository: MessagesRepository,
) {
    suspend operator fun invoke(id: Long): State<Unit> {
        return messagesRepository.requestUpdateMessage(
            dto = UpdateMessageDto(id = id, deleted = true),
        )
    }
}