package io.dolphin.move.android.basepresentation.components

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
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer32
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer72

@Composable
fun CommonError(
    viewState: ErrorState,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalSpacer72()
        TextMedium(
            text = stringResource(id = viewState.titleId),
            fontSize = 14.sp,
        )
        VerticalSpacer16()
        TextNormal(
            text = stringResource(id = viewState.description),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
        )
        VerticalSpacer32()
        MoveButton(buttonText = stringResource(id = R.string.retry)) {
            onRetry()
        }
    }
}