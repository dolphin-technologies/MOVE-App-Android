package io.dolphin.move.android.basepresentation

sealed interface AppEvent {
    object Logout : AppEvent
}