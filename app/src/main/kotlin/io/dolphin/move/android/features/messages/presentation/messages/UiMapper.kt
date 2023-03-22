package io.dolphin.move.android.features.messages.presentation.messages

import io.dolphin.move.android.basedata.local.database.messages.MoveMessage
import io.dolphin.move.android.basedata.network.responses.ApiMessage
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val DAY_MONTH_FORMAT_PATTERN = "dd MMM."
private val dayMonthFormat = DateTimeFormatter.ofPattern(DAY_MONTH_FORMAT_PATTERN)

fun ApiMessage.mapToMessageState(): MessageViewState {
    return MessageViewState(
        id = id ?: -1L,
        url = url.orEmpty(),
        isNew = read?.not() ?: false,
        title = text.orEmpty(),
        date = sentTime?.format(dayMonthFormat).orEmpty(),
        description = preview.orEmpty(),
    )
}

fun MoveMessage.mapToMessageState(): MessageViewState {
    val dateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofTotalSeconds(timeOffset))
    return MessageViewState(
        id = id,
        url = url,
        isNew = isNew,
        title = title,
        date = dateTime.format(dayMonthFormat),
        description = description,
    )
}