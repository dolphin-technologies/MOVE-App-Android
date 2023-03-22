package io.dolphin.move.android.features.changepassword.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.api.ProfileRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.changepassword.domain.dto.ChangePasswordDto
import io.dolphin.move.android.features.changepassword.domain.dto.toChangePasswordRequest
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for user profile operations
 */
interface ChangePasswordRepository {

    suspend fun requestChangePassword(changePasswordData: ChangePasswordDto): State<Boolean>
}

class ChangePasswordRepositoryImpl @Inject constructor(
    private val profileRestApi: ProfileRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userStorage: UserStorage,
) : BaseRepository(), ChangePasswordRepository {

    override suspend fun requestChangePassword(changePasswordData: ChangePasswordDto): State<Boolean> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                val userId = userStorage.getUser()?.contractId ?: ""
                profileRestApi.apiV1UsersPasswordsPut(
                    apiChangePasswordRequest = changePasswordData.toChangePasswordRequest(),
                    xAppContractid = userId,
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<Boolean> = if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                if (response?.status.isSuccessfull) {
                    State.Data(true)
                } else {
                    mapToServiceError(
                        code = response?.status?.code,
                        message = response?.status?.message,
                    )
                }
            } else {
                rawResponse.mapToError()
            }
            state
        }
    }
}
