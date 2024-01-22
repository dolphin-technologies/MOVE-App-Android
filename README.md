## MOVE App and MOVE SDK

The MOVE SDK enables you to collect location data, motion information and other sensor data from
your users smartphones. This data is then transmitted to our backend, where it is evaluated,
enriched with industry leading machine learning algorithms and applied to a comprehensive 24/7
timeline.

This timeline gives you insights on where your users move and how they get there, be it in the past,
the present or even the future (via our prediction algorithms). With our MOVE SDK, you will gain a
deep understanding of your users personas and service them in a whole new way, completely
transforming the way you interact with them.

The MOVE App shows you how to integrate our SDK very easily into your existing or future app.

For more information: [MOVE SDK Android Documentation](https://docs.movesdk.com/move-platform/).

### Requirements

You need to provide a `google-services.json` file in the `/app` folder. For more information
check [The Google Services Gradle Plugin](https://developers.google.com/android/guides/google-services-plugin)
page.
Also make sure you have the correct `namespace` for `google-services.json` file. You can find it
in `build.gradle.kts` file in `/app` folder.

To use Google Maps you need to add api key in `local.properties` file:

```kotlin
MAPS_API_KEY = YOUR_API_KEY
```

### Starting Point:
A good entry point is the application class `Move.kt`.
On the first line of the function `onCreate()` the MOVE SDK will be initialized with `MoveSdk.init(..)`

You should also have a look into `MoveSdkManager.kt`. MOVE SDK features and some listeners will be defined with `registerMoveSdkFeatures(..)`.
The configuration of the MOVE SDK will be done within `createMoveConfig(..)`. After registering an new user or login an existing user the
MOVE SDK must be setup. In this example this will be done with `setupMoveSdk(..)`.

The permissions will be handled within the permission feature (see `PermissionsScreen.kt`, `PermissionsViewModel.kt`).
Turning on/off the MOVE SDK will be done within `turnMoveSdkOn()` / `turnMoveSdkOff()`.

For more information: [MOVE SDK Android Getting Started](https://docs.movesdk.com/move-platform/sdk/getting-started/android).

### Configuration

#### Notifications

You can configure notifications by providing notification builders for recognition, trips and
walking.
Check [Notification.Builder documentation](https://developer.android.com/reference/android/app/Notification.Builder)
and `createNotificationBuilder` method in `MoveSdkManagerImpl` class.

```kotlin
moveSdk.apply {
    recognitionNotification(recognitionNotification)
    tripNotification(drivingNotification)
    walkingLocationNotification(walkingNotification)
}
```

#### Listeners

Move SDK allows to track current states using different listeners.

[SDK state listener](https://docs.movesdk.com/move-platform/sdk/models/untitled#sdk-state-listener).
Triggers whenever the `MoveSDKState` changes.

```kotlin
private val sdkStateListener: MoveSdk.StateListener = object : MoveSdk.StateListener {
    override fun onStateChanged(sdk: MoveSdk, state: MoveSdkState) {
        // Your code here
    }
}
moveSdk.sdkStateListener(sdkStateListener)
```

[Authentication state listener](https://docs.movesdk.com/move-platform/sdk/models/untitled#auth-state-update-listener)
. Triggers whenever the `MoveAuthState` changes.

```kotlin
private val authStateListener = object : MoveSdk.AuthStateUpdateListener {
    override fun onAuthStateUpdate(state: MoveAuthState) {
        // Your code here
    }
}

moveSdk.authStateUpdateListener(authStateListener)
```

[Trip state listener](https://docs.movesdk.com/move-platform/sdk/models/untitled#trip-state-listener)
. Triggers whenever the `MoveTripState` changes.

```kotlin
private val tripStateListener = object : MoveSdk.TripStateListener {
    override fun onTripStateChanged(tripState: MoveTripState) {
        // Your code here
    }
}
moveSdk.tripStateListener(tripStateListener)
```

[Initialization listener](https://docs.movesdk.com/move-platform/sdk/models/untitled#initialization-listener)
. Notifies when the configuration could not be verified with the server.

```kotlin
private val initListener: MoveSdk.InitializeListener = object : MoveSdk.InitializeListener {
    override fun onError(error: MoveConfigurationError) {
        // Your code here
    }
}
moveSdk.initializationListener(initListener)
```

[Service warning listener](https://docs.movesdk.com/move-platform/sdk/models/untitled#service-failure-callback)
. Triggers whenever MoveServiceWarnings change.

```kotlin
private val warningListener = object : MoveSdk.MoveWarningListener {
    override fun onMoveWarning(serviceWarnings: List<MoveServiceWarning>) {
        // Your code here
    }
}

moveSdk.setServiceWarningListener(warningListener)
```

[Service error listener](https://docs.movesdk.com/move-platform/sdk/models/untitled#service-warning-callback)
.Triggers whenever MoveServiceFailures change.

```kotlin
private val errorListener = object : MoveSdk.MoveErrorListener {
    override fun onMoveError(serviceFailures: List<MoveServiceFailure>) {
        // Your code here
    }
}

moveSdk.setServiceErrorListener(errorListener)
```

### Setup

To setup Move SDK you need to provide a proper authorization object with project ID, contract ID,
access token and refresh token(see `MoveAuth`) and configuration object with a list of services you
want to use. You can find a list of available services in `MoveDetectionService` enum class.

```kotlin
private fun createMoveConfig(): MoveConfig {
    val moveServices: MutableSet<MoveDetectionService> = mutableSetOf()

    val drivingServices: MutableSet<DrivingService> = mutableSetOf()
    val walkingServices: MutableSet<WalkingService> = mutableSetOf()

    drivingServices.add(DrivingService.DrivingBehaviour)
    drivingServices.add(DrivingService.DistractionFreeDriving)

    walkingServices.add(WalkingService.Location)

    moveServices.add(MoveDetectionService.Driving(drivingServices = drivingServices.toList()))
    moveServices.add(MoveDetectionService.Walking(walkingServices = walkingServices.toList()))
    moveServices.add(MoveDetectionService.Cycling)
    moveServices.add(MoveDetectionService.Places)
    moveServices.add(MoveDetectionService.PublicTransport)
    moveServices.add(MoveDetectionService.PointsOfInterest)
    moveServices.add(MoveDetectionService.AutomaticImpactDetection)
    moveServices.add(MoveDetectionService.AssistanceCall)

    return MoveConfig(
        moveDetectionServices = moveServices.toList(),
    )
}
```

## Support

Contact info@dolph.in

## License

The contents of this repository are licensed under the
[Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).