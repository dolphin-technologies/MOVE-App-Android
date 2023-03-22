package io.dolphin.move.android.basepresentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.ui.theme.black
import io.dolphin.move.android.ui.theme.white

@Preview(showBackground = true)
@Composable
fun SimpleOkDialogPreview() {
    SimpleOkDialog(
        title = "Dialog Title",
        description = "This is a simple Dialog!",
    ) {
    }
}

@Composable
fun SimpleOkDialog(
    title: String?,
    description: String?,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = white,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = black,
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (title != null) {
                    TextBold(
                        text = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                        )
                    )
                }
                if (description != null) {
                    TextNormal(
                        text = description,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
                TextButton(
                    onClick = { onDismiss() },
                    shape = ButtonDefaults.textShape,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextNormal(text = stringResource(id = R.string.btn_ok))
                }
            }
        }
    }
}

@Composable
fun ProgressDialog(
    title: String? = null,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Row {
            if (title != null) {
                TextBold(text = title)
                VerticalSpacer16()
            }
            CircularProgressIndicator(color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmDialogPreview() {
    ConfirmDialog(
        title = "Confirm Dialog Title",
        description = "This is a confirm Dialog!",
        onConfirm = {}
    ) {
    }
}

@Composable
fun ConfirmDialog(
    title: String?,
    description: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = white,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = black,
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (title != null) {
                    TextBold(
                        text = title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                        )
                    )
                }
                if (description != null) {
                    TextNormal(
                        text = description,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        shape = ButtonDefaults.textShape,
                        modifier = Modifier.weight(1f)
                    ) {
                        TextNormal(text = stringResource(id = R.string.btn_cancel))
                    }
                    TextButton(
                        onClick = { onConfirm() },
                        shape = ButtonDefaults.textShape,
                        modifier = Modifier.weight(1f)
                    ) {
                        TextNormal(text = stringResource(id = R.string.btn_confirm))
                    }
                }
            }
        }
    }
}
