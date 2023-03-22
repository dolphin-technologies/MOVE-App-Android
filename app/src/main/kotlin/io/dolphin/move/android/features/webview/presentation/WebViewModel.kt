package io.dolphin.move.android.features.webview.presentation

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.dolphin.move.android.basepresentation.BaseViewModel
import io.dolphin.move.android.basepresentation.delegate
import io.dolphin.move.android.basepresentation.mapDistinct
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.firebase.domain.RemoteConfigInteractor
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val remoteConfigInteractor: RemoteConfigInteractor,
) : BaseViewModel() {

    private val liveState = MutableLiveData(WebViewState())
    private var viewState: WebViewState by liveState.delegate()

    val webViewState = liveState.mapDistinct { it }

    fun requestRemoteUrl(agreement: Agreement?) {
        if (agreement == null) return
        val remoteConfigs = remoteConfigInteractor.getRemoteConfigs()
        viewState = viewState.copy(
            url = when (agreement) {
                Agreement.TERMS_OF_USE -> remoteConfigs.urlTermsOfUse
                Agreement.PRIVACY_POLICY -> remoteConfigs.urlPrivacy
                Agreement.PRIVACY_ANALYTICS -> remoteConfigs.urlPrivacyAnalytics
                Agreement.INFO_HELP -> remoteConfigs.urlHelp
                Agreement.IMPRINT_ABOUT -> remoteConfigs.urlImprint
            },
        )
    }
}