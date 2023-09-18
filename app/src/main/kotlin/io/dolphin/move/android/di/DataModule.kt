package io.dolphin.move.android.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.dolphin.move.android.basedata.BASE_URL
import io.dolphin.move.android.basedata.MoveAppEventsRepository
import io.dolphin.move.android.basedata.MoveAppEventsRepositoryImpl
import io.dolphin.move.android.basedata.local.storage.UserInMemoryStorage
import io.dolphin.move.android.basedata.local.storage.UserStorage
import io.dolphin.move.android.basedata.network.RequestHelper
import io.dolphin.move.android.basedata.network.RequestHelperImpl
import io.dolphin.move.android.basedata.network.api.*
import io.dolphin.move.android.basedata.network.client.infrastructure.ApiClient
import io.dolphin.move.android.basedata.network.client.interceptors.Authorization
import io.dolphin.move.android.basedata.network.client.interceptors.AuthorizationInterceptor
import io.dolphin.move.android.basedata.network.client.interceptors.CustomHeadersInterceptor
import io.dolphin.move.android.basedata.network.client.interceptors.ProductAuthorizationInterceptor
import io.dolphin.move.android.basedata.storage.AppMemory
import io.dolphin.move.android.basedata.storage.SessionMemory
import io.dolphin.move.android.basedata.storage.common.WebAgreementInMemoryStorage
import io.dolphin.move.android.basedata.storage.common.WebAgreementStorage
import io.dolphin.move.android.pushnotification.data.NotificationsRepository
import io.dolphin.move.android.pushnotification.data.NotificationsRepositoryImpl
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsMoveAppEventsRepository(
        moveAppEventsRepository: MoveAppEventsRepositoryImpl
    ): MoveAppEventsRepository

    @Singleton
    @Binds
    fun bindsNotificationsRepository(
        notificationsRepository: NotificationsRepositoryImpl
    ): NotificationsRepository

    @Singleton
    @Binds
    fun bindsWebAgreementStorage(
        webAgreementStorage: WebAgreementInMemoryStorage
    ): WebAgreementStorage

    @Singleton
    @Binds
    fun bindsUserStorage(
        userInMemoryStorage: UserInMemoryStorage
    ): UserStorage

    companion object {

        @Singleton
        @Provides
        fun provideAppMemory(): AppMemory {
            return AppMemory()
        }

        @Singleton
        @Provides
        fun provideSessionMemory(): SessionMemory {
            return SessionMemory()
        }

        @Singleton
        @Provides
        fun provideRequestHelper(
            @ApplicationContext appContext: Context,
            userStorage: UserStorage,
        ): RequestHelper {
            val contentResolver = appContext.contentResolver
            val packageManager = appContext.packageManager
            val packageName = appContext.packageName
            val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            val appVersion = "${packageName}_${pInfo.versionName}"

            val locale = Locale.getDefault()
            val country = locale.country
            val language = locale.language
            val languageCode = "$language-$country"

            @SuppressLint("HardwareIds")
            val appId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            return RequestHelperImpl(userStorage, appVersion, packageName, appId, languageCode)
        }

        @Singleton
        @Provides
        fun provideMoveUserRestApi(
            requestHelper: RequestHelper,
        ): MoveUserRestApi {
            val productAuthorization = ProductAuthorizationInterceptor()
            return ApiClient(
                baseUrl = BASE_URL,
                okHttpClientBuilder = OkHttpClient()
                    .newBuilder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply { level = HttpLoggingInterceptor.Level.BODY }
                    )
                    .addInterceptor(productAuthorization)
                    .addInterceptor(CustomHeadersInterceptor(requestHelper))
            )
                .createService(MoveUserRestApi::class.java)
        }

        @Singleton
        @Provides
        fun provideAuthenticator(
            moveUserRestApi: MoveUserRestApi,
            requestHelper: RequestHelper,
            userStorage: UserStorage,
            appEventsRepository: MoveAppEventsRepository,
        ): Authenticator {
            return Authorization(moveUserRestApi, requestHelper, userStorage, appEventsRepository)
        }

        @Singleton
        @Provides
        fun provideSdkRestApi(
            userStorage: UserStorage,
            requestHelper: RequestHelper,
            authorization: Authorization,
        ): SdkRestApi {
            val authInterceptor = AuthorizationInterceptor(userStorage)
            val headersInterceptor = CustomHeadersInterceptor(requestHelper)
            return ApiClient(
                baseUrl = BASE_URL,
                okHttpClientBuilder = OkHttpClient()
                    .newBuilder()
                    .authenticator(authorization)
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply { level = HttpLoggingInterceptor.Level.BODY }
                    )
                    .addInterceptor(headersInterceptor)
                    .addInterceptor(authInterceptor)
            )
                .createService(SdkRestApi::class.java)
        }

        @Singleton
        @Provides
        fun provideTimelineRestApi(
            userStorage: UserStorage,
            requestHelper: RequestHelper,
            authorization: Authorization,
        ): MoveTimelineRestApi {
            val authInterceptor = AuthorizationInterceptor(userStorage)
            val headersInterceptor = CustomHeadersInterceptor(requestHelper)

            return ApiClient(
                baseUrl = BASE_URL,
                okHttpClientBuilder = OkHttpClient()
                    .newBuilder()
                    .authenticator(authorization)
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply { level = HttpLoggingInterceptor.Level.BODY }
                    )
                    .addInterceptor(headersInterceptor)
                    .addInterceptor(authInterceptor)
            )
                .createService(MoveTimelineRestApi::class.java)
        }

        @Singleton
        @Provides
        fun providePushRestApi(
            userStorage: UserStorage,
            requestHelper: RequestHelper,
            authorization: Authorization,
        ): PushRestApi {
            val authInterceptor = AuthorizationInterceptor(userStorage)
            val headersInterceptor = CustomHeadersInterceptor(requestHelper)

            return ApiClient(
                baseUrl = BASE_URL,
                okHttpClientBuilder = OkHttpClient()
                    .newBuilder()
                    .authenticator(authorization)
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply { level = HttpLoggingInterceptor.Level.BODY }
                    )
                    .addInterceptor(headersInterceptor)
                    .addInterceptor(authInterceptor)
            )
                .createService(PushRestApi::class.java)
        }


        @Singleton
        @Provides
        fun provideProfileRestApi(
            userStorage: UserStorage,
            requestHelper: RequestHelper,
            authorization: Authorization,
        ): ProfileRestApi {
            val authInterceptor = AuthorizationInterceptor(userStorage)
            val headersInterceptor = CustomHeadersInterceptor(requestHelper)
            return ApiClient(
                baseUrl = BASE_URL,
                okHttpClientBuilder = OkHttpClient()
                    .newBuilder()
                    .authenticator(authorization)
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .apply { level = HttpLoggingInterceptor.Level.BODY }
                    )
                    .addInterceptor(headersInterceptor)
                    .addInterceptor(authInterceptor)
            )
                .createService(ProfileRestApi::class.java)
        }
    }
}
