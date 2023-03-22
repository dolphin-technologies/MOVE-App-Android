package io.dolphin.move.android.features.timeline.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.MoveTimelineRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineResponseData
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for timeline
 */
interface TimelineRepository {
    /**
     * Request for timeline items.On success returns [State.Data] of [ApiMoveTimelineResponseData].
     * On failure returns [State.Error] with error code of result.
     *
     * @param from start time range
     * @param to end time range
     * @return [State] of [ApiMoveTimelineResponseData]
     */
    suspend fun requestTimeline(from: Long, to: Long): State<ApiMoveTimelineResponseData>
}

class TimelineRepositoryImpl @Inject constructor(
    private val timelineRestApi: MoveTimelineRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val requestHelper: RequestHelper,
) : TimelineRepository, BaseRepository() {

    override suspend fun requestTimeline(
        from: Long,
        to: Long,
    ): State<ApiMoveTimelineResponseData> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                timelineRestApi.apiV1TimelineGet(
                    xAppContractid = requestHelper.getContractId(),
                    from = from,
                    to = to,
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<ApiMoveTimelineResponseData>
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