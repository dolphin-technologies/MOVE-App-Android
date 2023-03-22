package io.dolphin.move.android.basepresentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import io.dolphin.move.android.R
import io.dolphin.move.android.basedata.PRIVACY_POLICY
import io.dolphin.move.android.basedata.TERMS_OF_USE
import io.dolphin.move.android.ui.theme.dusty_teal

@Composable
@ReadOnlyComposable
fun getTermsString(): AnnotatedString {
    return getAnnotatedString(
        label = stringResource(R.string.lbl_accept),
        UrlText(
            text = stringResource(R.string.lnk_terms_of_use),
            url = TERMS_OF_USE,
        ),
    )
}

@Composable
@ReadOnlyComposable
fun getPolicyString(): AnnotatedString {
    return getAnnotatedString(
        label = stringResource(R.string.lbl_privacy),
        UrlText(
            text = stringResource(R.string.lnk_privacy_policy),
            url = PRIVACY_POLICY,
        ),
    )
}

fun getAnnotatedString(label: String, linkText: String, url: String): AnnotatedString {
    return buildAnnotatedString {
        val result = buildString {
            append(label)
            append(" ")
            append(linkText)
        }
        val startIndex = result.indexOf(linkText)
        val endIndex = startIndex + linkText.length
        append(result)
        addStyle(
            style = textLinkSpan,
            start = startIndex,
            end = endIndex,
        )
        addStringAnnotation(
            tag = "URL",
            annotation = url,
            start = startIndex,
            end = endIndex,
        )
    }
}

fun getAnnotatedString(label: String, vararg urlTexts: UrlText): AnnotatedString {
    return buildAnnotatedString {
        val result = buildString {
            append(label)
            urlTexts.forEach {
                append(" ")
                append(it.text)
            }
        }
        append(result)
        urlTexts.forEach {
            val startIndex = result.indexOf(it.text)
            val endIndex = startIndex + it.text.length

            addStyle(
                style = textLinkSpan,
                start = startIndex,
                end = endIndex,
            )
            addStringAnnotation(
                tag = "URL",
                annotation = it.url,
                start = startIndex,
                end = endIndex,
            )
        }
    }
}

data class UrlText(
    val text: String,
    val url: String,
)

val textLinkSpan = SpanStyle(
    color = dusty_teal,
    textDecoration = TextDecoration.Underline,
)
