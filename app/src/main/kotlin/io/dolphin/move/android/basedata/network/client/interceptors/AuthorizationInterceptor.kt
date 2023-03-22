package io.dolphin.move.android.basedata.network.client.interceptors

import io.dolphin.move.android.basedata.local.storage.UserStorage
import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_AUTHORIZATION = "Authorization"
private const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
private const val HEADER_X_APP_APPID = "x-app-appid"
private const val HEADER_X_APP_APPVERSION = "x-app-appversion"
private const val HEADER_X_APP_PLATFORM = "x-app-platform"
private const val HEADER_DATE = "Date"

class AuthorizationInterceptor(
    private val userStorage: UserStorage,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()
        val token = userStorage.getAccessToken()
        if (!token.isNullOrEmpty()) {
            newRequestBuilder.addHeader(
                name = HEADER_AUTHORIZATION,
                value = "Bearer $token"
            )
        }
        return chain.proceed(newRequestBuilder.build())
    }
}