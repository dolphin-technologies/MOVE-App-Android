package io.dolphin.move.android.basedata.local.database.messages

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MoveMessage::class], version = 1)
abstract class MessagesDatabase : RoomDatabase() {
    abstract fun moveMessagesDao(): MoveMessagesDao
}
