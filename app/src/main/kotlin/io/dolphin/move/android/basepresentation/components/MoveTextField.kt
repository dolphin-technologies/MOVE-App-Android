package io.dolphin.move.android.basepresentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.light_blue_grey
import io.dolphin.move.android.ui.theme.styleNormal

@ExperimentalMaterial3Api
@Composable
fun MoveTextField(
    value: String,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onNextFocus: (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        textStyle = styleNormal(fontSize = 16.sp),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = dark_indigo,
            focusedLabelColor = battleship_grey,
            unfocusedLabelColor = battleship_grey,
            focusedBorderColor = light_blue_grey,
            unfocusedBorderColor = light_blue_grey,
        ),
        label = {
            TextNormal(text = label, color = battleship_grey)
        },
        keyboardActions = KeyboardActions(
            onNext = {
                this.defaultKeyboardAction(ImeAction.Next)
                onNextFocus?.invoke()
            },
            onDone = {
                focusManager.clearFocus()
            }
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
    )
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun MoveTextFieldPreview() {
    Column(Modifier.background(Color.White)) {
        MoveTextField(
            value = "some value",
            label = "Label",
            onValueChange = {}
        )
    }
}