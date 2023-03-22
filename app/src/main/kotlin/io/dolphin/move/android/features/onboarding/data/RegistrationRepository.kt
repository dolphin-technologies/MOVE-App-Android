package io.dolphin.move.android.features.onboarding.data

import com.google.firebase.messaging.FirebaseMessaging
import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.MoveApiError
import io.dolphin.move.android.basedata.network.api.MoveUserRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.mapToServiceError
import io.dolphin.move.android.basedata.network.responses.ApiLogin
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.messages.data.MessagesRepository
import io.dolphin.move.android.features.onboarding.domain.dto.UserDto
import io.dolphin.move.android.features.onboarding.domain.dto.toContractRequest
import io.dolphin.move.android.features.onboarding.domain.dto.toUserRequest
import io.dolphin.move.android.pushnotification.data.NotificationsRepository
import io.dolphin.move.android.sdk.MoveSdkManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Interface for user registration operations
 */
interface RegistrationRepository {

    /**
     * Request for registration. On success emits [State.Data] of [ApiLogin].
     * On failure emits [State.Error] with error code of result.
     *
     * @param userDto user registration information
     * @return [State] of [ApiLogin]
     */
    suspend fun registerUser(userDto: UserDto): State<ApiLogin>
}

class RegistrationRepositoryImpl @Inject constructor(
    private val userStorage: UserStorage,
    private val userApi: MoveUserRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val moveSdkManager: MoveSdkManager,
    private val messagesRepository: MessagesRepository,
    private val notificationsRepository: NotificationsRepository,
) : BaseRepository(), RegistrationRepository {

    override suspend fun registerUser(userDto: UserDto): State<ApiLogin> {
        return withContext(ioDispatcher) {
            val userRequest = userDto.toUserRequest()
            val rawResponse = try {
                userApi.apiV1UsersPost(
                    apiRegisterUserRequest = userRequest,
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
                        userStorage.setContract(userDto.toContractRequest())
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
                        registerPushToken()
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

    private fun registerPushToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            notificationsRepository.requestRegisterToken(token)
        }
    }
}