package io.dolphin.move.android.sdk.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dolphin.move.android.sdk.MoveSdkManager
import io.dolphin.move.android.sdk.MoveSdkManagerImpl
import io.dolphin.move.android.sdk.data.UpdateTokenRepository
import io.dolphin.move.android.sdk.data.UpdateTokenRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MoveSdkManagerModule {

    @Binds
    @Singleton
    fun bindUpdateTokenRepository(updateTokenRepository: UpdateTokenRepositoryImpl): UpdateTokenRepository

    @Binds
    @Singleton
    fun bindMoveSdkManager(moveSdkManager: MoveSdkManagerImpl): MoveSdkManager
}
