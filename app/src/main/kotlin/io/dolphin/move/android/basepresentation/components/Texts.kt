package io.dolphin.move.android.basepresentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.dusty_teal
import io.dolphin.move.android.ui.theme.styleBold
import io.dolphin.move.android.ui.theme.styleLink
import io.dolphin.move.android.ui.theme.styleMedium
import io.dolphin.move.android.ui.theme.styleNormal

@Composable
fun TextNormal(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = dark_indigo,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = styleNormal,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        textDecoration = textDecoration,
        overflow = overflow,
    )
}

@Composable
fun TextNormal(
    anotatedString: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = dark_indigo,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = styleNormal,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = anotatedString,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        textDecoration = textDecoration,
        overflow = overflow,
    )
}

@Composable
fun TextMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = dark_indigo,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = styleMedium,
    textDecoration: TextDecoration? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        textDecoration = textDecoration,
    )
}

@Composable
fun TextMedium(
    anotatedString: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = dark_indigo,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = styleMedium,
    textDecoration: TextDecoration? = null,
) {
    Text(
        text = anotatedString,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        textDecoration = textDecoration,
    )
}

@Composable
fun TextBold(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = dark_indigo,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = styleBold,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        textDecoration = textDecoration,
        overflow = overflow,
    )
}

@Composable
fun TextBold(
    anotatedString: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = dark_indigo,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = styleBold,
    textDecoration: TextDecoration? = null,
) {
    Text(
        text = anotatedString,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        textDecoration = textDecoration,
    )
}

@Composable
fun TextLink(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = dusty_teal,
    textAlign: TextAlign? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = styleLink,
    textDecoration: TextDecoration? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        fontSize = fontSize,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        style = style,
        textDecoration = textDecoration,
    )
}