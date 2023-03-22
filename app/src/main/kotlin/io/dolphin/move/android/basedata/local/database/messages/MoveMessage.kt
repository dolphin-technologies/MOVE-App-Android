package io.dolphin.move.android.basedata.local.database.messages

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.dolphin.move.android.basedata.network.responses.ApiMessage

@Entity(tableName = "messages")
data class MoveMessage(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val title: String,
    val description: String,
    val isNew: Boolean,
    val deleted: Boolean,
    val timestamp: Long,
    val timeOffset: Int,
    val url: String,
)

fun ApiMessage.mapToMoveMessage(): MoveMessage {
    return MoveMessage(
        id = id ?: -1L,
        title = text.orEmpty(),
        description = preview.orEmpty(),
        isNew = read?.not() ?: false,
        deleted = deleted ?: false,
        timestamp = sentTime?.toEpochSecond() ?: 0L,
        timeOffset = sentTime?.offset?.totalSeconds ?: 0,
        url = url.orEmpty(),
    )
}