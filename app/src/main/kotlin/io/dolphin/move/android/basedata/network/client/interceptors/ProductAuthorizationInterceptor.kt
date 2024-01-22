package io.dolphin.move.android.basedata.network.client.interceptors

import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_AUTHORIZATION = "Authorization"
class ProductAuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()
        newRequestBuilder.addHeader(
            name = HEADER_AUTHORIZATION,
            value = "Bearer "
        )
        return chain.proceed(newRequestBuilder.build())
    }
}
