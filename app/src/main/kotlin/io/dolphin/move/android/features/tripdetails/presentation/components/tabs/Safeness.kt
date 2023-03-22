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
import androidx.compose.material3.Divider
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
import io.dolphin.move.android.features.tripdetails.presentation.SafenessItem
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsTabState
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.score_green
import io.dolphin.move.android.ui.theme.white

@Composable
fun Safeness(
    state: TripDetailsTabState.Safeness,
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
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                state.safenessItems.forEach { safenessItem ->
                    Column {
                        TextMedium(
                            text = stringResource(
                                id = when (safenessItem) {
                                    is SafenessItem.Acceleration -> R.string.txt_eco1
                                    is SafenessItem.Braking -> R.string.txt_braking
                                    is SafenessItem.Cornering -> R.string.txt_cornering
                                },
                            ),
                            fontSize = 12.sp,
                        )
                        VerticalSpacer8()
                        Row {
                            TextNormal(
                                text = stringResource(id = R.string.txt_moderate),
                                fontSize = 12.sp
                            )
                            HorizontalSpacer4()
                            TextMedium(text = "${safenessItem.moderate}", fontSize = 12.sp)
                        }
                        VerticalSpacer8()
                        Row {
                            TextNormal(
                                text = stringResource(id = R.string.txt_strong),
                                fontSize = 12.sp
                            )
                            HorizontalSpacer4()
                            TextMedium(text = "${safenessItem.strong}", fontSize = 12.sp)
                        }
                        VerticalSpacer8()
                        Row {
                            TextNormal(
                                text = stringResource(id = R.string.txt_extreme),
                                fontSize = 12.sp
                            )
                            HorizontalSpacer4()
                            TextMedium(text = "${safenessItem.extreme}", fontSize = 12.sp)
                        }
                    }
                }
            }
            HorizontalSpacer16()
            Divider(
                color = white,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
            )
            HorizontalSpacer12()
            Box(
                modifier = Modifier.padding(0.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp),
                    progress = state.score / 100f,
                    color = score_green,
                    strokeWidth = 2.dp
                )
                TextNormal(
                    text = "${state.score}",
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun SafenessPreview() {
    Safeness(
        state = TripDetailsTabState.Safeness(
            score = 33,
            safenessItems = listOf(
                SafenessItem.Acceleration(
                    moderate = 1,
                    strong = 0,
                    extreme = 4,
                ),
                SafenessItem.Braking(
                    moderate = 1,
                    strong = 0,
                    extreme = 4,
                ),
                SafenessItem.Cornering(
                    moderate = 1,
                    strong = 0,
                    extreme = 4,
                ),
            )
        )
    )
}