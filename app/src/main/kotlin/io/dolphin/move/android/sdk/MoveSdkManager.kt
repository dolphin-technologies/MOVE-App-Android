package io.dolphin.move.android.sdk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import io.dolphin.move.*
import io.dolphin.move.android.BuildConfig
import io.dolphin.move.android.R
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.sdk.data.UpdateTokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for interacting with the MOVE SDK.
 */
interface MoveSdkManager {
    fun getMoveSdk(): MoveSdk?

    /**
     *
     * Flow to receive [MoveSdkState] changes.
     *
     * @return [StateFlow] of [MoveSdkState] if there is a value or [MoveSdkState.Uninitialised]
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/models/untitled#trip-state-listener">MOVE SDK Wiki Trip State Listener</a>
     */
    fun fetchMoveStateFlow(): StateFlow<MoveSdkState>

    /**
     *
     * Flow to receive [MoveTripState] changes.
     *
     * @return [StateFlow] of [MoveTripState] if there is a value or [MoveTripState.UNKNOWN]
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/models/untitled#trip-state-listener">MOVE SDK Wiki Trip State Listener</a>
     */
    fun fetchTripStateFlow(): StateFlow<MoveTripState>

    /**
     *
     * Flow to receive [MoveConfigurationError] changes.
     *
     * @return [StateFlow] of [MoveConfigurationError] if there is a value or null
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/models/moveconfigurationerror">MOVE SDK Wiki MoveConfigurationError</a>
     */
    fun fetchConfigErrorFlow(): StateFlow<MoveConfigurationError?>

    /**
     *
     * Flow to receive a list of [MoveServiceFailure].
     *
     * @return [StateFlow] of [List] of [MoveServiceFailure] if there are warnings or an empty list
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/models/untitled#service-failure-callback">MOVE SDK Wiki Service Failure Callback</a>
     */
    fun fetchErrorsFlow(): StateFlow<List<MoveServiceFailure>>

    /**
     *
     * Flow to receive a list of [MoveServiceWarning].
     *
     * @return [StateFlow] of [List] of [MoveServiceWarning] if there are warnings or an empty list
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/models/untitled#service-warning-callback">MOVE SDK Wiki Service Warning Callback</a>
     */
    fun fetchWarningsFlow(): StateFlow<List<MoveServiceWarning>>

    /**
     *
     * Flow to receive [MoveAssistanceCallStatus] changes.
     *
     * @return [StateFlow] of [MoveAssistanceCallStatus] if there is a value or null
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android/services#initiate-assistance-call">MOVE SDK Wiki Assistance Call</a>
     */
    fun fetchAssistanceStateFlow(): StateFlow<MoveAssistanceCallStatus?>

    /**
     *
     * Flow to receive [MoveHealthScore] changes.
     *
     * @return [StateFlow] of [MoveHealthScore] if there is a value or null
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android/services#set-health-score-listener">MOVE SDK Wiki Health Score Listener</a>
     */
    fun fetchHealthScoreFlow(): StateFlow<MoveHealthScore?>

    /**
     *
     * Transforms the MOVE SDK credentials into a MoveAuth object and after success
     * starts the setup process.
     *
     * @param authCode
     * @see <a href="https://docs.movesdk.com/move-platform/backend/example-request">MOVE SDK Wiki Example-Request</a>
     */
    fun setupMoveSdk(
        authCode: String?
    )

    /**
     *
     * Register desired notification listeners (e.g. recognition, driving, walking),
     * MOVE SDK listeners (e.g. sdkStateListener, tripStateListener, authStateUpdateListener, ...)
     * and configure MOVE SDK features (e.g. consoleLogging, allowMockLocations).
     *
     * @param moveSdk
     */
    fun registerMoveSdkFeatures(moveSdk: MoveSdk)

    /**
     *
     * Creates the config and setup the MOVE SDK with the local stored user credentials.
     *
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android-1/builder#setup">MOVE SDK Wiki Setup</a>
     */
    fun setupAndStart()
}

private const val NOTIFICATION_CHANNEL = "TRIP_CHANNEL"

/**
 *
 * MoveSdkManager to config, setup and interact with the MOVE SDK.
 *
 * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android-1/builder">MOVE SDK Wiki Initialization</a>
 */
