package io.dolphin.move.android.features.deleteaccount.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.api.ProfileRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiDeleteAccountRequest
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for delete account operation
 */
interface DeleteAccountRepository {

    /**
     * Request for deleteing the account.
     * On success emits [State.Data] of [Boolean].
     * On failure emits [State.Error] with error code of result.
     *
     * @param password of the user
     * @return [State] of [Boolean]
     */
    suspend fun requestDeleteAccount(password: String): State<Boolean>
}

class DeleteAccountRepositoryImpl @Inject constructor(
    private val profileRestApi: ProfileRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userStorage: UserStorage,
) : BaseRepository(), DeleteAccountRepository {

    override suspend fun requestDeleteAccount(password: String): State<Boolean> {
        return withContext(ioDispatcher) {
            val deleteAccountRequest = ApiDeleteAccountRequest(password = password)
            val rawResponse = try {
                profileRestApi.apiV2UsersDeletePost(
                    apiDeleteAccountRequest = deleteAccountRequest,
                    xAppContractid = userStorage.getUser()?.contractId
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
