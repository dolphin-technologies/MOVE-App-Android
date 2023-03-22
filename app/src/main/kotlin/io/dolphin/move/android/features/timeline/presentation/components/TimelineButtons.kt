package io.dolphin.move.android.features.timeline.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.features.timeline.presentation.TimelineView
import io.dolphin.move.android.ui.theme.dusty_teal_two

@Composable
fun TimelineButtons(
    nextDayAvailable: Boolean,
    timelineView: TimelineView,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextButton(
                onClick = timelineView::onPrevious,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_arrow_back_colored),
                    contentDescription = null,
                    tint = dusty_teal_two,
                )
                TextMedium(
                    text = stringResource(R.string.txt_previous_day),
                    fontSize = 14.sp,
                    color = dusty_teal_two,
                )
            }
            if (nextDayAvailable) {
                TextButton(
                    onClick = timelineView::onNext,
                ) {
                    TextMedium(
                        text = stringResource(R.string.txt_next_day),
                        fontSize = 14.sp,
                        color = dusty_teal_two,
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_front_colored),
                        contentDescription = null,
                        tint = dusty_teal_two,
                    )
                }
            }
        }
    }
}