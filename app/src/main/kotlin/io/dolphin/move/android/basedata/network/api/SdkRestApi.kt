package io.dolphin.move.android.basedata.network.api

import io.dolphin.move.android.basedata.network.client.infrastructure.CollectionFormats.*
import io.dolphin.move.android.basedata.network.responses.ApiBaseResponse
import io.dolphin.move.android.basedata.network.responses.ApiConvertTimelineTypeRestRequest
import io.dolphin.move.android.basedata.network.responses.ApiTripRemovalReasonsResponse
import retrofit2.Response
import retrofit2.http.*

interface SdkRestApi {
    /**
     * 
     * 
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param id 
     * @param apiConvertTimelineTypeRestRequest request
     * @param xAppContractid  (optional)
     * @param acceptLanguage  (optional)
     * @return [ApiBaseResponse]
     */
    @POST("api/v1/timeline/items/{id}/converttype")
    suspend fun apiV1TimelineItemsIdConverttypePost(
        @Path("id") id: Long,
        @Body apiConvertTimelineTypeRestRequest: ApiConvertTimelineTypeRestRequest,
        @Header("x-app-contractid") xAppContractid: String? = null,
        @Header("Accept-Language") acceptLanguage: String? = null
    ): Response<ApiBaseResponse>

    /**
     * 
     * 
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param id 
     * @param xAppContractid  (optional)
     * @param acceptLanguage  (optional)
     * @param reasonId  (optional)
     * @return [ApiBaseResponse]
     */
    @DELETE("api/v1/timeline/items/{id}")
    suspend fun apiV1TimelineItemsIdDelete(
        @Path("id") id: Long,
        @Header("x-app-contractid") xAppContractid: String? = null,
        @Header("Accept-Language") acceptLanguage: String? = null,
        @Query("reasonId") reasonId: Long? = null
    ): Response<ApiBaseResponse>

    /**
     * 
     * 
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param acceptLanguage  (optional)
     * @return [ApiTripRemovalReasonsResponse]
     */
    @GET("api/v1/timeline/removereasons")
    suspend fun apiV1TimelineRemovereasonsGet(
        @Header("Accept-Language") acceptLanguage: String? = null
    ): Response<ApiTripRemovalReasonsResponse>

}
