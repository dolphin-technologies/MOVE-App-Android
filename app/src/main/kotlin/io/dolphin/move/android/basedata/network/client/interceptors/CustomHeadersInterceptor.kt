package io.dolphin.move.android.basedata.network.client.interceptors

import io.dolphin.move.android.basedata.network.RequestHelper
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
private const val HEADER_X_APP_APPID = "x-app-appid"
private const val HEADER_X_APP_APPVERSION = "x-app-appversion"
private const val HEADER_X_APP_PLATFORM = "x-app-platform"
private const val HEADER_DATE = "Date"

class CustomHeadersInterceptor(
    private val requestHelper: RequestHelper,
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()

        newRequestBuilder.addHeader(
            name = HEADER_ACCEPT_LANGUAGE,
            value = requestHelper.getAcceptLanguage(),
        ).addHeader(
            name = HEADER_X_APP_APPID,
            value = requestHelper.getAppId(),
        ).addHeader(
            name = HEADER_X_APP_APPVERSION,
            value = requestHelper.getAppVersion(),
        ).addHeader(
            name = HEADER_X_APP_PLATFORM,
            value = requestHelper.getPlatform(),
        ).addHeader(
            name = HEADER_DATE,
            value = formatter.format(OffsetDateTime.now()),
        )
        return chain.proceed(newRequestBuilder.build())
    }
}