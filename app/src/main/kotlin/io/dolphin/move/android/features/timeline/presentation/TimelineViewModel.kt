package io.dolphin.move.android.features.timeline.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basedata.network.responses.ApiTimelineItemType
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.components.ErrorState
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.timeline.domain.GetAreYouDrivingInteractor
import io.dolphin.move.android.features.timeline.domain.GetIsNotRecordingSdkInteractor
import io.dolphin.move.android.features.timeline.domain.GetTimelineInteractor
import java.time.LocalDate
import java.time.ZoneOffset.UTC
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val getTimelineInteractor: GetTimelineInteractor,
    private val getIsNotRecordingSdkInteractor: GetIsNotRecordingSdkInteractor,
    private val getAreYouDrivingInteractor: GetAreYouDrivingInteractor,
) : BaseViewModel(), TimelineView {

    private val liveState = MutableLiveData(TimelineViewState())
    private var viewState: TimelineViewState by liveState.delegate()
    private val _showDateDialog = MutableLiveData(false)
    val showDateDialog = _showDateDialog.mapDistinct { it }

    private var currentDate = LocalDate.now()

    val timelineViewState = liveState.mapDistinct { it }

    init {
        getTimelineForCurrentDay()
        observeSdkState()
        observeTripState()
    }

    override fun onNext() {
        currentDate = currentDate.plusDays(1)
        getTimelineForCurrentDay()
    }

    override fun onPrevious() {
        currentDate = currentDate.minusDays(1)
        getTimelineForCurrentDay()
    }

    override fun onDaySelected(selectedDate: LocalDate?) {
        _showDateDialog.postValue(false)
        if (selectedDate == null) return
        currentDate = selectedDate
        getTimelineForCurrentDay()
    }

    override fun showDatePicker() {
        _showDateDialog.postValue(true)
    }

    override fun onFilterSelected(selectedFilter: TripResults) {
        val name = selectedFilter.getName()
        viewState.timelineHeader?.selectedFilters?.toMutableList()?.let { selectedFilters ->
            if (selectedFilters.contains(name)) {
                selectedFilters.remove(name)
            } else {
                selectedFilters.add(name)
            }
            viewState = viewState.copy(
                timelineHeader = viewState.timelineHeader?.copy(
                    selectedFilters = selectedFilters.toList(),
                ),
                filteredItems = if (selectedFilters.isEmpty()) {
                    null
                } else {
                    viewState.timelineItems?.filter {
                        it.getName() in selectedFilters
                    }
                }
            )
        }
    }

    override fun retryOnError() {
        getTimelineForCurrentDay()
    }

    fun getCurrentDate(): LocalDate {
        return currentDate
    }

    private fun getTimelineForCurrentDay() {
        val from: Long = currentDate.atTime(0, 0, 0).toEpochSecond(UTC)
        val to: Long = currentDate.atTime(23, 59, 59).toEpochSecond(UTC)
        getTimeline(from, to)
    }

    private fun getTimeline(from: Long, to: Long) {
        showProgress()
        viewModelScope.launch {
            when (val dataState = getTimelineInteractor.getTimeline(from, to)) {
                is State.Data -> {
                    hideProgress()
                    dataState.data.timelineItemBaseList?.filter {
                        it.type != ApiTimelineItemType.fAKETRIP &&
                                it.type != ApiTimelineItemType.uNKNOWN
                    }?.mapToTimelineItemList()?.let { items ->
                        val todayMillis = LocalDate.now().toEpochDay()
                        val currentDayMillis = currentDate.toEpochDay()
                        val diff = todayMillis - currentDayMillis // difference in days
                        val header = items.mapToHeaderState(currentDate)
                        viewState = viewState.copy(
                            timelineHeader = header,
                            timelineItems = items,
                            filteredItems = emptyList(),
                            nextDayAvailable = diff > 0,
                            error = null,
                        )
                    }
                }
                is State.Error -> {
                    hideProgress()
                    viewState = TimelineViewState(error = ErrorState())
                }
                State.Loading,
                State.None -> { /* do nothing*/
                }
            }
        }
    }

    private fun observeSdkState() {
        getIsNotRecordingSdkInteractor()
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)
            .onEach {
                viewState = viewState.copy(
                    moveSdkIsNotRecording = it,
                )
            }
            .launchIn(viewModelScope)
    }

    private fun observeTripState() {
        getAreYouDrivingInteractor()
            .stateIn(viewModelScope, SharingStarted.Eagerly, false)
            .onEach {
                viewState = viewState.copy(
                    areYouDriving = it,
                )
            }
            .launchIn(viewModelScope)
    }
}