@Singleton
class MoveSdkManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userStorage: UserStorage,
    private val updateTokenRepository: UpdateTokenRepository,
) : CoroutineScope, MoveSdkManager {

    private val job = Job()
    override val coroutineContext = Dispatchers.IO + job

    private val moveStateFlow = MutableStateFlow<MoveSdkState>(MoveSdkState.Uninitialised)
    private val moveAuthStateFlow = MutableStateFlow<MoveAuthState>(MoveAuthState.UNKNOWN)
    private val moveTripStateFlow = MutableStateFlow(MoveTripState.UNKNOWN)
    private val moveConfigErrorFlow = MutableStateFlow<MoveConfigurationError?>(null)
    private val moveErrorsFlow = MutableStateFlow<List<MoveServiceFailure>>(emptyList())
    private val moveWarningsFlow = MutableStateFlow<List<MoveServiceWarning>>(emptyList())
    private val assistanceStateFlow = MutableStateFlow<MoveAssistanceCallStatus?>(null)
    private val healthScoreFlow = MutableStateFlow<MoveHealthScore?>(null)

    init {
        Timber.i("Running MOVE SDK version ${MoveSdk.version}")
    }

    override fun registerMoveSdkFeatures(moveSdk: MoveSdk) {
        // Let's configure the MoveSdk and register all the listeners for your usage.
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name: CharSequence = context.getString(R.string.notification_driving)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(NOTIFICATION_CHANNEL, name, importance)
        notificationManager.createNotificationChannel(mChannel)

        val recognitionNotification = createNotificationBuilder(
            titleId = R.string.notification_recognition,
            textId = R.string.waiting_for_trip,
        )
        val drivingNotification = createNotificationBuilder(
            titleId = R.string.notification_driving,
            textId = R.string.trip_active,
        )
        val walkingNotification = createNotificationBuilder(
            titleId = R.string.notification_recognition,
            textId = R.string.subtit_walking,
        )

        moveSdk.apply {
            recognitionNotification(recognitionNotification)
            tripNotification(drivingNotification) // If the device is on a trip you can show an other notification icon.,
            walkingLocationNotification(walkingNotification) // notification for places and walking path
            sdkStateListener(sdkStateListener)
            tripStateListener(tripStateListener)
            authStateUpdateListener(authStateListener)
            initializationListener(initListener)
            setServiceWarningListener(warningListener)
            setServiceErrorListener(errorListener)
            setMoveHealthScoreListener(healthScoreListener)
            consoleLogging(true)
            allowMockLocations(true) // mock location not recommended for use in production
        }
    }

    /**
     *
     * Creation of the MOVE Config and trigger the setup of the MOVE SDK.
     *
     * @param authCode
     */
    override fun setupMoveSdk(authCode: String?) {
        // call this after registration/login only (once in a lifetime)
        val moveConfig = createMoveConfig()
        getMoveSdk()?.setup(authCode = authCode?: "", config = moveConfig, start = false, callback = authCallback)
    }

    override fun setupAndStart() {
        userStorage.getUser()?.let {
            val moveConfig = createMoveConfig()
            getMoveSdk()?.setup(authCode = it.sdkUserAuthCode?: "", config = moveConfig, start = true, callback = authCallback)
        }
    }

    override fun getMoveSdk(): MoveSdk? {
        return MoveSdk.get()
    }

    override fun fetchMoveStateFlow(): StateFlow<MoveSdkState> {
        return moveStateFlow
    }

    override fun fetchTripStateFlow(): StateFlow<MoveTripState> {
        return moveTripStateFlow
    }

    override fun fetchConfigErrorFlow(): StateFlow<MoveConfigurationError?> {
        return moveConfigErrorFlow
    }

    override fun fetchErrorsFlow(): StateFlow<List<MoveServiceFailure>> {
        return moveErrorsFlow
    }

    override fun fetchWarningsFlow(): StateFlow<List<MoveServiceWarning>> {
        return moveWarningsFlow
    }

    override fun fetchAssistanceStateFlow(): StateFlow<MoveAssistanceCallStatus?> {
        return assistanceStateFlow
    }

    override fun fetchHealthScoreFlow(): StateFlow<MoveHealthScore?> {
        return healthScoreFlow
    }

    private val initListener: MoveSdk.InitializeListener = object : MoveSdk.InitializeListener {

        // Triggers whenever an error during the MOVE SDK initialization occurs.
        override fun onError(error: MoveConfigurationError) {
            Timber.tag("MoveConfigurationError").e(error.toString())
            launch {
                moveConfigErrorFlow.emit(error)
            }
            when (error) {
                is MoveConfigurationError.ServiceUnreachable -> {
                    Timber.tag("MoveConfigurationError")
                        .e("The connection to our servers failed. Please ensure that you have a valid internet connection. If the problem still remains, please contact customer support")
                }
            }
        }
    }

    private val sdkStateListener: MoveSdk.StateListener = object : MoveSdk.StateListener {

        private var lastState: MoveSdkState = MoveSdkState.Uninitialised

        override fun onStateChanged(sdk: MoveSdk, state: MoveSdkState) {
            moveStateFlow.update { state }
            if (BuildConfig.DEBUG && state == MoveSdkState.Running) {
                sdk.forceTripRecognition()
            }
            lastState = state
        }
    }

    private val tripStateListener = object : MoveSdk.TripStateListener {
        override fun onTripStateChanged(tripState: MoveTripState) {
            Timber.tag("MoveTripState").d(tripState.toString())
            moveTripStateFlow.value = tripState
        }
    }

    private val authStateListener = object : MoveSdk.AuthStateUpdateListener {
        override fun onAuthStateUpdate(state: MoveAuthState) {
            // Triggers whenever the MoveAuthState changes.
            moveAuthStateFlow.value = state

            when (state) {
                is MoveAuthState.VALID -> {
                    // Authentication is valid. Latest MoveAuth provided.
                }
                is MoveAuthState.UNKNOWN -> {
                    // The SDK authorization state when SDK is uninitialized.
                }

                is MoveAuthState.INVALID -> {
                    // Latest MoveAuth is invalid.
                }

                else -> {}
            }
        }
    }

    private val warningListener = object : MoveSdk.MoveWarningListener {
        override fun onMoveWarning(serviceWarnings: List<MoveServiceWarning>) {
            moveWarningsFlow.value = serviceWarnings
            serviceWarnings.forEach { moveServiceWarning ->
                moveServiceWarning.warnings.forEach { moveWarning ->
                    Timber.w(
                        "${
                            moveServiceWarning.service?.name().orEmpty()
                        } - ${moveWarning.name}"
                    )
                }
            }
        }
    }

    private val errorListener = object : MoveSdk.MoveErrorListener {
        override fun onMoveError(serviceFailures: List<MoveServiceFailure>) {
            moveErrorsFlow.value = serviceFailures
            serviceFailures.forEach { moveServiceFailure ->
                moveServiceFailure.reasons.forEach { reasons ->
                    Timber.e("${moveServiceFailure.service.name()} - ${reasons.name}")
                }
            }
        }
    }

    /**
     * Example how to implement the assistanceListener.
     */
