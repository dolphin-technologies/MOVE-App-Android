package io.dolphin.move.android.features.permissions.presentation

import androidx.compose.ui.graphics.Color
import io.dolphin.move.android.R
import io.dolphin.move.android.ui.theme.white

data class PermissionsUiState(
    val locationButtonText: Int = R.string.btn_continue,
    val locationButtonColor: Color = white,

    val motionButtonText: Int = R.string.btn_continue,
    val motionButtonColor: Color = white,

    val phoneCallButtonText: Int = R.string.btn_continue,
    val phoneCallButtonColor: Color = white,

    val overlayButtonText: Int = R.string.btn_continue,
    val overlayButtonColor: Color = white,

    val batteryButtonText: Int = R.string.btn_continue,
    val batteryButtonColor: Color = white,

    val notificationsButtonText: Int = R.string.btn_continue,
    val notificationsButtonColor: Color = white,

    val bluetoothButtonText: Int = R.string.btn_continue,
    val bluetoothButtonColor: Color = white,
)

