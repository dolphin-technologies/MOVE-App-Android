package io.dolphin.move.android.features.tripdetails.domain

import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineDetailResponseData
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.tripdetails.data.TripDetailsRepository
import javax.inject.Inject

class GetTripDetailsInteractor @Inject constructor(
    private val tripDetailsRepository: TripDetailsRepository,
) {
    suspend operator fun invoke(tripId: Long): State<ApiMoveTimelineDetailResponseData> {
        return tripDetailsRepository.requestTimeline(tripId)
    }
}