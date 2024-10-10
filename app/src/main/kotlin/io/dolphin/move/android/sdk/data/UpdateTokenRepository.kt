package io.dolphin.move.android.sdk.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.MoveUserRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiRefreshSdkTokenResponseData
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for refreshing sdk token
 */
interface UpdateTokenRepository {

    /**
     * Request for sdk token updates. On success returns [State.Data] of [ApiRefreshSdkTokenResponseData].
     * On failure returns [State.Error] with error code of result.
     *
     * @return [State] of [ApiRefreshSdkTokenResponseData]
     */
    suspend fun requestUpdateToken(): State<ApiRefreshSdkTokenResponseData>
}

class UpdateTokenRepositoryImpl @Inject constructor(
    private val moveUserRestApi: MoveUserRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val requestHelper: RequestHelper,
    private val userStorage: UserStorage,
) : BaseRepository(), UpdateTokenRepository {

    override suspend fun requestUpdateToken(): State<ApiRefreshSdkTokenResponseData> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                moveUserRestApi.apiV2UsersTokensSdksGet(
                    xAppContractid = requestHelper.getContractId(),
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<ApiRefreshSdkTokenResponseData>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.data != null) {
                        response.data.sdkUserLoginInfo?.let(userStorage::updateSdkAuth)
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