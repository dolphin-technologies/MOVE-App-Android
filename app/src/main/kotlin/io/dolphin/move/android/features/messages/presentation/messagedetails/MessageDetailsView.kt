package io.dolphin.move.android.features.messages.presentation.messagedetails

interface MessageDetailsView {
    fun showMessage(index: Int?)
    fun removeMessage(id: Long)
}