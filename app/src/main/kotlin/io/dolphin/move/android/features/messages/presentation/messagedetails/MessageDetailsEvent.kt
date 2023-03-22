package io.dolphin.move.android.features.messages.presentation.messagedetails

import io.dolphin.move.android.basepresentation.viewmodel.Event

sealed class MessageDetailsEvent : Event() {
    object NoMessages : MessageDetailsEvent()
    object None : MessageDetailsEvent()
}