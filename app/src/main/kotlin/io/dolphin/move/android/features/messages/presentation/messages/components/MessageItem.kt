package io.dolphin.move.android.features.messages.presentation.messages.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer8
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer8
import io.dolphin.move.android.features.messages.presentation.messages.MessageViewState
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.dusty_teal
import io.dolphin.move.android.ui.theme.orangey_red
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.white

@ExperimentalMaterialApi
@Composable
fun MessageItem(
    state: MessageViewState,
    onItemClick: (Long) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = orangey_red,
                shape = RoundedCornerShape(10.dp),
            ),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.icon_trash_outline),
                tint = white,
                contentDescription = null,
            )
            HorizontalSpacer16()
        }
        Column(
            modifier = Modifier
                .background(
                    color = pale_grey,
                    shape = RoundedCornerShape(10.dp),
                )
                .clickable { onItemClick(state.id) }
                .padding(horizontal = 8.dp, vertical = 16.dp),
        ) {
            Row {
                if (state.isNew) {
                    Canvas(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(8.dp)
                    ) {
                        drawCircle(color = dusty_teal)
                    }
                }
                if (state.isNew) {
                    HorizontalSpacer8()
                } else {
                    HorizontalSpacer16()
                }
                TextBold(
                    modifier = Modifier.weight(1f),
                    text = state.title,
                    color = battleship_grey,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                HorizontalSpacer8()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextNormal(
                        text = state.date,
                        color = dark_indigo,
                        fontSize = 12.sp,
                    )
                    HorizontalSpacer8()
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_forward),
                        contentDescription = null,
                    )
                }
            }
            VerticalSpacer8()
            TextNormal(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = state.description,
                color = battleship_grey,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@ExperimentalMaterialApi
@Composable
fun MessageItemPreview() {
    MessageItem(
        state = MessageViewState(
            id = 0L,
            url = "",
            isNew = true,
            title = "Super, \nSie haben Ihren Bonus erhalten!",
            date = "10 Jun",
            description = "Wussten Sie, dass Sie Ihre Reise unkompliziert und flexibel direkt von Ihrem Smartphone. Wussten Sie, dass Sie Ihre Reise unkompliziert und flexibel direkt von Ihrem Smartphone."
        ),
        onItemClick = {},
    )
}
