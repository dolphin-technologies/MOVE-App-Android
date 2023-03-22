package io.dolphin.move.android.basedata.local.database.messages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoveMessagesDao {
    @Query("SELECT * FROM messages ORDER BY timestamp")
    fun getMoves(): Flow<List<MoveMessage>>

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun removeMessage(id: Long)

    @Insert(onConflict = REPLACE)
    suspend fun insertMessagesList(itemList: List<MoveMessage>)
}
