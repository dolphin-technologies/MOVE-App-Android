package io.dolphin.move.android.features.forgotpassword.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.forgotpassword.data.ForgotPasswordRepository
import io.dolphin.move.android.features.forgotpassword.data.ForgotPasswordRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ForgotPasswordModule {
    @ViewModelScoped
    @Binds
    fun bindsForgotPasswordRepository(
        forgotPasswordRepositoryImpl: ForgotPasswordRepositoryImpl
    ): ForgotPasswordRepository
}