package io.dolphin.move.android.features.timeline.presentation

import java.time.LocalDate

interface TimelineView {
    fun onNext()
    fun onPrevious()
    fun onDaySelected(selectedDate: LocalDate?)
    fun showDatePicker()
    fun onFilterSelected(selectedFilter: TripResults)
    fun retryOnError()
}