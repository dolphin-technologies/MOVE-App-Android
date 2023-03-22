package io.dolphin.move.android.pushnotification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class MovePushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: MoveNotificationManager

    override fun onMessageReceived(message: RemoteMessage) {
        notificationManager.handleMessage(message)
    }

    override fun onNewToken(token: String) {
        Timber.v("New token: $token")
        notificationManager.registerNewToken(token)
    }
}