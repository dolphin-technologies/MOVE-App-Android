package io.dolphin.move.android.basedata.network

import io.dolphin.move.android.basedata.local.storage.UserStorage
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

private const val OS_LABEL = "Android"


interface RequestHelper {
    fun getAppVersion(): String
    fun getPackageName(): String
    fun getAppId(): String
    fun getAcceptLanguage(): String
    fun getUUID(): String
    fun getPlatform(): String
    fun getCurrentTime(): OffsetDateTime

    /**
     * String date-time with an offset, such as '2011-12-03T10:15:30+01:00'.
     *
     * @return current [String] date-time with an offset
     */
    fun getCurrentStringTime(): String
    fun getContractId(): String?

    fun getRefreshToken(): String?
}

class RequestHelperImpl @Inject constructor(
    private val userStorage: UserStorage,
    private val appVersion: String,
    private val packageName: String,
    private val appId: String,
    private val languageCode: String,
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
) : RequestHelper {

    override fun getAppVersion(): String {
        return appVersion
    }

    override fun getPackageName(): String {
        return packageName
    }

    override fun getAppId(): String {
        return appId
    }

    override fun getAcceptLanguage(): String {
        return languageCode
    }

    override fun getUUID(): String {
        return UUID.randomUUID().toString()
    }

    override fun getPlatform(): String {
        return OS_LABEL
    }

    override fun getCurrentTime(): OffsetDateTime {
        return OffsetDateTime.now()
    }

    override fun getCurrentStringTime(): String {
        return formatter.format(OffsetDateTime.now())
    }

    override fun getContractId(): String? {
        return userStorage.getUser()?.contractId
    }

    override fun getRefreshToken(): String? {
        return userStorage.getRefreshToken()
    }
}