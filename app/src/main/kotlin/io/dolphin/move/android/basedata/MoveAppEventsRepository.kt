package io.dolphin.move.android.basedata

import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.basepresentation.AppEvent
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

interface MoveAppEventsRepository {
    val appEvents: SharedFlow<AppEvent>
    fun offerEvent(appEvent: AppEvent)
}

class MoveAppEventsRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : MoveAppEventsRepository, CoroutineScope {

    override val coroutineContext = ioDispatcher + SupervisorJob()

    private val _appEvents = MutableSharedFlow<AppEvent>()
    override val appEvents: SharedFlow<AppEvent> = _appEvents

    override fun offerEvent(appEvent: AppEvent) {
        launch {
            _appEvents.emit(appEvent)
        }
    }
}
