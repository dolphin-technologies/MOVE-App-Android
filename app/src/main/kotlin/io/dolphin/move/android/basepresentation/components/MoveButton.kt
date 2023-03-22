package io.dolphin.move.android.basepresentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.dolphin.move.android.ui.theme.ocean_green
import io.dolphin.move.android.ui.theme.pale_olive_green
import io.dolphin.move.android.ui.theme.white

@Composable
fun MoveButton(
    buttonText: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(5.dp),
            )
            .background(
                brush = Brush.verticalGradient(
                    listOf(pale_olive_green, ocean_green)
                ),
            ),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
    ) {
        TextMedium(text = buttonText, color = white)
    }
}

@Preview
@Composable
fun MoveButtonPreview() {
    MoveButton("Button") {}
}