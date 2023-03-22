package io.dolphin.move.android.features.changepassword.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.changepassword.data.ChangePasswordRepository
import io.dolphin.move.android.features.changepassword.data.ChangePasswordRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ChangePasswordModule {

    @ViewModelScoped
    @Binds
    fun bindsChangePasswordRepository(
        ChangePasswordRepositoryImpl: ChangePasswordRepositoryImpl
    ): ChangePasswordRepository
}
