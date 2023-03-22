package io.dolphin.move.android.features.permissions.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.features.permissions.presentation.components.PermissionCard
import io.dolphin.move.android.features.permissions.presentation.components.PermissionStateHeader
import io.dolphin.move.android.features.permissions.presentation.components.PermissionsStateHeaderAdapter
import io.dolphin.move.android.features.permissions.presentation.components.PermissionsStateHeaderView
import io.dolphin.move.android.ui.MainActivity
import io.dolphin.move.android.ui.theme.color_bg_card
import io.dolphin.move.android.ui.theme.dark_grey

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
fun PermissionsScreen(
    permissionsViewModel: PermissionsViewModel = hiltViewModel()
) {
    val permissionsUiHeaderState by permissionsViewModel.permissionsUiHeaderState.collectAsState()
    val permissionsUiState by permissionsViewModel.permissionsUiState.collectAsState()
    val permissionsUiFooterState by permissionsViewModel.permissionsUiFooterState.collectAsState()

    val permissionsStates = PermissionsStates()

    permissionsStates.locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    permissionsStates.backgroundLocationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else null

    permissionsStates.phoneCallPermissionState = rememberPermissionState(permission = Manifest.permission.READ_PHONE_STATE)

    permissionsStates.motionPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)
    } else null

    permissionsStates.bluetoothPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        rememberPermissionState(permission = Manifest.permission.BLUETOOTH_CONNECT)
    } else null

    permissionsStates.notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else null

    val activity = LocalContext.current as MainActivity
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_CREATE) {
                    permissionsViewModel.load(activity, permissionsStates)
                }
                if (event == Lifecycle.Event.ON_RESUME) {
                    permissionsViewModel.updateLocationPermissionViews(
                        permissionsStates.locationPermissionState,
                        permissionsStates.backgroundLocationPermissionState
                    )
                    permissionsViewModel.updatePhoneCallPermissionViews(permissionsStates.phoneCallPermissionState)
                    permissionsViewModel.updateMotionPermissionViews(permissionsStates.motionPermissionState)
                    permissionsViewModel.updateBluetoothPermissionViews(permissionsStates.bluetoothPermissionState)
                    permissionsViewModel.updateNotificationPermissionViews(permissionsStates.notificationPermissionState)
                    val ignoringBatteryOptimizations = permissionsViewModel.isIgnoringBatteryOptimizations(activity)
                    permissionsViewModel.updateBatteryOptimization(ignoringBatteryOptimizations)
                    permissionsViewModel.updateOverlayPermission(Settings.canDrawOverlays(activity))
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    PermissionsScreenContent(
        activity = activity,
        permissionsView = permissionsViewModel,
        permissionsStateHeaderView = permissionsViewModel,
        permissionsUiHeaderState = permissionsUiHeaderState,
        permissionsUiState = permissionsUiState,
        permissionsUiFooterState = permissionsUiFooterState,
        permissionsStates = permissionsStates,
    )
}

