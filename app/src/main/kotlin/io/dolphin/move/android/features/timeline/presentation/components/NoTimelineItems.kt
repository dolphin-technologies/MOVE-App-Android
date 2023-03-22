package io.dolphin.move.android.features.timeline.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer72

@Composable
fun NoTimelineItems() {
    Column(
        modifier = Modifier.padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalSpacer72()
        TextMedium(
            text = stringResource(id = R.string.tit_no_data_for_chosen_day),
            fontSize = 14.sp,
        )
        VerticalSpacer16()
        TextNormal(
            text = stringResource(id = R.string.txt_as_soon_as_we_record),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
        )
    }
}