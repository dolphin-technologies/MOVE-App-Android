package io.dolphin.move.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.dolphin.move.MoveSdk
import io.dolphin.move.android.sdk.MoveSdkManager
import timber.log.Timber
import javax.inject.Inject

/**
 *
 * Within the Application class the MOVE SDK must be initialized at the first line.
 * -> MoveSdk.init(..)
 *
 * After that add notifications, listeners or activate additional features.
 * -> MoveSdkManager.kt
 * --> registerMoveSdkFeatures(..)
 *
 * After receiving the MOVE SDK credentials ...
 * -> LoginRepository.kt / RegisterRepository.kt
 *
 * setup the MOVE SDK with MoveAuth and MoveConfig.
 * -> MoveSdkManager.kt
 * --> setupMoveSdk(..)
 * --> createMoveConfig()
 * --> MoveSdk.setup(..)
 *
 * @see <a href="https://docs.movesdk.com/move-platform/sdk/getting-started/android/quick-start">MOVE SDK Wiki Quick-Start</a>
 * @see <a href="https://docs.movesdk.com/move-platform/sdk/api-interface/android-1/builder">MOVE SDK Wiki Initialization</a>
 */
@HiltAndroidApp
class Move : Application() {

    @Inject
    lateinit var moveSdkManager: MoveSdkManager

    override fun onCreate() {
        val sdk = MoveSdk.init(this) /** MOVE SDK initialization */
        super.onCreate()

        moveSdkManager.registerMoveSdkFeatures(sdk) /** adding MOVE SDK features */

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
