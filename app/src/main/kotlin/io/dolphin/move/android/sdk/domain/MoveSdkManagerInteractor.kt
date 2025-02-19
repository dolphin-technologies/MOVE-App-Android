package io.dolphin.move.android.sdk.domain

import io.dolphin.move.MoveAssistanceCallStatus
import io.dolphin.move.MoveConfigurationError
import io.dolphin.move.MoveHealthScore
import io.dolphin.move.MoveSdk
import io.dolphin.move.MoveSdkState
import io.dolphin.move.MoveServiceFailure
import io.dolphin.move.MoveServiceWarning
import io.dolphin.move.MoveTripState
import io.dolphin.move.android.sdk.MoveSdkManager
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

class MoveSdkManagerInteractor @Inject constructor(
    private val moveSdkManager: MoveSdkManager,
) {

    fun getMoveSdk(): MoveSdk? {
        return moveSdkManager.getMoveSdk()
    }

    fun fetchTripStateFlow(): StateFlow<MoveTripState> {
        return moveSdkManager.fetchTripStateFlow()
    }

    fun fetchMoveStateFlow(): StateFlow<MoveSdkState> {
        return moveSdkManager.fetchMoveStateFlow()
    }

    fun fetchConfigErrorFlow(): StateFlow<MoveConfigurationError?> {
        return moveSdkManager.fetchConfigErrorFlow()
    }

    fun fetchErrorsFlow(): StateFlow<List<MoveServiceFailure>> {
        return moveSdkManager.fetchErrorsFlow()
    }

    fun fetchWarningsFlow(): StateFlow<List<MoveServiceWarning>> {
        return moveSdkManager.fetchWarningsFlow()
    }

    fun fetchAssistanceStateFlow(): StateFlow<MoveAssistanceCallStatus?> {
        return moveSdkManager.fetchAssistanceStateFlow()
    }

    fun fetchHealthScoreFlow(): StateFlow<MoveHealthScore?> {
        return moveSdkManager.fetchHealthScoreFlow()
    }

    fun setupAndStart() {
        moveSdkManager.setupAndStart()
    }
}