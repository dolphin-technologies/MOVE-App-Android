package io.dolphin.move.android.basedata.network.client.interceptors

import io.dolphin.move.android.BuildConfig
import io.dolphin.move.android.basedata.MoveAppEventsRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.MoveUserRestApi
import io.dolphin.move.android.basedata.network.client.infrastructure.getErrorResponse
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiBaseResponse
import io.dolphin.move.android.basedata.network.responses.ApiRefreshTokenRequest
import io.dolphin.move.android.basepresentation.AppEvent
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

private const val HEADER_AUTHORIZATION = "Authorization"

class Authorization @Inject constructor(
    private val moveUserRestApi: MoveUserRestApi,
    private val requestHelper: RequestHelper,
    private val userStorage: UserStorage,
    private val moveAppEventsRepository: MoveAppEventsRepository,
) : Authenticator {
    private val Response.responseCount: Int
        get() = generateSequence(this) { it.priorResponse }.count()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.responseCount >= 2) {
            return null // If we've failed 2 times, give up.
        }
        val apiRefreshTokenRequest = ApiRefreshTokenRequest(
            timestamp = System.currentTimeMillis(),
            appId = requestHelper.getAppId(),
            clientVersion = BuildConfig.VERSION_NAME,
            platform = requestHelper.getPlatform(),
            language = requestHelper.getAcceptLanguage(),
            isoTime = requestHelper.getCurrentTime(),
            contractId = requestHelper.getContractId(),
            refreshToken = requestHelper.getRefreshToken(),
        )

        val result = runBlocking {
            moveUserRestApi.apiV1UsersTokensProductsPost(
                apiRefreshTokenRequest = apiRefreshTokenRequest,
                xAppAppid = requestHelper.getAppId(),
                xAppContractid = requestHelper.getContractId()
            )
        }
        if (result.isSuccessful) {
            result.body()?.data?.productAuthInfo?.let(userStorage::updateAuth)
        } else {
            try {
                val errorBody = result.getErrorResponse<ApiBaseResponse>()
                val state = mapToServiceError(
                    code = errorBody?.status?.code,
                    message = errorBody?.status?.message,
                )
                if (state.error is MoveApiError.ServiceError.RefreshTokenForAnotherUser) {
                    userStorage.setUser(null)
                    moveAppEventsRepository.offerEvent(AppEvent.Logout)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        return response.request.newBuilder()
            .header(HEADER_AUTHORIZATION, "Bearer ${userStorage.getAccessToken()}")
            .build()
    }
}