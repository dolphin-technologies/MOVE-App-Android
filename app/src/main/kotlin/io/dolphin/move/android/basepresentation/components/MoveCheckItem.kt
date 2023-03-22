package io.dolphin.move.android.basepresentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import io.dolphin.move.android.basepresentation.components.spacers.HorizontalSpacer4
import io.dolphin.move.android.basepresentation.getPolicyString
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.dusty_teal
import io.dolphin.move.android.ui.theme.styleNormal

@Composable
fun MoveCheckItem(
    isChecked: Boolean,
    annotatedString: AnnotatedString,
    onCheckedChange: (Boolean) -> Unit,
    onClick: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = dusty_teal,
                uncheckedColor = battleship_grey,
            ),
        )
        HorizontalSpacer4()
        ClickableText(
            text = annotatedString,
            style = styleNormal(
                color = battleship_grey,
            ),
            onClick = onClick
        )
    }
}

@Preview
@Composable
fun MoveCheckItemPreview() {
    MoveCheckItem(
        isChecked = false,
        annotatedString = getPolicyString(),
        onClick = {},
        onCheckedChange = {},
    )
}
