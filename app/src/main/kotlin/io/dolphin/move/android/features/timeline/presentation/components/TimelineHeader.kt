package io.dolphin.move.android.features.timeline.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer8
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.features.timeline.presentation.TimelineHeaderState
import io.dolphin.move.android.features.timeline.presentation.TripResults
import io.dolphin.move.android.ui.theme.dark_blue_grey
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.white

@Composable
fun TimelineHeader(
    state: TimelineHeaderState,
    showDatePicker: () -> Unit,
    onFilterSelected: (TripResults) -> Unit,
) {
    Row(
        modifier = Modifier
            .background(color = pale_grey)
            .fillMaxWidth()
            .padding(all = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        FilledTonalButton(
            modifier = Modifier.size(56.dp),
            onClick = showDatePicker::invoke,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = dark_blue_grey,
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextNormal(text = state.dayOfWeak, fontSize = 12.sp, color = white)
                TextBold(
                    text = state.dayAndMonth,
                    fontSize = 12.sp, color = white,
                    textAlign = TextAlign.Center,
                )
            }
        }
        state.resultList.forEach { result ->
            val isSelected = state.selectedFilters.contains(result.getName())
            HorizontalSpacer8()
            Column(
                modifier = Modifier
                    .height(56.dp)
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onFilterSelected(result)
                        },
                        role = Role.Checkbox,
                    )
                    .background(
                        color = if (isSelected) {
                            dark_indigo
                        } else {
                            Color.Transparent
                        },
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(id = result.iconRes),
                    contentDescription = null,
                    tint = if (isSelected) {
                        white
                    } else {
                        LocalContentColor.current
                    },
                )
                VerticalSpacer8()
                TextNormal(
                    text = stringResource(
                        R.string.hh_mm,
                        result.durationHours,
                        result.durationMinutes,
                    ),
                    fontSize = 10.sp,
                    color = if (isSelected) white else dark_indigo,
                )
            }
            HorizontalSpacer8()
        }
    }
}

@Preview
@Composable
fun TimelineHeaderPreview() {
    TimelineHeader(
        state = TimelineHeaderState(
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
            ),
            selectedFilters = mutableListOf("Car")
        ),
        showDatePicker = {},
        onFilterSelected = {},
    )
}