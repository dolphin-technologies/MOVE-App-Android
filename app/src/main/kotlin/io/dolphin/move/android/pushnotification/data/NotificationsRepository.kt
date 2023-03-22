package io.dolphin.move.android.pushnotification.data

import com.google.firebase.messaging.FirebaseMessaging
import io.dolphin.move.android.BuildConfig
import io.dolphin.move.android.basedata.BaseRepository
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.api.PushRestApi
import io.dolphin.move.android.basedata.network.isSuccessfull
import io.dolphin.move.android.basedata.network.responses.ApiAddDeviceTokenRequest
import io.dolphin.move.android.basedata.network.responses.ApiBaseResponse
import io.dolphin.move.android.basedata.storage.IODispatcher
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.pushnotification.TOKEN_TYPE_DEBUG
import io.dolphin.move.android.pushnotification.TOKEN_TYPE_PROD
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Interface for token registration
 */
interface NotificationsRepository {

    /**
     * Request for token registration. On success returns [State.Data] of [ApiBaseResponse].
     * On failure returns [State.Error] with error code of result.
     *
     * @return [State] of [ApiBaseResponse]
     */
    fun requestRegisterToken(token: String)

    /**
     * Allows to check if there is a registered push token.
     */
    fun checkPushToken()
}

class NotificationsRepositoryImpl @Inject constructor(
    private val messagesApi: PushRestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val requestHelper: RequestHelper,
    private val userStorage: UserStorage,
) : BaseRepository(), NotificationsRepository, CoroutineScope {

    private var registrationProcessRunning = false
    override val coroutineContext = SupervisorJob() + ioDispatcher

    override fun requestRegisterToken(token: String) {
        if (userStorage.getUser() == null) return
        if (registrationProcessRunning) return
        launch {
            registrationProcessRunning = true
            val request = ApiAddDeviceTokenRequest(
                deviceToken = token,
                deviceTokenType = if (BuildConfig.DEBUG) TOKEN_TYPE_DEBUG else TOKEN_TYPE_PROD,
                deviceTokenBundle = requestHelper.getPackageName(),
                payloadVersion = 2,
            )
            try {
                val rawResponse = messagesApi.apiV1MessagesTokensPost(
                    xAppContractid = requestHelper.getContractId(),
                    apiAddDeviceTokenRequest = request,
                )
                val response = rawResponse.body()
                if (response?.status.isSuccessfull) {
                    registrationProcessRunning = false
                    userStorage.setPushToken(token)
                } else {
                    // Drop push token on any error
                    registrationProcessRunning = false
                    userStorage.setPushToken(null)
                }
            } catch (e: Exception) {
                // Drop push token on any error
                registrationProcessRunning = false
                userStorage.setPushToken(null)
                Timber.e(e)
            }
        }
    }

    override fun checkPushToken() {
        val pushToken = userStorage.getPushToken()
        Timber.v("Push token: $pushToken")
        if (pushToken == null) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                requestRegisterToken(token)
            }
        }
    }
}