package io.dolphin.move.android.features.more.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.features.more.presentation.components.MoreInfoCard
import io.dolphin.move.android.features.more.presentation.components.MoreItem
import io.dolphin.move.android.features.profile.presentation.ProfileRouter
import io.dolphin.move.android.features.webview.presentation.WebRouter

@Composable
fun MoreScreen(
    moreViewModel: MoreViewModel = hiltViewModel(),
    profileRouter: ProfileRouter,
    webRouter: WebRouter,
) {
    MoreScreenContent(
        onShowProfile = profileRouter::showProfileScreen,
        onShowAgreement = webRouter::showAgreementInApp,
        userId = moreViewModel.getUserId(),
        appVersion = moreViewModel.getAppVersion()
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoreScreenContent(
    onShowProfile: () -> Unit,
    onShowAgreement: (Agreement) -> Unit,
    userId: String,
    appVersion: String
) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            VerticalSpacer16()
            MoreItem(
                text = stringResource(id = R.string.lbl_profile),
                onClick = onShowProfile
            )

            MoreItem(
                text = stringResource(id = R.string.tit_infos_help),
                onClick = { onShowAgreement(Agreement.INFO_HELP) }
            )

            MoreItem(
                text = stringResource(id = R.string.tit_imprint_contact),
                onClick = { onShowAgreement(Agreement.IMPRINT_ABOUT) }
            )

            MoreItem(
                text = stringResource(id = R.string.tit_terms_of_use),
                onClick = { onShowAgreement(Agreement.TERMS_OF_USE) }
            )

            MoreItem(
                text = stringResource(id = R.string.tit_data_privacy),
                onClick = { onShowAgreement(Agreement.PRIVACY_POLICY) }
            )

            MoreInfoCard(
                userId = userId,
                version = appVersion,
            )
        }
    }
}

@Composable
@Preview
fun MoreScreenPreview() {
    MoreScreenContent(
        onShowProfile = {},
        onShowAgreement = {},
        userId = "",
        appVersion = ""
    )
}