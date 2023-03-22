package io.dolphin.move.android.features.permissions.presentation

import android.content.Context
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@ExperimentalPermissionsApi
interface PermissionsView {
    fun updateLocationPermissionViews(locationPermissionState: PermissionState?, backgroundPermissionState: PermissionState?)

    fun updatePhoneCallPermissionViews(permissionState: PermissionState?)

    fun updateMotionPermissionViews(permissionState: PermissionState?)

    fun updateBluetoothPermissionViews(permissionState: PermissionState?)

    fun updateNotificationPermissionViews(permissionState: PermissionState?)

    fun updateBatteryOptimization(ignoring: Boolean)

    fun updateOverlayPermission(canOverlay: Boolean)

    fun isIgnoringBatteryOptimizations(context: Context?): Boolean

    fun load(context: Context?, permissionsStates: PermissionsStates)
}