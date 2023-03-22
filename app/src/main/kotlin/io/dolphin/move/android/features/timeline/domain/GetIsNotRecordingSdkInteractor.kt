package io.dolphin.move.android.features.timeline.domain

import io.dolphin.move.MoveSdkState
import io.dolphin.move.android.sdk.MoveSdkManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetIsNotRecordingSdkInteractor @Inject constructor(
    private val sdkManager: MoveSdkManager,
) {
    operator fun invoke(): Flow<Boolean> {
        return sdkManager.fetchMoveStateFlow().map { sdkState ->
            sdkState != MoveSdkState.Running
        }
    }
}