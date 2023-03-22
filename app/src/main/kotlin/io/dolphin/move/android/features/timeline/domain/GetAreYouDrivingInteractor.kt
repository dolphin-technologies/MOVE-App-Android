package io.dolphin.move.android.features.timeline.domain

import io.dolphin.move.MoveTripState
import io.dolphin.move.android.sdk.MoveSdkManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAreYouDrivingInteractor @Inject constructor(
    private val sdkManager: MoveSdkManager,
) {
    operator fun invoke(): Flow<Boolean> {
        return sdkManager.fetchTripStateFlow().map { tripState ->
            (tripState == MoveTripState.DRIVING) || (tripState == MoveTripState.HALT)
        }
    }
}