package io.dolphin.move.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val styleNormal = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
)

fun styleNormal(
    color: Color = dark_indigo,
    fontSize: TextUnit = 14.sp,
) = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Normal,
    fontSize = fontSize,
    color = color,
)

val styleMedium = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    color = dark_indigo,
)

fun styleMedium(
    color: Color = Color.Unspecified,
    fontSize: TextUnit = 14.sp,
) = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Medium,
    fontSize = fontSize,
    color = color,
)

val styleBold = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
    color = dark_indigo,
)

fun styleBold(
    color: Color = dark_indigo,
    fontSize: TextUnit = 14.sp,
) = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Bold,
    fontSize = fontSize,
    color = color,
)

val styleLink = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = dusty_teal,
    textDecoration = TextDecoration.Underline
)

fun styleLink(
    color: Color = dusty_teal,
    fontSize: TextUnit = 14.sp,
) = TextStyle(
    fontFamily = helveticaFamily,
    fontWeight = FontWeight.Bold,
    fontSize = fontSize,
    color = color,
)
