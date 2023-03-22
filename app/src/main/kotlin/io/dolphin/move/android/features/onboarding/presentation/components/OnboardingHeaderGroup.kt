package io.dolphin.move.android.features.onboarding.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer48
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.light_blue_grey

@Composable
fun OnboardingHeaderGroup() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VerticalSpacer48()
        Image(painter = painterResource(id = R.drawable.logo_register), contentDescription = "")
        VerticalSpacer48()
        TextBold(
            text = stringResource(id = R.string.tit_reg),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
        VerticalSpacer16()
        TextNormal(
            text = stringResource(id = R.string.txt_plsreg),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = battleship_grey,
        )
        VerticalSpacer48()
        Divider(
            color = light_blue_grey,
            thickness = 1.dp,
        )
        VerticalSpacer16()
        TextBold(
            text = stringResource(id = R.string.txt_pls_register_or_login),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
        )
    }
}

@Composable
@Preview
fun OnboardingHeaderGroupPreview() {
    OnboardingHeaderGroup()
}