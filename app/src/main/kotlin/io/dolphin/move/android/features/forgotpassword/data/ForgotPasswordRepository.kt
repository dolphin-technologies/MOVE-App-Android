package io.dolphin.move.android.features.forgotpassword.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.MoveUserRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiStatus
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.forgotpassword.domain.dto.DropPasswordDto
import io.dolphin.move.android.features.forgotpassword.domain.dto.toDropPasswordRequest
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for user forgot password operations
 */
interface ForgotPasswordRepository {

    /**
     * Request for password drop. On success emits [State.Data] of [ApiStatus].
     * On failure emits [State.Error] with error code of result.
     *
     * @param changePasswordDto use drop password information
     * @return [State] of [ApiStatus]
     */
    suspend fun requestDropPassword(changePasswordDto: DropPasswordDto): State<ApiStatus>

}

class ForgotPasswordRepositoryImpl @Inject constructor(
    private val userApi: MoveUserRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val requestHelper: RequestHelper,
) : BaseRepository(),ForgotPasswordRepository {

    override suspend fun requestDropPassword(changePasswordDto: DropPasswordDto): State<ApiStatus> {
        return withContext(ioDispatcher) {
            val request = changePasswordDto.toDropPasswordRequest()
            val rawResponse = try {
                userApi.apiV2UsersPasswordsResetsPost(
                    apiRequestResetPasswordRequest = request,
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<ApiStatus>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.status != null) {
                        State.Data(response.status)
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