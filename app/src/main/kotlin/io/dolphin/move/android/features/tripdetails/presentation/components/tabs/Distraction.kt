package io.dolphin.move.android.features.tripdetails.presentation.components.tabs

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer12
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.features.tripdetails.presentation.DistractionItemState
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsTabState
import io.dolphin.move.android.ui.theme.dusty_teal
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.pale_olive_green
import io.dolphin.move.android.ui.theme.score_green
import io.dolphin.move.android.ui.theme.watermelon
import io.dolphin.move.android.ui.theme.white

private const val PLAY_PAUSE_OFFSET_DP = 4

@Composable
fun Distraction(
    state: TripDetailsTabState.Distraction,
) {
    val imageTouch = ImageBitmap.imageResource(id = R.drawable.icon_touch)
    val imagePhone = ImageBitmap.imageResource(id = R.drawable.icon_phone)
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
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Canvas(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            listOf(pale_olive_green, dusty_teal)
                        ),
                        cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
                        topLeft = Offset(0f, 0f),
                        size = Size(canvasWidth, canvasHeight),
                    )

                    // draw distractions as red rectangles on line
                    state.distractionItems.forEach { item ->
                        val startX = canvasWidth * item.start
                        val endX = canvasWidth * item.end
                        drawLine(
                            start = Offset(x = startX, y = canvasHeight / 2),
                            end = Offset(x = endX, y = canvasHeight / 2),
                            color = watermelon,
                            strokeWidth = 20.dp.toPx(),
                        )
                        val image = when (item) {
                            is DistractionItemState.Phone -> imagePhone
                            is DistractionItemState.Touch -> imageTouch
                        }
                        val topLeftX = (endX - ((endX - startX) / 2)) - (image.width / 2)
                        val topLeftY = -(image.height - 5.dp.toPx())
                        drawImage(
                            image = image,
                            topLeft = Offset(topLeftX, topLeftY)
                        )
                    }

                    // draw trip start play icon
                    drawCircle(
                        color = white,
                        radius = 10.dp.toPx(),
                        style = Stroke(width = 1.5.dp.toPx()),
                        center = Offset(
                            x = 10.dp.toPx(),
                            y = canvasHeight / 2,
                        ),
                    )
                    val offsetPx = PLAY_PAUSE_OFFSET_DP.dp.toPx()
                    drawPath(
                        path = Path().apply {
                            moveTo(offsetPx * 1.5f, offsetPx)
                            lineTo(15.dp.toPx(), canvasHeight / 2)
                            lineTo(offsetPx * 1.5f, canvasHeight - offsetPx)
                            close()
                        },
                        color = white,
                    )

                    // draw trip end stop icon
                    drawCircle(
                        color = white,
                        radius = 10.dp.toPx(),
                        style = Stroke(width = 1.5.dp.toPx()),
                        center = Offset(
                            x = canvasWidth - 10.dp.toPx(),
                            y = canvasHeight / 2,
                        ),
                    )
                    drawRect(
                        color = white,
                        size = Size(8.dp.toPx(), 8.dp.toPx()),
                        topLeft = Offset(canvasWidth - 14.dp.toPx(), 6.dp.toPx())
                    )
                }
                VerticalSpacer8()
                Row {
                    TextMedium(
                        text = "${state.distractionMinutes} ${stringResource(id = R.string.txt_min)} ",
                        fontSize = 10.sp,
                    )
                    TextNormal(
                        text = stringResource(id = R.string.txt_distraction),
                        fontSize = 10.sp,
                    )
                    TextMedium(
                        text = " • ${state.distractionFreeMinutes} ${stringResource(id = R.string.txt_min)} ",
                        fontSize = 10.sp,
                    )
                    TextNormal(
                        text = buildString {
                            append(stringResource(id = R.string.txt_distraction))
                            append(" ")
                            append(stringResource(id = R.string.txt_free))
                        },
                        fontSize = 10.sp,
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
fun DistractionPreview() {
    Distraction(
        state = TripDetailsTabState.Distraction(
            score = 66,
            distractionItems = listOf(
                DistractionItemState.Touch(
                    start = 0.0f,
                    end = 0.084f,
                ),
                DistractionItemState.Phone(
                    start = 0.834f,
                    end = 0.894f,
                ),
            )
        ),
    )
}