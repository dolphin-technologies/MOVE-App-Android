package io.dolphin.move.android.features.timeline.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.dolphin.move.android.features.timeline.data.TimelineRepository
import io.dolphin.move.android.features.timeline.data.TimelineRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface TimelineModule {
    @Binds
    @ViewModelScoped
    fun bindsTimelineRepository(
        tmelineRepository: TimelineRepositoryImpl
    ): TimelineRepository
}