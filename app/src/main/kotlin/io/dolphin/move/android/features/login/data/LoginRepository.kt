package io.dolphin.move.android.features.login.data

import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.api.MoveUserRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiLogin
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.login.domain.dto.LoginUserDto
import io.dolphin.move.android.features.login.domain.dto.toLoginRequest
import io.dolphin.move.android.features.messages.data.MessagesRepository
import io.dolphin.move.android.pushnotification.data.NotificationsRepository
import io.dolphin.move.android.sdk.MoveSdkManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Interface for user login operations
 */
interface LoginRepository {

    /**
     * Request for login. On success emits [State.Data] of [ApiLogin].
     * On failure emits [State.Error] with error code of result.
     *
     * @param loginDto user login information
     * @return [State] of [ApiLogin]
     */
    suspend fun requestLogin(loginDto: LoginUserDto): State<ApiLogin>

    /**
     * Subscription for user data in app memory storage
     *
     * @return [Flow] of [ApiLogin]
     */
    fun getUser(): Flow<ApiLogin>
}

class LoginRepositoryImpl @Inject constructor(
    private val userApi: MoveUserRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userStorage: UserStorage,
    private val moveSdkManager: MoveSdkManager,
    private val messagesRepository: MessagesRepository,
    private val notificationsRepository: NotificationsRepository,
) : BaseRepository(), LoginRepository {

    override suspend fun requestLogin(loginDto: LoginUserDto): State<ApiLogin> {
        return withContext(ioDispatcher) {
            val loginRequest = loginDto.toLoginRequest()
            val rawResponse = try {
                userApi.apiV1UsersLoginPost(
                    apiLoginRequest = loginRequest,
                )
            } catch (e: Exception) {
                return@withContext State.Error(
                    MoveApiError.HttpError(code = -1, responseMessage = e.message)
                )
            }
            val state: State<ApiLogin>
            if (rawResponse.isSuccessful) {
                val response = rawResponse.body()
                state = if (response?.status.isSuccessfull) {
                    if (response?.data != null) {
                        userStorage.setUser(response.data)
                        response.data.sdkUserLoginInfo?.let {
                            /** setup the MOVE SDK after receiving the MOVE SDK credentials */
                            moveSdkManager.setupMoveSdk(
                                projectId = it.productId,
                                userId = it.contractId,
                                accessToken = it.accessToken,
                                refreshToken = it.refreshToken
                            )
                        }
                        messagesRepository.requestMessages()
                        notificationsRepository.checkPushToken()
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

    override fun getUser(): Flow<ApiLogin> {
        return userStorage.subscribeUserData()
    }
}

