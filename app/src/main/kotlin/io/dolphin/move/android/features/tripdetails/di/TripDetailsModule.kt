package io.dolphin.move.android.features.tripdetails.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.tripdetails.data.TripDetailsRepository
import io.dolphin.move.android.features.tripdetails.data.TripDetailsRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface TripDetailsModule {

    @Binds
    @ViewModelScoped
    fun bindsTripDetailsRepository(
        tripDetailsRepository: TripDetailsRepositoryImpl
    ): TripDetailsRepository
}