package io.dolphin.move.android.basepresentation.viewmodel

abstract class Event {
    open val titleId: Int? = null
    open val messageId: Int? = null
    open val message: String? = null
}
