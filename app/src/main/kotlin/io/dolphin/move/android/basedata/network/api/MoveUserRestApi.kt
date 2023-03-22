package io.dolphin.move.android.basedata.network.api

import io.dolphin.move.android.basedata.network.client.infrastructure.CollectionFormats.*
import io.dolphin.move.android.basedata.network.responses.ApiBaseResponse
import io.dolphin.move.android.basedata.network.responses.ApiLoginRequest
import io.dolphin.move.android.basedata.network.responses.ApiLoginResponse
import io.dolphin.move.android.basedata.network.responses.ApiRefreshSdkTokenResponse
import io.dolphin.move.android.basedata.network.responses.ApiRefreshTokenRequest
import io.dolphin.move.android.basedata.network.responses.ApiRefreshTokenResponse
import io.dolphin.move.android.basedata.network.responses.ApiRegisterUserRequest
import io.dolphin.move.android.basedata.network.responses.ApiRegisterUserResponse
import io.dolphin.move.android.basedata.network.responses.ApiRequestResetPasswordRequest
import retrofit2.Response
import retrofit2.http.*

interface MoveUserRestApi {
    /**
     * 
     * Logs the user into the app.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiLoginRequest request
     * @param xAppAppid  (optional)
     * @param date  (optional)
     * @param acceptLanguage  (optional)
     * @param xAppAppversion  (optional)
     * @param xAppPlatform  (optional)
     * @return [ApiLoginResponse]
     */
    @POST("api/v1/users/login")
    suspend fun apiV1UsersLoginPost(
        @Body apiLoginRequest: ApiLoginRequest,
        @Header("x-app-appid") xAppAppid: String? = null,
        @Header("Date") date: String? = null,
        @Header("Accept-Language") acceptLanguage: String? = null,
        @Header("x-app-appversion") xAppAppversion: String? = null,
        @Header("x-app-platform") xAppPlatform: String? = null
    ): Response<ApiLoginResponse>

    /**
     *
     * Start reset password process
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiRequestResetPasswordRequest request
     * @param acceptLanguage  (optional)
     * @return [ApiBaseResponse]
     */
    @POST("api/v1/users/passwords/resets")
    suspend fun apiV1UsersPasswordsResetsPost(
        @Body apiRequestResetPasswordRequest: ApiRequestResetPasswordRequest,
        @Header("Accept-Language") acceptLanguage: String? = null
    ): Response<ApiBaseResponse>

    /**
     * 
     * Registers a new user. Stores the data provided by user, checks if a user with the same email is already registered.
     * Gender can be one of the following: male, female, diverse
     * Consent type can be one of these: tou, privacy
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiRegisterUserRequest request
     * @param xAppAppversion  (optional)
     * @param xAppAppid  (optional)
     * @param acceptLanguage  (optional)
     * @param date  (optional)
     * @param xAppPlatform  (optional)
     * @return [ApiRegisterUserResponse]
     */
    @POST("api/v1/users")
    suspend fun apiV1UsersPost(
        @Body apiRegisterUserRequest: ApiRegisterUserRequest,
        @Header("x-app-appversion") xAppAppversion: String? = null,
        @Header("x-app-appid") xAppAppid: String? = null,
        @Header("Accept-Language") acceptLanguage: String? = null,
        @Header("Date") date: String? = null,
        @Header("x-app-platform") xAppPlatform: String? = null
    ): Response<ApiRegisterUserResponse>

    /**
     * 
     * Used for refreshing the access token.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param apiRefreshTokenRequest request
     * @param xAppAppid  (optional)
     * @param xAppContractid  (optional)
     * @return [ApiRefreshTokenResponse]
     */
    @POST("api/v1/users/tokens/products")
    suspend fun apiV1UsersTokensProductsPost(
        @Body apiRefreshTokenRequest: ApiRefreshTokenRequest,
        @Header("x-app-appid") xAppAppid: String? = null,
        @Header("x-app-contractid") xAppContractid: String? = null
    ): Response<ApiRefreshTokenResponse>

    /**
     * 
     * Used for refreshing the jwt token for the sdk.
     * Responses:
     *  - 200: Request was successful
     *  - 0: An unexpected error occurred while processing the request.
     *
     * @param xAppAppid  (optional)
     * @param xAppContractid  (optional)
     * @return [ApiRefreshSdkTokenResponse]
     */
    @GET("api/v1/users/tokens/sdks")
    suspend fun apiV1UsersTokensSdksGet(
        @Header("x-app-appid") xAppAppid: String? = null,
        @Header("x-app-contractid") xAppContractid: String? = null,
    ): Response<ApiRefreshSdkTokenResponse>

}
