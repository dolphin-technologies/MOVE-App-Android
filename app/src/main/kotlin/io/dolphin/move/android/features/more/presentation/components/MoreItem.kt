package io.dolphin.move.android.features.more.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.light_blue_grey

@Composable
fun MoreItem(
    text: String,
    onClick: () -> Unit,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(48.dp).padding(end = 8.dp)
            ) {
                TextMedium(
                    text = text,
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    modifier = Modifier.scale(0.8f),
                    painter = painterResource(id = R.drawable.icon_arrow_front_colored),
                    contentDescription = null,
                    tint = dark_indigo,
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = light_blue_grey
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoreItemPreview() {
    MoreItem(
        text = "Text of this item",
        onClick = {}
    )
}
