package io.dolphin.move.android.features.tripdetails.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsHeaderState
import io.dolphin.move.android.features.tripdetails.presentation.TripDuration
import io.dolphin.move.android.ui.theme.dark_blue_grey
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.score_green
import io.dolphin.move.android.ui.theme.white

@Composable
fun TripDetailsHeader(
    state: TripDetailsHeaderState,
) {
    Row(
        modifier = Modifier
            .background(color = pale_grey)
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilledTonalButton(
            onClick = {},
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = dark_blue_grey,
            ),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextNormal(text = state.dayOfWeak, fontSize = 12.sp, color = white)
                TextBold(
                    text = state.dayAndMonth,
                    fontSize = 14.sp, color = white,
                    textAlign = TextAlign.Center,
                )
            }
        }
        HorizontalSpacer16()
        Column(
            modifier = Modifier.weight(1f)
        ) {
            state.tripDuration?.let {
                val minutes = stringResource(id = R.string.txt_min)
                val time = "${it.start} - ${it.end} (${it.durationMinutes} $minutes)"
                VerticalSpacer8()
                TextNormal(
                    text = time,
                    fontSize = 10.sp,
                )
            }
            VerticalSpacer8()
            state.from?.let {
                Row {
                    TextMedium(
                        text = "${stringResource(id = R.string.txt_from)}: ",
                        fontSize = 10.sp,
                    )
                    TextNormal(text = it, fontSize = 10.sp)
                }

            }
            state.to?.let {
                Row {
                    TextMedium(
                        text = "${stringResource(id = R.string.txt_to_big)}: ",
                        fontSize = 10.sp,
                    )
                    TextNormal(text = it, fontSize = 10.sp)
                }
            }
        }
        state.score?.let { score ->
            HorizontalSpacer16()
            Box(
                modifier = Modifier.padding(0.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp),
                    progress = score / 100f,
                    color = score_green,
                    strokeWidth = 1.dp
                )
                TextNormal(
                    text = "$score",
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun TripDetailsHeaderPreview() {
    TripDetailsHeader(
        TripDetailsHeaderState(
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
    )
}