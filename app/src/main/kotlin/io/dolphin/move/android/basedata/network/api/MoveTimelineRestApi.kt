package io.dolphin.move.android.basedata.network.api

import io.dolphin.move.android.basedata.network.client.infrastructure.CollectionFormats.*
import io.dolphin.move.android.basedata.network.responses.ApiMoveScoringHistoryResponse
import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineDetailResponse
import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineResponse
import io.dolphin.move.android.basedata.network.responses.ApiPeriodType
import retrofit2.Response
import retrofit2.http.*

interface MoveTimelineRestApi {
    /**
     * 
     * Fetch timeline data of specific time range
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param xAppContractid  (optional)
     * @param from  (optional)
     * @param to  (optional)
     * @return [ApiMoveTimelineResponse]
     */
    @GET("api/v1/timeline")
    suspend fun apiV1TimelineGet(@Header("x-app-contractid") xAppContractid: kotlin.String? = null, @Query("from") from: kotlin.Long? = null, @Query("to") to: kotlin.Long? = null): Response<ApiMoveTimelineResponse>

    /**
     *
     * Fetch details of car trip   Color returns how to display it on map View Green: Speed &lt;&#x3D; Speedlimit Yellow Speed &lt;&#x3D; Speedlimit * 1.1 Red: Speed &gt; Speedlmit * 1.1
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param id
     * @param xAppContractid  (optional)
     * @return [ApiMoveTimelineDetailResponse]
     */
    @GET("api/v1/timeline/{id}/details")
    suspend fun apiV1TimelineIdDetailsGet(
        @Path("id") id: kotlin.Long,
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null
    ): Response<ApiMoveTimelineDetailResponse>

    /**
     *
     * Returns scoring history information using calendar-based periods
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param xAppContractid  (optional)
     * @param period  (optional)
     * @param startDate  (optional)
     * @return [ApiMoveScoringHistoryResponse]
     */
    @GET("api/v1/timeline/scoringhistory")
    suspend fun apiV1TimelineScoringhistoryGet(
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null,
        @Query("period") period: ApiPeriodType? = null,
        @Query("startDate") startDate: java.time.LocalDate? = null
    ): Response<ApiMoveScoringHistoryResponse>

    /**
     *
     * Returns scoring history information using rolling back dates
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param xAppContractid  (optional)
     * @param period  (optional)
     * @return [ApiMoveScoringHistoryResponse]
     */
    @GET("api/v1/timeline/scoringhistoryrollingback")
    suspend fun apiV1TimelineScoringhistoryrollingbackGet(
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null,
        @Query("period") period: ApiPeriodType? = null
    ): Response<ApiMoveScoringHistoryResponse>

}
