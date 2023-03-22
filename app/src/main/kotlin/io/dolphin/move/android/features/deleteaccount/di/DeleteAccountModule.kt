package io.dolphin.move.android.features.deleteaccount.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.deleteaccount.data.DeleteAccountRepository
import io.dolphin.move.android.features.deleteaccount.data.DeleteAccountRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DeleteAccountModule {

    @ViewModelScoped
    @Binds
    fun bindsLoginRepository(
        deleteAccountRepositoryImpl: DeleteAccountRepositoryImpl
    ): DeleteAccountRepository
}
