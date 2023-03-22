package io.dolphin.move.android.features.timeline.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.features.timeline.presentation.Duration
import io.dolphin.move.android.features.timeline.presentation.TimelineItem
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.score_green
import io.dolphin.move.android.ui.theme.white

@Composable
fun TimelineItem(
    stateList: List<TimelineItem>,
    onTripSelected: (Long) -> Unit,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .drawBehind {
                drawRoundRect(
                    color = pale_grey,
                    cornerRadius = CornerRadius(x = 10.0f, y = 10.0f),
                )
            },
    ) {
        stateList.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = item is TimelineItem.Car) {
                        (item as? TimelineItem.Car)?.id?.let(onTripSelected)
                    }
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 16.dp),
            ) {
                Column(
                    modifier = Modifier.defaultMinSize(minWidth = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    VerticalSpacer16()
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = null,
                    )
                    if (item is TimelineItem.Car) {
                        item.score?.let {
                            VerticalSpacer8()
                            Box(
                                modifier = Modifier.padding(0.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .width(22.dp)
                                        .height(22.dp),
                                    progress = it / 100f,
                                    color = score_green,
                                    strokeWidth = 1.dp
                                )
                                TextNormal(
                                    text = "$it",
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
                HorizontalSpacer16()
                Canvas(modifier = Modifier.fillMaxHeight()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    if (index != 0) {
                        drawLine(
                            start = Offset(x = canvasWidth, y = 0f),
                            end = Offset(x = 0f, y = 20.dp.toPx()),
                            color = dark_indigo,
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                    drawCircle(
                        color = dark_indigo,
                        radius = 4.dp.toPx(),
                        style = Stroke(width = 1.dp.toPx()),
                        center = Offset(x = canvasWidth, y = 24.dp.toPx()),
                    )
                    if (index != stateList.lastIndex) {
                        drawLine(
                            start = Offset(x = canvasWidth, y = 28.dp.toPx()),
                            end = Offset(x = 0f, y = canvasHeight),
                            color = dark_indigo,
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                }
                HorizontalSpacer16()
                Column {
                    VerticalSpacer16()
                    TextBold(
                        text = stringResource(id = item.nameResId),
                        fontSize = 16.sp,
                    )
                    item.timeAndDuration?.let {
                        val minutes = stringResource(id = R.string.txt_min)
                        val time = "${it.start} - ${it.end} (${it.durationMinutes} $minutes)"
                        VerticalSpacer8()
                        TextNormal(
                            text = time,
                            fontSize = 16.sp,
                        )
                    }
                    if (item is TimelineItem.Car) {
                        VerticalSpacer8()
                        item.tripFrom?.let {
                            Row {
                                TextMedium(
                                    text = "${stringResource(id = R.string.txt_from)}: ",
                                    fontSize = 12.sp,
                                )
                                TextNormal(text = it, fontSize = 12.sp)
                            }

                        }
                        item.tripTo?.let {
                            Row {
                                TextMedium(
                                    text = "${stringResource(id = R.string.txt_to_big)}: ",
                                    fontSize = 12.sp,
                                )
                                TextNormal(text = it, fontSize = 12.sp)
                            }
                        }
                    }
                    VerticalSpacer16()
                    Divider(
                        thickness = 1.dp,
                        color = white,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun TimelineItemPreview() {
    val duration = Duration(
        start = "11:00",
        end = "13:00",
        durationMinutes = 75,
    )
    TimelineItem(
        stateList = listOf(
            TimelineItem.Car(
                timeAndDuration = duration,
                tripFrom = "startAddress",
                tripTo = "endAddress",
                score = 49,
                id = 0L,
            )
        ),
        onTripSelected = {},
        scrollState = ScrollState(0),
    )
}

