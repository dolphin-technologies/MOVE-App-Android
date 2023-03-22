package io.dolphin.move.android.features.timeline.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.CommonError
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.features.permissions.presentation.StatusRouter
import io.dolphin.move.android.features.timeline.presentation.components.DatePicker
import io.dolphin.move.android.features.timeline.presentation.components.NoTimelineItems
import io.dolphin.move.android.features.timeline.presentation.components.TimelineButtons
import io.dolphin.move.android.features.timeline.presentation.components.TimelineHeader
import io.dolphin.move.android.features.timeline.presentation.components.TimelineItem
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsRouter
import io.dolphin.move.android.ui.theme.orange
import io.dolphin.move.android.ui.theme.warning_red
import io.dolphin.move.android.ui.theme.white
import java.time.LocalDate

@ExperimentalMaterial3Api
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = hiltViewModel(),
    tripDetailsRouter: TripDetailsRouter,
    statusRouter: StatusRouter,
) {
    val scrollState = rememberScrollState()
    val viewState by viewModel.timelineViewState.observeAsState(TimelineViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)
    val showDateDialog by viewModel.showDateDialog.observeAsState(false)

    if (showProgress) ProgressDialog()

    if (showDateDialog) DatePicker(
        currentDate = viewModel.getCurrentDate(),
        onDateSelected = viewModel::onDaySelected,
    )

    TimelineScreenContent(
        viewState = viewState,
        timelineView = viewModel,
        onTripSelected = tripDetailsRouter::showTimelineScreen,
        showStatus = statusRouter::showStatusScreen,
        scrollState = scrollState,
    )
}

@ExperimentalMaterial3Api
@Composable
fun TimelineScreenContent(
    viewState: TimelineViewState,
    timelineView: TimelineView,
    onTripSelected: (Long) -> Unit,
    showStatus: () -> Unit,
    scrollState: ScrollState,
) {
    Column {
        viewState.timelineHeader?.let {
            TimelineHeader(
                state = it,
                showDatePicker = timelineView::showDatePicker,
                onFilterSelected = timelineView::onFilterSelected,
            )
        }
        if (viewState.moveSdkIsNotRecording) {
            Card(
                modifier = Modifier.padding(all = 16.dp),
                colors = CardDefaults.cardColors(containerColor = orange)
            ) {
                TextNormal(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                        .clickable { showStatus() },
                    text = stringResource(id = R.string.err_permissions),
                    color = white,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        if (viewState.areYouDriving) {
            Card(
                modifier = Modifier.padding(all = 16.dp),
                colors = CardDefaults.cardColors(containerColor = warning_red)
            ) {
                TextNormal(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    text = stringResource(id = R.string.warning_driving),
                    color = white,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        val items = if (
            viewState.filteredItems.isNullOrEmpty() &&
            viewState.timelineHeader?.selectedFilters.isNullOrEmpty()
        ) {
            viewState.timelineItems
        } else {
            viewState.filteredItems
        }
        items?.let {
            TimelineButtons(viewState.nextDayAvailable, timelineView)
            if (it.isEmpty()) {
                NoTimelineItems()
            } else {
                TimelineItem(
                    stateList = it,
                    onTripSelected = onTripSelected,
                    scrollState = scrollState,
                )
            }
        }
        if (viewState.error != null) {
            CommonError(viewState = viewState.error, timelineView::retryOnError)
        }
    }
}

@Preview
@ExperimentalMaterial3Api
@Composable
fun TimelineScreenContentPreview() {
    val duration = Duration(
        start = "11:00",
        end = "13:00",
        durationMinutes = 75,
    )
    TimelineScreenContent(
        viewState = TimelineViewState(
            timelineHeader = TimelineHeaderState(
                dayOfWeak = "Tue",
                dayAndMonth = "20.07.",
                resultList = listOf(
                    TripResults.Car(
                        durationHours = 4,
                        durationMinutes = 45,
                    ),
                    TripResults.Bicycle(
                        durationHours = 0,
                        durationMinutes = 0,
                    ),
                    TripResults.Public(
                        durationHours = 0,
                        durationMinutes = 0,
                    ),
                    TripResults.Walking(
                        durationHours = 7,
                        durationMinutes = 5,
                    ),
                    TripResults.Idle(
                        durationHours = 8,
                        durationMinutes = 51,
                    ),
                )
            ),
            timelineItems = listOf(
                TimelineItem.Car(
                    timeAndDuration = duration,
                    tripFrom = "startAddress",
                    tripTo = "endAddress",
                    score = 49,
                    id = 0L,
                ),
                TimelineItem.Idle(
                    timeAndDuration = duration,
                ),
                TimelineItem.Public(
                    timeAndDuration = duration,
                ),
                TimelineItem.Bicycle(
                    timeAndDuration = duration,
                ),
                TimelineItem.Walking(
                    timeAndDuration = duration,
                ),
            ),
            moveSdkIsNotRecording = true,
            areYouDriving = true,
        ),
        timelineView = TimelineViewAdapter,
        onTripSelected = {},
        showStatus = {},
        scrollState = ScrollState(0),
    )
}

private object TimelineViewAdapter : TimelineView {
    override fun onNext() {}

    override fun onPrevious() {}

    override fun onDaySelected(selectedDate: LocalDate?) {}
    override fun showDatePicker() {}
    override fun onFilterSelected(selectedFilter: TripResults) {}
    override fun retryOnError() {}
}