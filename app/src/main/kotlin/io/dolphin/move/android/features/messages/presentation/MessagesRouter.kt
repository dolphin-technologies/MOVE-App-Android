package io.dolphin.move.android.features.messages.presentation

interface MessagesRouter {
    fun showMessagesScreen()
    fun showMessageDetailsScreen(id: Long)
}