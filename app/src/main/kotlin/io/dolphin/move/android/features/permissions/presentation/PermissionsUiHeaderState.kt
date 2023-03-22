package io.dolphin.move.android.features.permissions.presentation

import androidx.compose.ui.graphics.Color
import io.dolphin.move.android.R
import io.dolphin.move.android.ui.theme.*

data class PermissionsUiHeaderState(
    val currentStateText: Int = R.string.lbl_not_recording,
    val currentStateBackground: List<Color> = listOf(
        color_current_state_off_start,
        color_current_state_off_middle,
        color_current_state_off_end
    ),
    val currentStateChecked: Boolean = false,
)


