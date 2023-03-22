package io.dolphin.move.android.features.onboarding.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.onboarding.data.RegistrationRepository
import io.dolphin.move.android.features.onboarding.data.RegistrationRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface RegistrationModule {

    @ViewModelScoped
    @Binds
    fun bindsRegistrationRepository(
        registrationRepository: RegistrationRepositoryImpl
    ): RegistrationRepository
}