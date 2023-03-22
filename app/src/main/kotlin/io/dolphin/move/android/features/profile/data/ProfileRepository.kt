package io.dolphin.move.android.features.profile.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.ProfileRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiContract
import io.dolphin.move.android.basedata.network.responses.ApiLogoutRequest
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.profile.domain.dto.ProfileDto
import io.dolphin.move.android.features.profile.domain.dto.toChangeContractDataRequest
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for user profile operations
 */
interface ProfileRepository {

    suspend fun requestContractData(): State<ApiContract>
    suspend fun requestLogout(): State<Boolean>
    suspend fun requestSave(profileData: ProfileDto): State<ApiContract>
    suspend fun requestEmailChange(profileData: ProfileDto): State<Boolean>
}

class ProfileRepositoryImpl @Inject constructor(
    private val profileRestApi: ProfileRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val requestHelper: RequestHelper,
    private val userStorage: UserStorage,
) : BaseRepository(), ProfileRepository {

    override suspend fun requestContractData(): State<ApiContract> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                val userId = userStorage.getUser()?.contractId ?: ""
                profileRestApi.apiV1UsersGet(
                    xAppContractid = userId,
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<ApiContract>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.data != null) {
                        userStorage.setContract(response.data)
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

    override suspend fun requestLogout(): State<Boolean> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                val userId = userStorage.getUser()?.contractId ?: ""
                profileRestApi.apiV1UsersLogoutPost(
                    apiLogoutRequest = ApiLogoutRequest(userId),
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

    override suspend fun requestSave(profileData: ProfileDto): State<ApiContract> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                val userId = userStorage.getUser()?.contractId ?: ""
                profileRestApi.apiV1UsersPatch(
                    apiChangeContractDataRequest = profileData.toChangeContractDataRequest(),
                    xAppContractid = userId,
                    acceptLanguage = requestHelper.getAcceptLanguage(),
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<ApiContract>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.data != null) {
                        userStorage.setContract(response.data)
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

    override suspend fun requestEmailChange(profileData: ProfileDto): State<Boolean> {
        return withContext(ioDispatcher) {
            val rawResponse = try {
                val userId = userStorage.getUser()?.contractId ?: ""
                profileRestApi.apiV1UsersEmailPut(
                    apiChangeContractDataRequest = profileData.toChangeContractDataRequest(),
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
