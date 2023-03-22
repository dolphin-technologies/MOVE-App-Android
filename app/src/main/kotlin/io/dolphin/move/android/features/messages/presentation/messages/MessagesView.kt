package io.dolphin.move.android.features.messages.presentation.messages

interface MessagesView {
    fun onRemoveItem(id: Long)
    fun markAsRead(id: Long)
    fun retryOnError()
}