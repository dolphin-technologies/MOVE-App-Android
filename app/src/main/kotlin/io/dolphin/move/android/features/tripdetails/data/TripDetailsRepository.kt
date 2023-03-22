package io.dolphin.move.android.features.tripdetails.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.MoveTimelineRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineDetailResponseData
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for trip details
 */
interface TripDetailsRepository {

    /**
     * Request for trip details. On success returns [State.Data] of [ApiMoveTimelineDetailResponseData].
     * On failure returns [State.Error] with error code of result.
     *
     * @param tripId trip id
     * @return [State] of [ApiMoveTimelineDetailResponseData]
     */
    suspend fun requestTimeline(tripId: Long): State<ApiMoveTimelineDetailResponseData>
}

class TripDetailsRepositoryImpl @Inject constructor(
    private val timelineRestApi: MoveTimelineRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val requestHelper: RequestHelper,
) : BaseRepository(), TripDetailsRepository {

    override suspend fun requestTimeline(tripId: Long): State<ApiMoveTimelineDetailResponseData> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                timelineRestApi.apiV1TimelineIdDetailsGet(
                    xAppContractid = requestHelper.getContractId(),
                    id = tripId,
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<ApiMoveTimelineDetailResponseData>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.data != null) {
                        State.Data(response.data)
                    } else {
                        mapToServiceError(null, message = "Empty data")
                    }
                } else {
                    mapToServiceError(
                        code = response?.status?.code,
                        message = response?.status?.message,
                    )
                }
            } else {
                state = rawResponse.mapToError()
            }
            state
        }
    }
}