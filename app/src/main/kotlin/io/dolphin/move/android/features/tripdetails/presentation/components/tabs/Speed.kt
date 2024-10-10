package io.dolphin.move.android.features.tripdetails.presentation.components.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer12
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer4
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsTabState
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.score_green
import io.dolphin.move.android.ui.theme.white

@Composable
fun Speed(
    state: TripDetailsTabState.Speed,
) {
    Box(
        modifier = Modifier
            .height(144.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = pale_grey,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row {
                    TextMedium(
                        text = stringResource(id = R.string.txt_within_limits),
                        fontSize = 12.sp,
                    )
                    HorizontalSpacer4()
                    TextNormal(
                        text = stringResource(id = R.string.km, state.withinLimits),
                        fontSize = 12.sp,
                    )
                }
                VerticalSpacer8()
                Row {
                    TextMedium(
                        text = stringResource(id = R.string.less_10_percents),
                        fontSize = 12.sp,
                    )
                    HorizontalSpacer4()
                    TextNormal(
                        text = stringResource(id = R.string.km, state.less10Percents),
                        fontSize = 12.sp,
                    )
                }
                VerticalSpacer8()
                Row {
                    TextMedium(
                        text = stringResource(id = R.string.over_10_percents),
                        fontSize = 12.sp,
                    )
                    HorizontalSpacer4()
                    TextNormal(
                        text = stringResource(id = R.string.km, state.over10Percents),
                        fontSize = 12.sp,
                    )
                }
            }
            HorizontalSpacer16()
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = white
            )
            HorizontalSpacer12()
            Box(
                modifier = Modifier.padding(0.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    progress = { state.score / 100f },
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    color = score_green,
                    strokeWidth = 2.dp,
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                )
                TextNormal(
                    text = "${state.score}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp, end = 1.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeedPreview() {
    Speed(
        state = TripDetailsTabState.Speed(
            score = 78,
            withinLimits = "3.8",
            less10Percents = "0.0",
            over10Percents = "0.8",
        ),
    )
}