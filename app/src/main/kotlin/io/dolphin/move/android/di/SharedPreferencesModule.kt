package io.dolphin.move.android.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.dolphin.move.android.basedata.storage.APP_PREFERENCES_NAME
import io.dolphin.move.android.basedata.storage.AppSharePreferences
import io.dolphin.move.android.basedata.storage.SECURE_PREFERENCES_NAME
import io.dolphin.move.android.basedata.storage.SecureSharedPreferences
import io.dolphin.move.android.basedata.storage.USER_PREFERENCES_NAME
import io.dolphin.move.android.basedata.storage.UserSharePreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {

    @UserSharePreferences
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @AppSharePreferences
    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @SecureSharedPreferences
    @Provides
    @Singleton
    fun provideSecureSharePreferences(@ApplicationContext appContext: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(appContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            appContext,
            SECURE_PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}