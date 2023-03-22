package io.dolphin.move.android.basepresentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer8
import io.dolphin.move.android.ui.theme.dark_heliotrope_35
import io.dolphin.move.android.ui.theme.dark_peacock_blue_35
import io.dolphin.move.android.ui.theme.dark_robins_egg_35
import io.dolphin.move.android.ui.theme.watermelon
import io.dolphin.move.android.ui.theme.white

@ExperimentalMaterial3Api
@Composable
fun MoveTopBar(
    title: String,
    showBack: Boolean = false,
    showMessages: Boolean = false,
    newMessagesCount: Int,
    onBack: () -> Unit,
    onMessagesClick: () -> Unit,
) = CenterAlignedTopAppBar(
    modifier = Modifier
        .padding(0.dp)
        .background(
            brush = Brush.linearGradient(
                listOf(dark_robins_egg_35, dark_peacock_blue_35, dark_heliotrope_35)
            )
        ),
    title = {
        TextBold(
            text = title,
            fontSize = 18.sp,
            color = white,
        )
    },
    navigationIcon = {
        if (showBack) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onBack()
                    },
                painter = painterResource(id = R.drawable.icon_back_navigation),
                tint = white,
                contentDescription = null,
            )
        }
    },
    windowInsets = WindowInsets(0.dp, 24.dp, 0.dp, 0.dp),
    colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
    ),
    actions = {
        if (showMessages) {
            Box(
                modifier = Modifier
                    .clickable {
                        onMessagesClick()
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.TopEnd,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(top = 2.dp, end = 2.dp)
                        .size(18.dp),
                    painter = painterResource(id = R.drawable.icon_message_full),
                    contentDescription = null,
                    tint = white,
                )
                if (newMessagesCount > 0) {
                    TextNormal(
                        modifier = Modifier
                            .size(12.dp)
                            .drawBehind {
                                drawCircle(
                                    color = watermelon,
                                )
                            },
                        text = if (newMessagesCount > 99) {
                            "99"
                        } else {
                            newMessagesCount.toString()
                        },
                        color = white,
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            HorizontalSpacer8()
        }
    }
)

@ExperimentalMaterial3Api
@Composable
@Preview
fun MoveTopBarPreview() {
    MoveTopBar("MOVE", true, true, 999, {}, {})
}
