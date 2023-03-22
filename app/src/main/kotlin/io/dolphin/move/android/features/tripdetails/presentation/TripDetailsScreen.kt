package io.dolphin.move.android.features.tripdetails.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.CommonError
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.features.tripdetails.presentation.components.TripDetailsBottomTabs
import io.dolphin.move.android.features.tripdetails.presentation.components.TripDetailsButtons
import io.dolphin.move.android.features.tripdetails.presentation.components.TripDetailsHeader
import io.dolphin.move.android.features.tripdetails.presentation.components.map.DetailsMapDistraction
import io.dolphin.move.android.features.tripdetails.presentation.components.map.DetailsMapEco
import io.dolphin.move.android.features.tripdetails.presentation.components.map.DetailsMapOverview
import io.dolphin.move.android.features.tripdetails.presentation.components.map.DetailsMapSpeed

@ExperimentalMaterial3Api
@Composable
fun TripDetailsScreen(
    viewModel: TripDetailsViewModel = hiltViewModel(),
) {
    val viewState by viewModel.tripDetailsViewState.observeAsState(TripDetailsViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)

    if (showProgress) ProgressDialog()

    TripDetailsScreenContent(
        viewState = viewState,
        tripDetailsView = viewModel,
    )
}

@ExperimentalMaterial3Api
@Composable
fun TripDetailsScreenContent(
    viewState: TripDetailsViewState,
    tripDetailsView: TripDetailsView,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        viewState.tripDetailsHeader?.let {
            TripDetailsHeader(state = it)
        }
        viewState.selectedTeb?.let { selectedTebState ->
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                TripDetailsButtons(
                    previousDayAvailable = viewState.previousTripId != null,
                    nextDayAvailable = viewState.nextTripId != null,
                    tripDetailsView = tripDetailsView,
                )

                viewState.mapViewState?.let { mapViewState ->
                    val cameraPosition = rememberCameraPositionState()
                    val boundsPadding = with(LocalDensity.current) { 16.dp.toPx().toInt() }
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        properties = mapViewState.mapProperties,
                        cameraPositionState = cameraPosition,
                        onMapLoaded = {
                            mapViewState.polylinePoints.firstOrNull()?.let {
                                cameraPosition.move(
                                    update = CameraUpdateFactory.newLatLngBounds(
                                        mapViewState.latLngBounds,
                                        boundsPadding,
                                    )
                                )
                            }
                        }
                    ) {
                        DetailsMapOverview(mapViewState = mapViewState)
                        if (selectedTebState is TripDetailsTabState.Speed) {
                            DetailsMapSpeed(
                                mapViewState = mapViewState,
                            )
                        }
                        if (selectedTebState is TripDetailsTabState.Safeness) {
                            DetailsMapEco(mapViewState = mapViewState)
                        }
                        if (selectedTebState is TripDetailsTabState.Distraction) {
                            DetailsMapDistraction(mapViewState = mapViewState)
                        }
                    }
                }
                TripDetailsBottomTabs(
                    tabSate = selectedTebState,
                    onTabSelected = { newTab -> tripDetailsView.onTabSelected(newTab) },
                )
            }
        }
        if (viewState.error != null) {
            CommonError(viewState = viewState.error, tripDetailsView::retryOnError)
        }
    }
}

@Preview
@ExperimentalMaterial3Api
@Composable
fun TripDetailsScreenContentPreview() {
    TripDetailsScreenContent(
        viewState = TripDetailsViewState(
            tripDetailsHeader = TripDetailsHeaderState(
                dayOfWeak = "Tue",
                dayAndMonth = "20.07.",
                tripDuration = TripDuration(
                    start = "12:00",
                    end = "16:00",
                    durationMinutes = 45,
                ),
                from = "500 W 2nd St, Austin, TX 78701, USA",
                to = "2600 E 13th St, Austin, TX 78702, USA",
                score = 45,
            ),
            previousTripId = 1L,
            nextTripId = 1L,
            selectedTeb = TripDetailsTabState.Overview(
                overviewList = listOf(
                    OverviewScore(
                        score = 25,
                        title = R.string.txt_total,
                    ),
                    OverviewScore(
                        score = 50,
                        title = R.string.txt_distraction,
                    ),
                    OverviewScore(
                        score = 75,
                        title = R.string.txt_eco_score,
                    ),
                    OverviewScore(
                        score = 100,
                        title = R.string.txt_speed_score,
                    ),
                )
            )
        ),
        tripDetailsView = TripDetailsViewAdapter(),
    )
}

private class TripDetailsViewAdapter : TripDetailsView {
    override fun onNext() {}

    override fun onPrevious() {}

    override fun onTabSelected(tab: TripDetailsTabItem) {}
    override fun retryOnError() {}
}