package io.dolphin.move.android.features.timeline.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.ErrorState

data class TimelineViewState(
    val timelineHeader: TimelineHeaderState? = null,
    val timelineItems: List<TimelineItem>? = null,
    val filteredItems: List<TimelineItem>? = null,
    val nextDayAvailable: Boolean = false,
    val moveSdkIsNotRecording: Boolean = false,
    val areYouDriving: Boolean = false,
    val error: ErrorState? = null,
)

data class TimelineHeaderState(
    val dayOfWeak: String,
    val dayAndMonth: String,
    val resultList: List<TripResults>,
    val selectedFilters: List<String> = emptyList(),
)

sealed class TripResults {
    abstract val iconRes: Int
    abstract val durationMinutes: Int
    abstract val durationHours: Int

    fun getName(): String {
        return this::class.simpleName.orEmpty()
    }

    data class Car(
        @DrawableRes override val iconRes: Int = R.drawable.icon_car_dark,
        override val durationMinutes: Int,
        override val durationHours: Int,
    ) : TripResults()

    data class Bicycle(
        @DrawableRes override val iconRes: Int = R.drawable.icon_bike_dark,
        override val durationMinutes: Int,
        override val durationHours: Int,
    ) : TripResults()

    data class Public(
        @DrawableRes override val iconRes: Int = R.drawable.icon_public_dark,
        override val durationMinutes: Int,
        override val durationHours: Int,
    ) : TripResults()

    data class Walking(
        @DrawableRes override val iconRes: Int = R.drawable.icon_walk_dark,
        override val durationMinutes: Int,
        override val durationHours: Int,
    ) : TripResults()

    data class Idle(
        @DrawableRes override val iconRes: Int = R.drawable.icon_pause_dark,
        override val durationMinutes: Int,
        override val durationHours: Int,
    ) : TripResults()
}

sealed class TimelineItem {
    open val nameResId: Int = 0
    open val iconRes: Int = 0
    open val timeAndDuration: Duration? = null

    fun getName(): String {
        return this::class.simpleName.orEmpty()
    }

    data class Car(
        @StringRes override val nameResId: Int = R.string.subtit_car,
        @DrawableRes override val iconRes: Int = R.drawable.icon_car_dark,
        override val timeAndDuration: Duration?,
        val tripFrom: String?,
        val tripTo: String?,
        val score: Long?,
        val id: Long?,
    ) : TimelineItem()

    data class Bicycle(
        @StringRes override val nameResId: Int = R.string.subtit_bicycle,
        @DrawableRes override val iconRes: Int = R.drawable.icon_bike_dark,
        override val timeAndDuration: Duration?,
    ) : TimelineItem()

    data class Public(
        @StringRes override val nameResId: Int = R.string.subtit_public_transport,
        @DrawableRes override val iconRes: Int = R.drawable.icon_public_dark,
        override val timeAndDuration: Duration?,
    ) : TimelineItem()

    data class Walking(
        @StringRes override val nameResId: Int = R.string.subtit_walking,
        @DrawableRes override val iconRes: Int = R.drawable.icon_walk_dark,
        override val timeAndDuration: Duration?,
    ) : TimelineItem()

    data class Idle(
        @StringRes override val nameResId: Int = R.string.subtit_idle,
        @DrawableRes override val iconRes: Int = R.drawable.icon_pause_dark,
        override val timeAndDuration: Duration?,
    ) : TimelineItem()

    object Unknown : TimelineItem()
}

data class Duration(
    val start: String?,
    val end: String?,
    val durationMinutes: Long,
)