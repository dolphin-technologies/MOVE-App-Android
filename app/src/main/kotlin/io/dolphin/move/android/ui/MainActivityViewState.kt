package io.dolphin.move.android.ui

import androidx.annotation.StringRes
import io.dolphin.move.android.R

data class MainActivityViewState(
    @StringRes val titleId: Int = R.string.tit_move,
    val showBackButton: Boolean = false,
    val showMessagesIcon: Boolean = false,
    val unreadMessagesCount: Int = 0,
)