package io.dolphin.move.android.features.messages.domain.dto

data class UpdateMessageDto(
    val id: Long,
    val markAsRead: Boolean? = null,
    val deleted: Boolean? = null,
)