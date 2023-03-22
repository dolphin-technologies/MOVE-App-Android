package io.dolphin.move.android.features.login.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer48

@Composable
fun LoginHeaderGroup() {
    Column {
        VerticalSpacer48()
        TextBold(
            text = stringResource(id = R.string.tit_dolphin_move),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
        VerticalSpacer16()
        TextNormal(
            text = stringResource(id = R.string.txt_login_welcometext),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginHeaderGroupPreview() {
    LoginHeaderGroup()
}

