package io.dolphin.move.android.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import io.dolphin.move.android.pushnotification.data.NotificationsRepository
import io.dolphin.move.android.ui.navigation.DEEP_LINK_ROOT
import io.dolphin.move.android.ui.navigation.Routes
import java.util.*
import javax.inject.Inject

interface MoveNotificationManager {
    fun handleMessage(message: RemoteMessage)
    fun registerNewToken(token: String)
}

const val REQUEST_CODE = 1000

@ServiceScoped
class MoveNotificationManagerImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val notificationsRepository: NotificationsRepository,
) : MoveNotificationManager {

    override fun handleMessage(message: RemoteMessage) {
        message.notification?.title
        val intent = Intent(
            Intent.ACTION_VIEW,
            "${DEEP_LINK_ROOT}/${Routes.MessagesRoot.Messages.route}".toUri()
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = TaskStackBuilder.create(appContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val channelId = "default"
        val notificationBuilder = NotificationCompat.Builder(appContext, channelId)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager? =
            appContext.getSystemService(NotificationManager::class.java)

        val channel = NotificationChannel(
            channelId,
            "Move default notification channel",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager?.createNotificationChannel(channel)
        notificationManager?.notify(UUID.randomUUID().clockSequence(), notificationBuilder.build())
    }

    override fun registerNewToken(token: String) {
        notificationsRepository.requestRegisterToken(token)
    }
}