package io.dolphin.move.android.features.messages.presentation.messagedetails

import io.dolphin.move.android.basedata.local.database.messages.MoveMessage
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val FULL_DATE_FORMAT_PATTERN = "MMM dd yyyy, HH:mm"
private val fullDateFormat = DateTimeFormatter.ofPattern(FULL_DATE_FORMAT_PATTERN)

fun List<MoveMessage>.mapToMessageStateList(): List<MessageDetailsViewState> {
    return mapIndexed { index, moveMessage ->
        val dateTime = LocalDateTime.ofEpochSecond(
            moveMessage.timestamp,
            0,
            ZoneOffset.ofTotalSeconds(moveMessage.timeOffset),
        )
        MessageDetailsViewState(
            id = moveMessage.id,
            isNew = moveMessage.isNew,
            date = dateTime.format(fullDateFormat),
            title = moveMessage.title,
            contentUrl = moveMessage.url,
            prevIndex = index.takeIf { index > 0 }?.dec(),
            nextIndex = index.takeIf { index < lastIndex }?.inc(),
        )
    }
}