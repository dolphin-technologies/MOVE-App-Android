package io.dolphin.move.android.features.timeline.domain

import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineResponseData
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.timeline.data.TimelineRepository
import javax.inject.Inject

class GetTimelineInteractor @Inject constructor(
    private val timelineRepository: TimelineRepository,
) {
    suspend fun getTimeline(from: Long, to: Long): State<ApiMoveTimelineResponseData> {
        return timelineRepository.requestTimeline(from, to)
    }
}