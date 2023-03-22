package io.dolphin.move.android.features.profile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.profile.data.ProfileRepository
import io.dolphin.move.android.features.profile.data.ProfileRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ProfileModule {

    @ViewModelScoped
    @Binds
    fun bindsProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository
}
