package io.dolphin.move.android.features.tripdetails.presentation

interface TripDetailsView {
    fun onNext()
    fun onPrevious()
    fun onTabSelected(tab: TripDetailsTabItem)
    fun retryOnError()
}