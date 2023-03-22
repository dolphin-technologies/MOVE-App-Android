package io.dolphin.move.android.features.login.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.login.data.LoginRepository
import io.dolphin.move.android.features.login.data.LoginRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface LoginModule {

    @ViewModelScoped
    @Binds
    fun bindsLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository
}