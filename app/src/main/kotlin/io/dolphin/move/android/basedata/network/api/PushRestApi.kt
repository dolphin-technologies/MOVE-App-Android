package io.dolphin.move.android.basedata.network.api

import io.dolphin.move.android.basedata.network.client.infrastructure.CollectionFormats.*
import io.dolphin.move.android.basedata.network.responses.ApiAddDeviceTokenRequest
import io.dolphin.move.android.basedata.network.responses.ApiBaseResponse
import io.dolphin.move.android.basedata.network.responses.ApiGetMessagesResponse
import io.dolphin.move.android.basedata.network.responses.ApiMessageSettingsResponse
import io.dolphin.move.android.basedata.network.responses.ApiStoreMessageSettingsRequest
import io.dolphin.move.android.basedata.network.responses.ApiUpdateMessagesRequest
import retrofit2.Response
import retrofit2.http.*

interface PushRestApi {
    /**
     *
     * Return the saved push messages for a contract.&lt;li&gt;&lt;b&gt;iconType&lt;/b&gt;: returns the message type to display the corresponding icon. can be on of :&lt;table&gt;&lt;tr&gt;&lt;td&gt;DEFAULT&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;SCORING&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;CHALLENGE&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;JOURNEY&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;OFFER&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;COMMUNITY&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;REWARD&lt;/td&gt;&lt;/tr&gt;
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param xAppContractid  (optional)
     * @param date  (optional)
     * @return [ApiGetMessagesResponse]
     */
    @GET("api/v1/messages")
    suspend fun apiV1MessagesGet(
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null,
        @Header("Date") date: kotlin.String? = null
    ): Response<ApiGetMessagesResponse>

    /**
     *
     * Request to update the status of messages displayed in the message center. &lt;br&gt;&lt;b&gt;vote:&lt;/b&gt; can be of type :&lt;li&gt;&lt;b&gt;like&lt;/b&gt;&lt;/li&gt;&lt;li&gt;&lt;b&gt;dislike&lt;/b&gt;&lt;/li&gt;&lt;li&gt;null if not voted&lt;/li&gt;.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiUpdateMessagesRequest request
     * @param xAppContractid  (optional)
     * @param date  (optional)
     * @return [ApiGetMessagesResponse]
     */
    @PATCH("api/v1/messages")
    suspend fun apiV1MessagesPatch(
        @Body apiUpdateMessagesRequest: ApiUpdateMessagesRequest,
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null,
        @Header("Date") date: kotlin.String? = null
    ): Response<ApiGetMessagesResponse>

    /**
     *
     *
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param xAppContractid  (optional)
     * @param acceptLanguage  (optional)
     * @return [ApiMessageSettingsResponse]
     */
    @GET("api/v1/messages/settings")
    suspend fun apiV1MessagesSettingsGet(
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null,
        @Header("Accept-Language") acceptLanguage: kotlin.String? = null
    ): Response<ApiMessageSettingsResponse>

    /**
     *
     * Request to save/update the status of message types to be sent to the user, atm supported:. &lt;br&gt;&lt;li&gt;&lt;b&gt;goodieReached&lt;/b&gt;&lt;/li&gt;&lt;li&gt;&lt;b&gt;postTripSummary&lt;/b&gt;&lt;/li&gt;&lt;li&gt;&lt;b&gt;weeklySummary&lt;/b&gt;&lt;/li&gt;.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiStoreMessageSettingsRequest request
     * @param xAppContractid  (optional)
     * @return [ApiBaseResponse]
     */
    @PATCH("api/v1/messages/settings")
    suspend fun apiV1MessagesSettingsPatch(
        @Body apiStoreMessageSettingsRequest: ApiStoreMessageSettingsRequest,
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null
    ): Response<ApiBaseResponse>

    /**
     *
     * Request to store the device token for an app installation w/ contractId, needed for fcm/apns push messaging&lt;br&gt;&lt;b&gt;payloadVersion&lt;/b&gt; is used for deciding what push payload can be interpreted correctly by the app version currently used. If not present (null) the old structure is used
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiAddDeviceTokenRequest request
     * @param xAppContractid  (optional)
     * @param xAppAppid  (optional)
     * @param xAppPlatform  (optional)
     * @return [ApiBaseResponse]
     */
    @POST("api/v1/messages/tokens")
    suspend fun apiV1MessagesTokensPost(
        @Body apiAddDeviceTokenRequest: ApiAddDeviceTokenRequest,
        @Header("x-app-contractid") xAppContractid: kotlin.String? = null,
        @Header("x-app-appid") xAppAppid: kotlin.String? = null,
        @Header("x-app-platform") xAppPlatform: kotlin.String? = null
    ): Response<ApiBaseResponse>

}
