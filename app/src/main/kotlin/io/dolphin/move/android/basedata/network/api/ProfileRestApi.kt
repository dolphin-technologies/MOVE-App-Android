package io.dolphin.move.android.basedata.network.api

import io.dolphin.move.android.basedata.network.client.infrastructure.CollectionFormats.*
import io.dolphin.move.android.basedata.network.responses.ApiBaseResponse
import io.dolphin.move.android.basedata.network.responses.ApiChangeContractDataRequest
import io.dolphin.move.android.basedata.network.responses.ApiChangePasswordRequest
import io.dolphin.move.android.basedata.network.responses.ApiDeleteAccountRequest
import io.dolphin.move.android.basedata.network.responses.ApiGetContractResponse
import io.dolphin.move.android.basedata.network.responses.ApiLogoutRequest
import io.dolphin.move.android.basedata.network.responses.ApiValidateEmailHashRequest
import retrofit2.Response
import retrofit2.http.*

interface ProfileRestApi {
    /**
     * 
     * Deletes the users account on users request. After this call there is no way to restore the account.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiDeleteAccountRequest request
     * @param xAppContractid
     * @return [ApiBaseResponse]
     */
    @POST("api/v2/users/delete")
    suspend fun apiV2UsersDeletePost(
        @Body apiDeleteAccountRequest: ApiDeleteAccountRequest,
        @Header("x-app-contractid") xAppContractid: String? = null
    ): Response<ApiBaseResponse>

    /**
     * 
     * Validates hash for email change
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiValidateEmailHashRequest request
     * @param xAppContractid
     * @return [ApiBaseResponse]
     */
    @PATCH("api/v2/users/email")
    suspend fun apiV2UsersEmailPatch(
        @Body apiValidateEmailHashRequest: ApiValidateEmailHashRequest,
        @Header("x-app-contractid") xAppContractid: String? = null
    ): Response<ApiBaseResponse>

    /**
     * 
     * Starts email change -&gt; finish after new email is verified.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiChangeContractDataRequest request
     * @param xAppContractid
     * @return [ApiBaseResponse]
     */
    @PUT("api/v2/users/email")
    suspend fun apiV2UsersEmailPut(
        @Body apiChangeContractDataRequest: ApiChangeContractDataRequest,
        @Header("x-app-contractid") xAppContractid: String? = null
    ): Response<ApiBaseResponse>

    /**
     * 
     * Change password
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiChangePasswordRequest request
     * @param xAppContractid
     * @return [ApiBaseResponse]
     */
    @PUT("api/v2/users/passwords")
    suspend fun apiV2UsersPasswordsPut(
        @Body apiChangePasswordRequest: ApiChangePasswordRequest,
        @Header("x-app-contractid") xAppContractid: String? = null
    ): Response<ApiBaseResponse>

    /**
     * 
     * Changes user profile information: first name, last name, gender.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiChangeContractDataRequest request
     * @param xAppContractid
     * @param acceptLanguage
     * @return [ApiGetContractResponse]
     */
    @PATCH("api/v2/users")
    suspend fun apiV2UsersPatch(
        @Body apiChangeContractDataRequest: ApiChangeContractDataRequest,
        @Header("x-app-contractid") xAppContractid: String? = null,
        @Header("Accept-Language") acceptLanguage: String? = null
    ): Response<ApiGetContractResponse>

    /**
     *
     * Returns information about users contract. Non-generic (product-specific) attributes are returned in the fields map.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param xAppContractid
     * @return [ApiGetContractResponse]
     */
    @GET("api/v2/users")
    suspend fun apiV2UsersGet(
        @Header("x-app-contractid") xAppContractid: String? = null,
    ): Response<ApiGetContractResponse>

    /**
     *
     * Logs the user out of the app.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiLogoutRequest request
     * @param xAppContractid
     * @return [ApiBaseResponse]
     */
    @POST("api/v2/users/logout")
    suspend fun apiV2UsersLogoutPost(
        @Body apiLogoutRequest: ApiLogoutRequest,
        @Header("x-app-contractid") xAppContractid: String? = null,
    ): Response<ApiBaseResponse>

}
