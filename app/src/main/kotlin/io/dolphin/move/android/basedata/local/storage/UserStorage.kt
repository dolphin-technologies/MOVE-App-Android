package io.dolphin.move.android.basedata.local.storage

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import io.dolphin.move.android.basedata.network.responses.ApiContract
import io.dolphin.move.android.basedata.network.responses.ApiGetMessagesResponseData
import io.dolphin.move.android.basedata.network.responses.ApiLogin
import io.dolphin.move.android.basedata.network.responses.ApiProductAuthInfo
import io.dolphin.move.android.basedata.network.responses.ApiSdkUserLoginInfo
import io.dolphin.move.android.basedata.storage.AppMemory
import io.dolphin.move.android.basedata.storage.KEY_PUSH_TOKEN
import io.dolphin.move.android.basedata.storage.KEY_CONTRACT
import io.dolphin.move.android.basedata.storage.KEY_USER
import io.dolphin.move.android.basedata.storage.SecureSharedPreferences
import io.dolphin.move.android.basedata.storage.nullable
import io.dolphin.move.android.basedata.storage.stateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

/**
 * Storage interface to save and retrieve user information
 */
interface UserStorage {
    /**
     * Subscription for [ApiLogin] user login result
     *
     * @return [StateFlow] of [ApiLogin] if there is a value in app memory storage
     */
    fun subscribeUserData(): StateFlow<ApiLogin>

    /**
     * Saves [ApiLogin] user login result to app memory storage
     *
     * @param value the result of user login
     */
    fun setUser(value: ApiLogin?)

    /**
     * Allows get user data from app storage
     *
     * @return [ApiLogin] user data or null if there is no data in app storage
     */
    fun getUser(): ApiLogin?

    /**
     * Allows get user access token from app storage
     *
     * @return [String] access token or null if there is no data in app storage
     */
    fun getAccessToken(): String?

    /**
     * Allows get user refresh token from app storage
     *
     * @return [String] refresh token or null if there is no data in app storage
     */
    fun getRefreshToken(): String?

    /**
     * Updates user authentication information in app storage
     *
     * @param apiProductAuthInfo information about access and refresh tokens
     */
    fun updateAuth(apiProductAuthInfo: ApiProductAuthInfo)

    /**
     * Updates user SDK authentication information in app storage
     *
     * @param sdkUserLoginInfo information about access and refresh tokens
     */
    fun updateSdkAuth(sdkUserLoginInfo: ApiSdkUserLoginInfo)

    /**
     * Subscription for [ApiContract] contract data
     *
     * @return [StateFlow] of [ApiContract] if there is a value in app memory storage
     */
    fun subscribeContractData(): StateFlow<ApiContract>

    /**
     * Saves [ApiContract] contract data result to app memory storage
     *
     * @param value the result of user login
     */
    fun setContract(value: ApiContract?)

    /**
     * Allows get contract data from app storage
     *
     * @return [ApiContract] user data or null if there is no data in app storage
     */
    fun getContract(): ApiContract?

    /**
     * Saves messages in app storage
     *
     * @param messagesResponse
     */
    suspend fun saveMessages(messagesResponse: ApiGetMessagesResponseData)

    /**
     * Allows get user messages from memory storage
     *
     * @return
     */
    fun getUserMessages(): ApiGetMessagesResponseData?

    /**
     * Subscription for unread messages count
     *
     * @return [StateFlow] with count of unread messages
     */
    fun getUnreadMessagesCountLive(): StateFlow<Int>

    /**
     * Allows save push token in storage or drop it
     *
     * @param pushToken push token from FirebaseMessaging
     */
    fun setPushToken(pushToken: String?)

    /**
     * Returns saved push token from storage or drop it
     *
     * @return [String] token or null
     */
    fun getPushToken(): String?
}