@SuppressLint("BatteryLife")
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Composable
private fun PermissionsScreenContent(
    activity: MainActivity?,
    permissionsView: PermissionsView,
    permissionsStateHeaderView: PermissionsStateHeaderView,
    permissionsUiHeaderState: PermissionsUiHeaderState,
    permissionsUiState: PermissionsUiState,
    permissionsUiFooterState: PermissionsUiFooterState,
    permissionsStates: PermissionsStates,
) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp)
                .verticalScroll(rememberScrollState())
        ) {
            PermissionStateHeader(
                permissionsStateHeaderView = permissionsStateHeaderView,
                permissionsUiHeaderState = permissionsUiHeaderState,
            )

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                TextBold(
                    text = stringResource(id = R.string.subtit_permissions),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 14.dp),
                    textAlign = TextAlign.Start,
                )
                TextNormal(
                    text = stringResource(id = R.string.txt_permissions),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 14.dp),
                    textAlign = TextAlign.Justify,
                )
            }

            PermissionCard(
                title = stringResource(id = R.string.permission_location_title),
                text = stringResource(id = R.string.permission_location_text),
                buttonText = stringResource(id = permissionsUiState.locationButtonText),
                buttonColor = permissionsUiState.locationButtonColor,
                onClick = {
                    permissionsStates.locationPermissionState?.launchPermissionRequest()
                }
            )

            PermissionCard(
                title = stringResource(id = R.string.permission_phone_calls_title),
                text = stringResource(id = R.string.permission_phone_calls_text),
                buttonText = stringResource(id = permissionsUiState.phoneCallButtonText),
                buttonColor = permissionsUiState.phoneCallButtonColor,
                onClick = {
                    permissionsStates.phoneCallPermissionState?.launchPermissionRequest()
                }
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PermissionCard(
                    title = stringResource(id = R.string.permission_motion_title),
                    text = stringResource(id = R.string.permission_motion_text),
                    buttonText = stringResource(id = permissionsUiState.motionButtonText),
                    buttonColor = permissionsUiState.motionButtonColor,
                    onClick = {
                        permissionsStates.motionPermissionState?.launchPermissionRequest()
                    }
                )
            }

            PermissionCard(
                title = stringResource(id = R.string.permission_overlay_title),
                text = stringResource(id = R.string.permission_overlay_text),
                buttonText = stringResource(id = permissionsUiState.overlayButtonText),
                buttonColor = permissionsUiState.overlayButtonColor,
                onClick = {
                    if (!Settings.canDrawOverlays(activity)) {
                        launchSettings(activity, Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    }
                }
            )

            PermissionCard(
                title = stringResource(id = R.string.permission_battery_title),
                text = stringResource(id = R.string.permission_battery_text),
                buttonText = stringResource(id = permissionsUiState.batteryButtonText),
                buttonColor = permissionsUiState.batteryButtonColor,
                onClick = {
                    val ignoringBatteryOptimizations = permissionsView.isIgnoringBatteryOptimizations(activity)
                    if (!ignoringBatteryOptimizations) {
                        launchSettings(activity, Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    }
                }
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PermissionCard(
                    title = stringResource(id = R.string.permission_notifications_title),
                    text = stringResource(id = R.string.permission_notifications_text),
                    buttonText = stringResource(id = permissionsUiState.notificationsButtonText),
                    buttonColor = permissionsUiState.notificationsButtonColor,
                    onClick = {
                        permissionsStates.notificationPermissionState?.launchPermissionRequest()
                    }
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PermissionCard(
                    title = stringResource(id = R.string.permission_bluetooth_title),
                    text = stringResource(id = R.string.permission_bluetooth_text),
                    buttonText = stringResource(id = permissionsUiState.bluetoothButtonText),
                    buttonColor = permissionsUiState.bluetoothButtonColor,
                    onClick = {
                        permissionsStates.bluetoothPermissionState?.launchPermissionRequest()
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(color_bg_card)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    TextBold(
                        text = stringResource(id = R.string.permissions_sdk_state),
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        color = dark_grey
                    )
                    TextNormal(
                        modifier = Modifier.padding(start = 5.dp),
                        text = permissionsUiFooterState.sdkStateText.uppercase(),
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        color = dark_grey
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    TextBold(
                        text = stringResource(id = R.string.permissions_sdk_trip_state),
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        color = dark_grey
                    )
                    TextNormal(
                        modifier = Modifier.padding(start = 5.dp),
                        text = permissionsUiFooterState.sdkTripStateText.uppercase(),
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        color = dark_grey
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalFoundationApi
private fun launchSettings(activity: MainActivity?, destination: String) {
    val intent = Intent(
        destination,
        Uri.parse("package:${activity?.packageName}")
    )
    activity?.startForResult?.launch(intent)
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PermissionsScreenPreview() {
    PermissionsScreenContent(
        activity = null,
        permissionsView = PreviewPermissionsViewAdapter,
        permissionsStateHeaderView = PermissionsStateHeaderAdapter,
        permissionsUiHeaderState = PermissionsUiHeaderState(),
        permissionsUiState = PermissionsUiState(),
        permissionsUiFooterState = PermissionsUiFooterState(),
        permissionsStates = PermissionsStates()
    )
}

@ExperimentalPermissionsApi
private object PreviewPermissionsViewAdapter : PermissionsView {
    override fun updateLocationPermissionViews(locationPermissionState: PermissionState?, backgroundPermissionState: PermissionState?) {}
    override fun updatePhoneCallPermissionViews(permissionState: PermissionState?) {}
    override fun updateMotionPermissionViews(permissionState: PermissionState?) {}
    override fun updateBluetoothPermissionViews(permissionState: PermissionState?) {}
    override fun updateNotificationPermissionViews(permissionState: PermissionState?) {}
    override fun updateBatteryOptimization(ignoring: Boolean) {}
    override fun updateOverlayPermission(canOverlay: Boolean) {}
    override fun isIgnoringBatteryOptimizations(context: Context?): Boolean { return true }
    override fun load(context: Context?, permissionsStates: PermissionsStates) {}
}

@ExperimentalPermissionsApi
data class PermissionsStates constructor(
    var locationPermissionState: PermissionState? = null,
    var backgroundLocationPermissionState: PermissionState? = null,
    var phoneCallPermissionState: PermissionState? = null,
    var motionPermissionState: PermissionState? = null,
    var notificationPermissionState: PermissionState? = null,
    var bluetoothPermissionState: PermissionState? = null,
)

