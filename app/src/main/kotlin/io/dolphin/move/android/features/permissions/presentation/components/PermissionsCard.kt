package io.dolphin.move.android.features.permissions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer12
import io.dolphin.move.android.ui.theme.*

@Composable
fun PermissionCard(
    title: String,
    text: String,
    buttonText: String,
    buttonColor: Color,
    onClick: () -> Unit,
) {
    Card (
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color_bg_card)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextBold(
                    text = title.uppercase(),
                    textAlign = TextAlign.Start,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .background(color_bg_card)
                            .height(30.dp)
                            .width(95.dp)
                            .align(Alignment.CenterEnd),
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        TextNormal(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            text = buttonText,
                            color = if (buttonColor == color_permissions_settings) white
                                    else dark_indigo,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            VerticalSpacer12()
            TextNormal(
                text = text,
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
fun PermissionCardPreview() {
    PermissionCard(
        title = "Kind of Permission",
        text = "Description of the permission usage.",
        buttonText = "ButtonText",
        buttonColor = white,
        onClick = {},
    )
}
