package io.dolphin.move.android.features.more.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.ui.theme.color_bg_card

@Composable
fun MoreInfoCard(
    userId: String,
    version: String,
) {
    Card (
        modifier = Modifier
            .padding(vertical = 24.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = color_bg_card)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextNormal(
                text = "${stringResource(id = R.string.txt_user_id)}: $userId",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify,
                fontSize = 12.sp
            )
            VerticalSpacer8()
            TextNormal(
                text = "${stringResource(id = R.string.txt_version)}: $version",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoreCardPreview() {
    MoreInfoCard(
        userId = "100000xyz",
        version = "x.y.z (abc)",
    )
}
