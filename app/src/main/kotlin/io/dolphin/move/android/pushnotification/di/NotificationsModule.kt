package io.dolphin.move.android.pushnotification.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import io.dolphin.move.android.pushnotification.MoveNotificationManager
import io.dolphin.move.android.pushnotification.MoveNotificationManagerImpl

@Module
@InstallIn(ServiceComponent::class)
interface NotificationsModule {

    @ServiceScoped
    @Binds
    fun bindsNotificationManager(
        notificationManager: MoveNotificationManagerImpl
    ): MoveNotificationManager
}