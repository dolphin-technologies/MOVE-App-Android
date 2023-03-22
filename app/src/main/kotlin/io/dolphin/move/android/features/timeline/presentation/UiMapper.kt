package io.dolphin.move.android.features.timeline.presentation

import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineItemBase
import io.dolphin.move.android.basedata.network.responses.ApiTimelineItemType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val HH_MM_FORMAT_PATTERN = "HH:mm"
private const val DAY_OF_WEEK_FORMAT_PATTERN = "EEE"
private const val DAY_AND_MONTH_FORMAT_PATTERN = "dd.MM."
private const val DAY_MONTH_YEAR_FORMAT_PATTERN = "dd.MM.\nyy"
private val timeHHMMFormat = DateTimeFormatter.ofPattern(HH_MM_FORMAT_PATTERN)
private val weekFormat = DateTimeFormatter.ofPattern(DAY_OF_WEEK_FORMAT_PATTERN)
private val dayAndMonthFormat = DateTimeFormatter.ofPattern(DAY_AND_MONTH_FORMAT_PATTERN)
private val dayMonthYearFormat = DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_FORMAT_PATTERN)


fun List<ApiMoveTimelineItemBase>.mapToTimelineItemList(): List<TimelineItem> {
    return mapIndexed { index, timelineItem ->
        val startDayOfYear = (timelineItem.startTs?.dayOfYear ?: 0).toLong()
        val endDayOfYear = (timelineItem.endTs?.dayOfYear ?: 0).toLong()
        val resultStartTs = if (index == 0 && startDayOfYear < endDayOfYear) {
            timelineItem.startTs?.plusDays(endDayOfYear - startDayOfYear)?.withHour(0)
                ?.withMinute(0)
        } else {
            timelineItem.startTs
        }
        val resultEndTs = if (index == lastIndex && startDayOfYear < endDayOfYear) {
            timelineItem.endTs?.minusDays(endDayOfYear - startDayOfYear)?.withHour(23)
                ?.withMinute(59)
        } else {
            timelineItem.endTs
        }
        val resultDuration = if (index == lastIndex || index == 0) {
            resultEndTs?.toEpochSecond()?.minus(resultStartTs?.toEpochSecond() ?: 0)?.div(60L)
        } else {
            timelineItem.durationMinutes
        }
        val duration = try {
            Duration(
                start = resultStartTs?.format(timeHHMMFormat),
                end = resultEndTs?.format(timeHHMMFormat),
                durationMinutes = resultDuration ?: 0L,
            )
        } catch (e: Exception) {
            null
        }
        when (timelineItem.type) {

            ApiTimelineItemType.wALKING -> TimelineItem.Walking(
                timeAndDuration = duration,
            )
            ApiTimelineItemType.tRAM,
            ApiTimelineItemType.mETRO,
            ApiTimelineItemType.pUBLICTRANSPORT,
            ApiTimelineItemType.bUS,
            ApiTimelineItemType.tRAIN -> TimelineItem.Public(
                timeAndDuration = duration,
            )
            ApiTimelineItemType.cAR -> {
                TimelineItem.Car(
                    timeAndDuration = duration,
                    tripFrom = timelineItem.startAddress,
                    tripTo = timelineItem.endAddress,
                    score = timelineItem.scores?.total,
                    id = timelineItem.id,
                )
            }
            ApiTimelineItemType.cYCLING -> TimelineItem.Bicycle(
                timeAndDuration = duration,
            )
            ApiTimelineItemType.iDLE -> TimelineItem.Idle(
                timeAndDuration = duration,
            )
            else -> TimelineItem.Unknown
        }
    }
}

fun List<TimelineItem>.mapToHeaderState(date: LocalDate): TimelineHeaderState {
    val currentYear = LocalDate.now().year
    val day = weekFormat.format(date)
    val dayAndMonth = if (currentYear > date.year) {
        dayMonthYearFormat.format(date)
    } else {
        dayAndMonthFormat.format(date)
    }
    val durationCar =
        filterIsInstance<TimelineItem.Car>().sumOf { it.timeAndDuration?.durationMinutes ?: 0 }
    val durationBicycle =
        filterIsInstance<TimelineItem.Bicycle>().sumOf { it.timeAndDuration?.durationMinutes ?: 0 }
    val durationPublic =
        filterIsInstance<TimelineItem.Public>().sumOf { it.timeAndDuration?.durationMinutes ?: 0 }
    val durationWalking =
        filterIsInstance<TimelineItem.Walking>().sumOf { it.timeAndDuration?.durationMinutes ?: 0 }
    val durationIdle =
        filterIsInstance<TimelineItem.Idle>().sumOf { it.timeAndDuration?.durationMinutes ?: 0 }
    return TimelineHeaderState(
        dayOfWeak = day,
        dayAndMonth = dayAndMonth,
        resultList = listOf(
            TripResults.Car(
                durationHours = durationCar.getHours(),
                durationMinutes = durationCar.getMinutes(),
            ),
            TripResults.Bicycle(
                durationHours = durationBicycle.getHours(),
                durationMinutes = durationBicycle.getMinutes(),
            ),
            TripResults.Public(
                durationHours = durationPublic.getHours(),
                durationMinutes = durationPublic.getMinutes(),
            ),
            TripResults.Walking(
                durationHours = durationWalking.getHours(),
                durationMinutes = durationWalking.getMinutes(),
            ),
            TripResults.Idle(
                durationHours = durationIdle.getHours(),
                durationMinutes = durationIdle.getMinutes(),
            ),
        )
    )
}

private fun Long.getMinutes(): Int {
    return mod(60)
}

private fun Long.getHours(): Int {
    return ((this - this.getMinutes()) / 60).toInt()
}