class UserInMemoryStorage @Inject constructor(
    appMemory: AppMemory,
    @SecureSharedPreferences private val sharedPreferences: SharedPreferences,
) : UserStorage {

    private val gson = GsonBuilder().create()
    private var userApiLogin: ApiLogin? by appMemory.nullable()
    private var messages: ApiGetMessagesResponseData? by appMemory.nullable()

    private val userDataFlow: MutableStateFlow<ApiLogin> by appMemory.stateFlow(ApiLogin())
    private var userApiContract: ApiContract? by appMemory.nullable()
    private val contractDataFlow: MutableStateFlow<ApiContract> by appMemory.stateFlow(ApiContract())
    private val unreadMessagesCount: MutableStateFlow<Int> = MutableStateFlow(0)

    init {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        try {
            val userObject = gson.fromJson(userJson, ApiLogin::class.java)
            userApiLogin = userObject
            userDataFlow.tryEmit(userObject)
        } catch (e: Exception) {
            Timber.v("No user")
        }
        val contractJson = sharedPreferences.getString(KEY_CONTRACT, null)
        try {
            val contractObject = gson.fromJson(contractJson, ApiContract::class.java)
            userApiContract= contractObject
            contractDataFlow.tryEmit(contractObject)
        } catch (e: Exception) {
            Timber.v("No user")
        }
    }

    override fun subscribeUserData(): StateFlow<ApiLogin> {
        return userDataFlow
    }

    override fun setUser(value: ApiLogin?) {
        userApiLogin = value
        if (value == null) {
            sharedPreferences.edit().remove(KEY_USER).apply()
        } else {
            val userJson = gson.toJson(value)
            sharedPreferences.edit().putString(KEY_USER, userJson).apply()
        }
    }

    override fun getUser(): ApiLogin? {
        return userApiLogin
    }

    override fun getAccessToken(): String? {
        return userApiLogin?.productAuthInfo?.accessToken
    }

    override fun getRefreshToken(): String? {
        return userApiLogin?.productAuthInfo?.refreshToken
    }

    override fun updateAuth(apiProductAuthInfo: ApiProductAuthInfo) {
        userApiLogin = userApiLogin?.copy(
            productAuthInfo = apiProductAuthInfo,
        )
        userApiLogin?.let {
            val userJson = gson.toJson(it)
            sharedPreferences.edit().putString(KEY_USER, userJson).apply()
        }
    }

    override fun updateSdkAuth(sdkUserLoginInfo: ApiSdkUserLoginInfo) {
        userApiLogin = userApiLogin?.copy(
            sdkUserLoginInfo = sdkUserLoginInfo,
        )
        userApiLogin?.let {
            val userJson = gson.toJson(it)
            sharedPreferences.edit().putString(KEY_USER, userJson).apply()
        }
    }

    override fun subscribeContractData(): StateFlow<ApiContract> {
        return contractDataFlow
    }

    override fun setContract(value: ApiContract?) {
        userApiContract = value
        val userJson = gson.toJson(value)
        sharedPreferences.edit().putString(KEY_CONTRACT, userJson).apply()
    }

    override fun getContract(): ApiContract? {
        return userApiContract
    }

    override suspend fun saveMessages(messagesResponse: ApiGetMessagesResponseData) {
        messages = messagesResponse
        unreadMessagesCount.tryEmit(messagesResponse.messages?.count { it.read == false } ?: 0)
    }

    override fun getUserMessages(): ApiGetMessagesResponseData? {
        return messages
    }

    override fun getUnreadMessagesCountLive(): StateFlow<Int> {
        return unreadMessagesCount
    }

    override fun setPushToken(pushToken: String?) {
        if (pushToken != null) {
            sharedPreferences.edit().putString(KEY_PUSH_TOKEN, pushToken).apply()
        } else {
            sharedPreferences.edit().remove(KEY_PUSH_TOKEN).apply()
        }
    }

    override fun getPushToken(): String? {
        return sharedPreferences.getString(KEY_PUSH_TOKEN, null)
    }
}