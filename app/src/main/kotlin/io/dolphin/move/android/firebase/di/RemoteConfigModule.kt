package io.dolphin.move.android.firebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dolphin.move.android.firebase.data.RemoteConfigRepository
import io.dolphin.move.android.firebase.data.RemoteConfigRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteConfigModule {

    @Provides
    @Singleton
    fun bindRemoteConfig(remoteConfigRepository: RemoteConfigRepositoryImpl): RemoteConfigRepository {
        remoteConfigRepository.initRemoteConfigs()
        return RemoteConfigRepositoryImpl()
    }
}