/*
    private val assistanceListener = object : MoveSdk.AssistanceStateListener {
        override fun onAssistanceStateChanged(assistanceState: MoveAssistanceCallStatus) {
            assistanceStateFlow.value = assistanceState
            Timber.tag("MoveAssistanceCallStatus").d(assistanceState.name)
        }
    }
*/

    private val healthScoreListener = object : MoveSdk.MoveHealthScoreListener {
        override fun onMoveHealthScoreChanged(score: MoveHealthScore) {
            healthScoreFlow.value = score
            Timber.tag("MoveHealthScore").d("e.g. Battery at ${score.battery}%")
        }
    }

    private val authCallback = object : MoveSdk.MoveAuthCallback {
        override fun onResult(result: MoveAuthResult) {
            Timber.i("${result.status} ${result.description}")
        }
    }

    /**
     *
     * Returns the MOVE Config.
     * Add some MOVE services (e.g. driving, walking)
     * and MOVE detection services (e.g. driving, walking, cycling, ...)
     * to the MOVE Config.
     *
     * @return [MoveConfig]
     */
    private fun createMoveConfig(): MoveConfig {
        // MoveSdk services configuration
        val moveServices: MutableSet<MoveDetectionService> = mutableSetOf()

        val drivingServices: MutableSet<DrivingService> = mutableSetOf()

        drivingServices.add(DrivingService.DrivingBehaviour)
        drivingServices.add(DrivingService.DistractionFreeDriving)

        // Walking location and Places are experimental features and not available at this moment.
        // val walkingServices: MutableSet<WalkingService> = mutableSetOf()
        // walkingServices.add(WalkingService.Location)
        // moveServices.add(MoveDetectionService.Places)

        moveServices.add(MoveDetectionService.Driving(drivingServices = drivingServices.toList()))
        moveServices.add(MoveDetectionService.Walking(/*walkingServices = walkingServices.toList()*/))
        moveServices.add(MoveDetectionService.Cycling)
        moveServices.add(MoveDetectionService.PublicTransport)
        moveServices.add(MoveDetectionService.PointsOfInterest)
        moveServices.add(MoveDetectionService.AutomaticImpactDetection)
        moveServices.add(MoveDetectionService.AssistanceCall)

        return MoveConfig(
            moveDetectionServices = moveServices.toList(),
        )
    }

    /**
     *
     * To circumvent several background limitations with the Android OS the application needs to show
     * a foreground service with a notification visible to the user. (e.g. recognition, trip, walking)
     *
     * @param titleId The string resource id of the notification title.
     * @param textId the string resource id of the
     * @see <a href="https://docs.movesdk.com/move-platform/sdk/appendix/android/notification-managment">MOVE SDK Wiki Notification Management</a>
     * @return [NotificationCompat.Builder]
     */
    private fun createNotificationBuilder(
        @StringRes titleId: Int,
        @StringRes textId: Int
    ): NotificationCompat.Builder {
        val intentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else 0
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        var contentIntent: PendingIntent? = null
        if (intent != null) {
            contentIntent = PendingIntent.getActivity(context, 0, intent, intentFlags)
        }
        val contentTitle = context.getString(titleId)
        val contentText = context.getString(textId)
        val channelId = NOTIFICATION_CHANNEL
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setChannelId(channelId)
        if (contentIntent != null) { // unit test may have null values
            builder.setContentIntent(contentIntent)
        }
        builder.setOngoing(true)
        return builder
    }
}
