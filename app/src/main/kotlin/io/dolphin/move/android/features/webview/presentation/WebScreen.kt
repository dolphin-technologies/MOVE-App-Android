package io.dolphin.move.android.features.webview.presentation

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.domain.entities.Agreement

@ExperimentalMaterial3Api
@Composable
fun WebScreen(
    agreement: Agreement? = null,
    directUrl: String? = null,
    viewModel: WebViewModel = hiltViewModel(),
) {
    val viewState by viewModel.webViewState.observeAsState(WebViewState())
    viewModel.requestRemoteUrl(agreement)
    val url = viewState.url

    if (url != null || directUrl != null) {
        MoveWebView(
            url = directUrl ?: url,
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@ExperimentalMaterial3Api
@Composable
fun MoveWebView(
    url: String?,
) {
    Column {
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    // to enable JS
                    settings.javaScriptEnabled = true
                    isVerticalScrollBarEnabled = true
                    url?.let(::loadUrl)
                }
            },
        )
    }
}