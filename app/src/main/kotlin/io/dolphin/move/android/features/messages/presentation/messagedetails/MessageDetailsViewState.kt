package io.dolphin.move.android.features.messages.presentation.messagedetails

data class MessageDetailsViewState(
    val id: Long = -1L,
    val isNew: Boolean = false,
    val date: String = "",
    val title: String = "",
    val contentUrl: String = "",
    val prevIndex: Int? = null,
    val nextIndex: Int? = null,
)
