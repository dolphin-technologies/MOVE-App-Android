package io.dolphin.move.android.domain.usecase

import io.dolphin.move.android.basedata.MoveAppEventsRepository
import io.dolphin.move.android.basepresentation.AppEvent
import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow

class AppEventsInteractor @Inject constructor(
    private val moveAppEventsRepository: MoveAppEventsRepository,
) {
    fun offerEvent(appEvent: AppEvent) {
        moveAppEventsRepository
    }

    fun subscribeAppEvents(): SharedFlow<AppEvent> {
        return moveAppEventsRepository.appEvents
    }
}