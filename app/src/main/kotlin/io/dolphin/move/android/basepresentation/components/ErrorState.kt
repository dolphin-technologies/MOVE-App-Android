package io.dolphin.move.android.basepresentation.components

import androidx.annotation.StringRes
import io.dolphin.move.android.R

data class ErrorState(
    @StringRes val titleId: Int = R.string.err_tit_error,
    @StringRes val description: Int = R.string.err_network_error,
)