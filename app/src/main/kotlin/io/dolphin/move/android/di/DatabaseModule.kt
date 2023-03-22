package io.dolphin.move.android.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.dolphin.move.android.basedata.local.database.messages.MessagesDatabase
import io.dolphin.move.android.basedata.local.database.messages.MoveMessagesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideMoveMessagesDao(appDatabase: MessagesDatabase): MoveMessagesDao {
        return appDatabase.moveMessagesDao()
    }

    @Provides
    @Singleton
    fun provideMessagesDatabase(@ApplicationContext appContext: Context): MessagesDatabase {
        return Room.databaseBuilder(
            appContext,
            MessagesDatabase::class.java,
            "MoveMessagesDB"
        ).build()
    }
}