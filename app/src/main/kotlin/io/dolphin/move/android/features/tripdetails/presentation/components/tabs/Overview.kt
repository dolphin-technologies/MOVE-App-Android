package io.dolphin.move.android.features.tripdetails.presentation.components.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer12
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer4
import io.dolphin.move.android.features.tripdetails.presentation.OverviewScore
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.dark_blue_grey
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.score_green

@Composable
fun Overview(overviewList: List<OverviewScore>) {
    Row(
        modifier = Modifier
            .height(144.dp)
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        overviewList.forEachIndexed { index, scoreItem ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = pale_grey,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(start = 4.dp, top = 12.dp, end = 4.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.padding(0.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        progress = { scoreItem.score / 100f },
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp),
                        color = score_green,
                        strokeWidth = 2.dp,
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                    TextNormal(
                        text = "${scoreItem.score}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp, end = 1.dp)
                    )
                }
                VerticalSpacer12()
                TextNormal(
                    text = stringResource(id = scoreItem.title),
                    fontSize = 12.sp,
                    color = dark_blue_grey,
                    maxLines = 1,
                )
                VerticalSpacer4()
                TextNormal(
                    text = stringResource(id = R.string.txt_score).uppercase(),
                    fontSize = 9.sp,
                    color = battleship_grey,
                )
            }
            if (index != overviewList.lastIndex) {
                HorizontalSpacer16()
            }
        }
    }
}

@Composable
@Preview
fun TripDetailsBottomTabsPreview() {
    Overview(
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
    )
}