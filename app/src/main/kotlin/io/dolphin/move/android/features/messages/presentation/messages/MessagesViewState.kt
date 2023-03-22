package io.dolphin.move.android.features.messages.presentation.messages

import io.dolphin.move.android.basepresentation.components.ErrorState

data class MessagesViewState(
    val messages: List<MessageViewState> = emptyList(),
    val error: ErrorState? = null,
    val onRemoveErrorReset: Boolean = false,
)

data class MessageViewState(
    val id: Long,
    val url: String,
    val isNew: Boolean,
    val title: String,
    val date: String,
    val description: String,
)
