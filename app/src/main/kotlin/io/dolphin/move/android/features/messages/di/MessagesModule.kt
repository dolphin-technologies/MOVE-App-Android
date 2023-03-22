package io.dolphin.move.android.features.messages.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.dolphin.move.android.features.messages.data.MessagesRepository
import io.dolphin.move.android.features.messages.data.MessagesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MessagesModule {

    @Binds
    @Singleton
    fun bindsMessagesRepository(
        messagesRepository: MessagesRepositoryImpl
    ): MessagesRepository
}