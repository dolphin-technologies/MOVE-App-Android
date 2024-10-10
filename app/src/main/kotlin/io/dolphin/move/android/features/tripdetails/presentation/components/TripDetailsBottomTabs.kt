package io.dolphin.move.android.features.tripdetails.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.features.tripdetails.presentation.OverviewScore
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsTabItem
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsTabState
import io.dolphin.move.android.features.tripdetails.presentation.components.tabs.Distraction
import io.dolphin.move.android.features.tripdetails.presentation.components.tabs.Overview
import io.dolphin.move.android.features.tripdetails.presentation.components.tabs.Safeness
import io.dolphin.move.android.features.tripdetails.presentation.components.tabs.Speed
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.dusty_teal_two
import io.dolphin.move.android.ui.theme.styleNormal
import io.dolphin.move.android.ui.theme.white

@Composable
fun TripDetailsBottomTabs(
    tabSate: TripDetailsTabState,
    onTabSelected: (TripDetailsTabItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .offset(y = (-8).dp)
            .background(
                color = white,
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            ),
    ) {
        VerticalSpacer8()
        TabRow(
            modifier = Modifier.height(48.dp),
            selectedTabIndex = tabSate.tabItem.ordinal,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[tabSate.tabItem.ordinal]),
                    color = dusty_teal_two
                )
            }
        ) {
            TripDetailsTabItem.values().forEach { tripDetailsTabItem ->
                Tab(
                    modifier = Modifier.padding(0.dp),
                    selected = tripDetailsTabItem == tabSate.tabItem,
                    onClick = {
                        onTabSelected(tripDetailsTabItem)
                    },
                    text = {
                        Text(
                            text = stringResource(id = tripDetailsTabItem.titleRes),
                            fontSize = 12.sp,
                            color = Color.Unspecified,
                            maxLines = 1,
                            style = styleNormal(),
                            overflow = TextOverflow.Visible,
                        )
                    },
                    selectedContentColor = dusty_teal_two,
                    unselectedContentColor = dark_indigo,
                )
            }
        }
        when (tabSate) {
            is TripDetailsTabState.Distraction -> Distraction(state = tabSate)
            is TripDetailsTabState.Overview -> Overview(tabSate.overviewList)
            is TripDetailsTabState.Safeness -> Safeness(state = tabSate)
            is TripDetailsTabState.Speed -> Speed(state = tabSate)
        }
    }
}

@Composable
@Preview
fun TripDetailsBottomTabsPreview() {
    TripDetailsBottomTabs(
        tabSate = TripDetailsTabState.Overview(
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
                    title = R.string.btn_safeness,
                ),
                OverviewScore(
                    score = 100,
                    title = R.string.txt_speed_score,
                ),
            )
        ),
        onTabSelected = {},
    )
}