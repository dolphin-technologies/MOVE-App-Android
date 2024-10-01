package io.dolphin.move.android.features.permissions.presentation

import android.content.Context
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.google.accompanist.permissions.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.MoveSdkState
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.features.permissions.presentation.components.PermissionsStateHeaderView
import io.dolphin.move.android.sdk.domain.MoveSdkManagerInteractor
import io.dolphin.move.android.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * ViewModel for the Permission screen.
 * To start the MOVE SDK all the mandatory permissions must be granted by the user.
 *
 * @see <a href="https://docs.movesdk.com/move-platform/sdk/appendix/ios/permission-handling">MOVE SDK Permission Handling</a>
 */
@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val moveSdkManagerInteractor: MoveSdkManagerInteractor,
) : BaseViewModel(), CoroutineScope, PermissionsView, PermissionsStateHeaderView {

    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    private val _permissionsUiState = MutableStateFlow(PermissionsUiState())
    val permissionsUiState: StateFlow<PermissionsUiState> = _permissionsUiState.asStateFlow()

    private val _permissionsUiHeaderState = MutableStateFlow(PermissionsUiHeaderState())
    val permissionsUiHeaderState: StateFlow<PermissionsUiHeaderState> =
        _permissionsUiHeaderState.asStateFlow()

    private val _permissionsUiFooterState = MutableStateFlow(PermissionsUiFooterState())
    val permissionsUiFooterState: StateFlow<PermissionsUiFooterState> =
        _permissionsUiFooterState.asStateFlow()

    override fun updateLocationPermissionViews(
        locationPermissionState: PermissionState?,
        backgroundPermissionState: PermissionState?
    ) {
        locationPermissionState?.let { permState ->
            when {
                permState.status.isGranted -> {
                    _permissionsUiState.update {
                        it.copy(
                            locationButtonText = R.string.btn_granted,
                            locationButtonColor = color_permissions_granted,
                        )
                    }
                    backgroundPermissionState?.let {
                        backgroundPermissionState.launchPermissionRequest()
                    }
                }
                permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            locationButtonText = R.string.btn_continue,
                            locationButtonColor = color_permissions_continue,
                        )
                    }
                }
                !permState.status.isGranted && !permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            locationButtonText = R.string.btn_continue,
                            locationButtonColor = color_permissions_continue,
                        )
                    }
                }
                else -> {}
            }
        }
    }

    override fun updatePhoneCallPermissionViews(permissionState: PermissionState?) {
        permissionState?.let { permState ->
            when {
                permState.status.isGranted -> {
                    _permissionsUiState.update {
                        it.copy(
                            phoneCallButtonText = R.string.btn_granted,
                            phoneCallButtonColor = color_permissions_granted,
                        )
                    }
                }
                permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            phoneCallButtonText = R.string.btn_continue,
                            phoneCallButtonColor = color_permissions_continue,
                        )
                    }
                }
                !permState.status.isGranted && !permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            phoneCallButtonText = R.string.btn_continue,
                            phoneCallButtonColor = color_permissions_continue,
                        )
                    }
                }
            }
        }
    }

    override fun updateMotionPermissionViews(permissionState: PermissionState?) {
        permissionState?.let { permState ->
            when {
                permState.status.isGranted -> {
                    _permissionsUiState.update {
                        it.copy(
                            motionButtonText = R.string.btn_granted,
                            motionButtonColor = color_permissions_granted,
                        )
                    }
                }
                permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            motionButtonText = R.string.btn_continue,
                            motionButtonColor = color_permissions_continue,
                        )
                    }
                }
                !permState.status.isGranted && !permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            motionButtonText = R.string.btn_continue,
                            motionButtonColor = color_permissions_continue,
                        )
                    }
                }
            }
        }
    }

    override fun updateBluetoothPermissionViews(permissionState: PermissionState?) {
        permissionState?.let { permState ->
            when {
                permState.status.isGranted -> {
                    _permissionsUiState.update {
                        it.copy(
                            bluetoothButtonText = R.string.btn_granted,
                            bluetoothButtonColor = color_permissions_granted,
                        )
                    }
                }
                permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            bluetoothButtonText = R.string.btn_continue,
                            bluetoothButtonColor = color_permissions_continue,
                        )
                    }
                }
                !permState.status.isGranted && !permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            bluetoothButtonText = R.string.btn_continue,
                            bluetoothButtonColor = color_permissions_continue,
                        )
                    }
                }
            }
        }
    }

    override fun updateNotificationPermissionViews(permissionState: PermissionState?) {
        permissionState?.let { permState ->
            when {
                permState.status.isGranted -> {
                    _permissionsUiState.update {
                        it.copy(
                            notificationsButtonText = R.string.btn_granted,
                            notificationsButtonColor = color_permissions_granted,
                        )
                    }
                }
                permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            notificationsButtonText = R.string.btn_continue,
                            notificationsButtonColor = color_permissions_continue,
                        )
                    }
                }
                !permState.status.isGranted && !permState.status.shouldShowRationale -> {
                    _permissionsUiState.update {
                        it.copy(
                            notificationsButtonText = R.string.btn_continue,
                            notificationsButtonColor = color_permissions_continue,
                        )
                    }
                }
            }
        }
    }

    override fun updateBatteryOptimization(ignoring: Boolean) {
        _permissionsUiState.update {
            it.copy(
                batteryButtonText = if (ignoring) R.string.btn_granted else R.string.btn_open_settings,
                batteryButtonColor = if (ignoring) color_permissions_granted else color_permissions_settings,
            )
        }
    }

    override fun updateOverlayPermission(canOverlay: Boolean) {
        _permissionsUiState.update {
            it.copy(
                overlayButtonText = if (canOverlay) R.string.btn_granted else R.string.btn_open_settings,
                overlayButtonColor = if (canOverlay) color_permissions_granted else color_permissions_settings,
            )
        }
    }

    override fun isIgnoringBatteryOptimizations(context: Context?): Boolean {
        context?.let {
            val pm = it.getSystemService(Context.POWER_SERVICE) as PowerManager
            val packageName = it.packageName
            return pm.isIgnoringBatteryOptimizations(packageName)
        }
        return false
    }

    override fun load(context: Context?, permissionsStates: PermissionsStates) {
        moveSdkManagerInteractor.fetchMoveStateFlow()
            .onEach { moveSdkState ->
                _permissionsUiFooterState.update { it.copy(sdkStateText = moveSdkState.name) }
                when (moveSdkState) {
                    is MoveSdkState.Ready -> {
                        _permissionsUiHeaderState.update {
                            it.copy(
                                currentStateChecked = false,
                                currentStateText = R.string.lbl_not_recording,
                                currentStateBackground = listOf(
                                    color_current_state_off_start,
                                    color_current_state_off_middle,
                                    color_current_state_off_end,
                                )
                            )
                        }
                    }
                    is MoveSdkState.Uninitialised -> {
                        _permissionsUiHeaderState.update {
                            it.copy(
                                currentStateChecked = false,
                                currentStateText = R.string.lbl_not_recording,
                                currentStateBackground = listOf(
                                    color_current_state_off_start,
                                    color_current_state_off_middle,
                                    color_current_state_off_end,
                                )
                            )
                        }
                    }
                    is MoveSdkState.Running -> {
                        if (allMandatoryPermissionsGranted(context, permissionsStates)) {
                            _permissionsUiHeaderState.update { permissionHeaderState ->
                                permissionHeaderState.copy(
                                    currentStateChecked = true,
                                    currentStateText = R.string.lbl_rec,
                                    currentStateBackground = listOf(
                                        color_current_state_on_start,
                                        color_current_state_on_middle,
                                        color_current_state_on_end,
                                    )
                                )
                            }
                        } else {
                            turnMoveSdkOff()
                        }
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(this)

        moveSdkManagerInteractor.fetchTripStateFlow()
            .onEach { sdkTripState ->
                _permissionsUiFooterState.update { it.copy(sdkTripStateText = sdkTripState.name) }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(this)
    }

    /**
     *
     * Turns the MOVE SDK on -> startAutomaticDetection() if already initialised
     * or start the MOVE SDK setup process if it is uninitialised.
     *
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android-1/android#start-automatic-detection">MOVE SDK Wiki startAutomaticDetection</a>
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android-1/builder#setup">MOVE SDK Wiki Setup</a>
     */
    override fun turnMoveSdkOn() {
        moveSdkManagerInteractor.getMoveSdk()?.let { sdk ->
            launch {
                when (sdk.getSdkState()) {
                    MoveSdkState.Ready -> sdk.startAutomaticDetection()
                    MoveSdkState.Running -> { /* ignore */
                    }
                    MoveSdkState.Uninitialised -> moveSdkManagerInteractor.setupAndStart()
                }
            }
        }
    }

    /**
     *
     * Turns the MOVE SDK off -> stopAutomaticDetection().
     *
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android-1/android#stop-automatic-detection">MOVE SDK Wiki stopAutomaticDetection</a>
     */
    override fun turnMoveSdkOff() {
        moveSdkManagerInteractor.getMoveSdk()?.stopAutomaticDetection()
    }

    /**
     *
     * Checks if all mandatory permissions are granted by the user.
     *
     * @param context
     * @param permissionsStates
     * @return [Boolean] true if all mandatory permissions are given, false if some permissions are missing
     *
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/appendix/android/permission-handling">MOVE SDK Permission Handling</a>
     */
    private fun allMandatoryPermissionsGranted(
        context: Context?,
        permissionsStates: PermissionsStates
    ): Boolean {
        val locationPermission =
            permissionsStates.locationPermissionState?.status?.isGranted ?: false
        val backgroundPermission =
            permissionsStates.backgroundLocationPermissionState?.status?.isGranted ?: false
        val phonePermission = permissionsStates.phoneCallPermissionState?.status?.isGranted ?: false
        val motionPermission = permissionsStates.motionPermissionState?.status?.isGranted ?: false
        val overlayPermission = Settings.canDrawOverlays(context)
        val batteryOptimization = isIgnoringBatteryOptimizations(context)
        var bluetoothPermission =
            permissionsStates.locationPermissionState?.status?.isGranted ?: false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            bluetoothPermission = true
        }
        return locationPermission && backgroundPermission && phonePermission && motionPermission
                && overlayPermission && batteryOptimization && bluetoothPermission
    }

}
