package io.dolphin.move.android.pushnotification.domain

import io.dolphin.move.android.pushnotification.data.NotificationsRepository
import javax.inject.Inject

class CheckPushTokenInteractor @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
) {
    operator fun invoke() {
        return notificationsRepository.checkPushToken()
    }
}