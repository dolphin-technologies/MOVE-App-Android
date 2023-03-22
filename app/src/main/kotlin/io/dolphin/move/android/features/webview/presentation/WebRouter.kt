package io.dolphin.move.android.features.webview.presentation

import io.dolphin.move.android.domain.entities.Agreement

interface WebRouter {
    fun showAgreementInApp(agreement: Agreement)
    fun showUrlInApp(url: String)
}