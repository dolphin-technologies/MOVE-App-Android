package io.dolphin.move.android.features.tripdetails.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basedata.network.responses.ApiMoveTimelineItemDetail
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.components.ErrorState
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.State
import io.dolphin.move.android.features.tripdetails.domain.GetTripDetailsInteractor
import io.dolphin.move.android.ui.navigation.Routes
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTripDetailsInteractor: GetTripDetailsInteractor,
) : BaseViewModel(), TripDetailsView {

    private val liveState = MutableLiveData(TripDetailsViewState())
    private var viewState: TripDetailsViewState by liveState.delegate()

    private var tripDetails: ApiMoveTimelineItemDetail? = null
    private var currentTripId: Long? = null

    // we need store tab state to prevent recalculation on tab selection
    private var overviewState: TripDetailsTabState? = null
    private var distractionState: TripDetailsTabState? = null
    private var safenessState: TripDetailsTabState? = null
    private var speedState: TripDetailsTabState? = null

    val tripDetailsViewState = liveState.mapDistinct { it }

    init {
        currentTripId = savedStateHandle.get<Long>(Routes.TimelineRoot.TripDetails.Args.tripId)
        currentTripId?.let(::requestTripDetails)
    }

    override fun onNext() {
        viewState.nextTripId?.let {
            currentTripId = it
            requestTripDetails(it)
        }
    }

    override fun onPrevious() {
        viewState.previousTripId?.let {
            currentTripId = it
            requestTripDetails(it)
        }
    }

    override fun onTabSelected(tab: TripDetailsTabItem) {
        val state: TripDetailsTabState? = when (tab) {
            TripDetailsTabItem.OVERVIEW -> overviewState
            TripDetailsTabItem.DISTRACTION -> distractionState
            TripDetailsTabItem.SAFENESS -> safenessState
            TripDetailsTabItem.SPEED -> speedState
        }
        val selectedTabState = state ?: tripDetails?.mapToSelectedTeb(tab).also {
            when (tab) {
                TripDetailsTabItem.OVERVIEW -> overviewState = it
                TripDetailsTabItem.DISTRACTION -> distractionState = it
                TripDetailsTabItem.SAFENESS -> safenessState = it
                TripDetailsTabItem.SPEED -> speedState = it
            }
        }
        val distractiomMapStateList = if (selectedTabState is TripDetailsTabState.Distraction) {
            selectedTabState.mapToDistractionMapStateList(tripDetails)
        } else {
            emptyList()
        }
        viewState = viewState.copy(
            selectedTeb = selectedTabState,
            mapViewState = viewState.mapViewState?.copy(
                distractionMapList = distractiomMapStateList,
            )
        )
    }

    override fun retryOnError() {
        currentTripId?.let(::requestTripDetails)
    }

    private fun requestTripDetails(tripId: Long) {
        showProgress()
        viewModelScope.launch {
            dropLastStates()
            when (val dataState = getTripDetailsInteractor(tripId)) {
                is State.Data -> {
                    hideProgress()
                    tripDetails = dataState.data.tripDetail
                    val header = dataState.data.tripDetail?.mapToTripDetailsHeader()
                    viewState = viewState.copy(
                        tripDetailsHeader = header,
                        previousTripId = dataState.data.tripDetail?.previousTripId,
                        nextTripId = dataState.data.tripDetail?.nextTripId,
                        selectedTeb = dataState.data.tripDetail?.mapToSelectedTeb(TripDetailsTabItem.OVERVIEW),
                        mapViewState = dataState.data.tripDetail?.mapToMapState(),
                        error = null,
                    )
                }
                is State.Error -> {
                    hideProgress()
                    viewState = TripDetailsViewState(error = ErrorState())
                }
                State.Loading,
                State.None -> { /* do nothing*/
                }
            }
        }
    }

    private fun dropLastStates() {
        overviewState = null
        distractionState = null
        safenessState = null
        speedState = null
    }
}