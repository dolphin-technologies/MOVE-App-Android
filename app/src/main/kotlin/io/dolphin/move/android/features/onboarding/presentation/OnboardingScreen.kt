package io.dolphin.move.android.features.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.collectAsEffect
import io.dolphin.move.android.basepresentation.components.MoveButton
import io.dolphin.move.android.basepresentation.components.MoveCheckItem
import io.dolphin.move.android.basepresentation.components.MoveTextField
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.SimpleOkDialog
import io.dolphin.move.android.basepresentation.components.TextLink
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer72
import io.dolphin.move.android.basepresentation.getPolicyString
import io.dolphin.move.android.basepresentation.getTermsString
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.features.login.presentation.LoginRouter
import io.dolphin.move.android.features.onboarding.presentation.components.OnboardingHeaderGroup
import io.dolphin.move.android.features.permissions.presentation.StatusRouter
import io.dolphin.move.android.features.webview.presentation.WebRouter
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.dusty_teal
import io.dolphin.move.android.ui.theme.pale_grey

private val identityOptions = UserIdentity.values().dropLast(1)

@ExperimentalMaterial3Api
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    webRouter: WebRouter,
    loginRouter: LoginRouter,
    dashboardRouter: StatusRouter
) {
    val scrollState = rememberScrollState()
    val viewState by viewModel.onboardingViewState.observeAsState(OnboardingViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)

    val (event, onEventChanged) = remember {
        mutableStateOf<OnboardingEvent>(OnboardingEvent.None)
    }
    viewModel.events.collectAsEffect {
        onEventChanged(it)
    }
    if (showProgress) {
        ProgressDialog()
    }
    if (event !is OnboardingEvent.None) {
        val title = event.titleId?.let { stringResource(id = it) }
        val description = event.message
            ?: event.messageId?.let { stringResource(id = it) }
        SimpleOkDialog(
            title = title,
            description = description,
        ) {
            onEventChanged(OnboardingEvent.None)
            if (event == OnboardingEvent.Success()) {
                dashboardRouter.showStatusScreen()
            }
        }
    }

    OnboardingScreenContent(
        viewState = viewState,
        onboardingView = viewModel,
        onShowAgreement = webRouter::showAgreementInApp,
        onShowLogin = loginRouter::backToLoginScreen,
        scrollState = scrollState,
    )
}

@ExperimentalMaterial3Api
@Composable
private fun OnboardingScreenContent(
    viewState: OnboardingViewState,
    onboardingView: OnboardingView,
    onShowAgreement: (Agreement) -> Unit,
    onShowLogin: () -> Unit,
    scrollState: ScrollState,
) {
    val clickableTermsMessage = getTermsString()
    val clickablePolicyMessage = getPolicyString()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
    ) {
        OnboardingHeaderGroup()
        VerticalSpacer16()
        Card(
            modifier = Modifier.padding(all = 0.dp),
            colors = CardDefaults.cardColors(containerColor = pale_grey)
        ) {
            Column (
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                VerticalSpacer16()
                TextMedium(
                    text = stringResource(R.string.txt_ident),
                    color = dark_indigo,
                )
                VerticalSpacer16()
                Row(Modifier.selectableGroup()) {
                    identityOptions.forEach { userIdentity ->
                        val text = stringResource(userIdentity.textId)
                        Row(
                            Modifier
                                .wrapContentWidth()
                                .selectable(
                                    selected = (viewState.identity == userIdentity),
                                    onClick = {
                                        onboardingView.onIdentitySelected(userIdentity)
                                    },
                                    role = Role.RadioButton,
                                ),
                            verticalAlignment = CenterVertically,
                        ) {
                            RadioButton(
                                selected = (viewState.identity == userIdentity),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = dusty_teal,
                                    unselectedColor = battleship_grey,
                                ),
                            )
                            TextNormal(
                                text = text,
                                modifier = Modifier.padding(start = 5.dp, end = 16.dp),
                                color = battleship_grey,
                            )
                        }
                    }
                }
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.firstName,
                    label = "${stringResource(R.string.lbl_your_firstname)} *",
                    onNextFocus = {

                    },
                    onValueChange = {
                        onboardingView.onFirstNameChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.lastName,
                    label = "${stringResource(R.string.lbl_your_lastname)} *",
                    onValueChange = {
                        onboardingView.onLastNameChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.email,
                    label = "${stringResource(R.string.lbl_your_email)} *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    onValueChange = {
                        onboardingView.onEmailChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.phone,
                    label = stringResource(R.string.lbl_your_cell),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                    ),
                    onValueChange = {
                        onboardingView.onPhoneChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.companyName,
                    label = stringResource(R.string.lbl_company_name),
                    onValueChange = {
                        onboardingView.onCompanyChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.password,
                    label = "${stringResource(R.string.lbl_choose_password)} *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        onboardingView.onPasswordChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.repeatPassword,
                    label = "${stringResource(R.string.lbl_repeat_password)} *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        onboardingView.onRepeatPasswordChanged(it)
                    },
                )

                Column {
                    VerticalSpacer16()
                    MoveCheckItem(
                        isChecked = viewState.isTermsAccepted,
                        annotatedString = clickableTermsMessage,
                        onCheckedChange = {
                            if (it) {
                                onboardingView.acceptAgreement(Agreement.TERMS_OF_USE)
                            } else {
                                onboardingView.removeAgreement(Agreement.TERMS_OF_USE)
                            }
                        },
                        onClick = {
                            /**
                             * To make sure that we are handling click on the link and not on any symbol
                             */
                            val annotations =
                                clickableTermsMessage.getStringAnnotations("URL", it, it)
                            annotations.firstOrNull()?.let {
                                onShowAgreement(Agreement.TERMS_OF_USE)
                            }
                        }
                    )
                    VerticalSpacer16()
                    MoveCheckItem(
                        isChecked = viewState.isPolicyAccepted,
                        annotatedString = clickablePolicyMessage,
                        onCheckedChange = {
                            if (it) {
                                onboardingView.acceptAgreement(Agreement.PRIVACY_POLICY)
                            } else {
                                onboardingView.removeAgreement(Agreement.PRIVACY_POLICY)
                            }
                        },
                        onClick = {
                            /**
                             * To make sure that we are handling click on the link and not on any symbol
                             */
                            val annotations =
                                clickablePolicyMessage.getStringAnnotations("URL", it, it)
                            annotations.firstOrNull()?.let {
                                onShowAgreement(Agreement.PRIVACY_POLICY)
                            }
                        }
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    VerticalSpacer16()
                    MoveButton(
                        buttonText = stringResource(R.string.btn_register),
                        onClick = onboardingView::onRegisterUserRequested,
                    )
                    VerticalSpacer16()
                    TextButton(
                        onClick = onShowLogin
                    ) {
                        TextLink(text = stringResource(R.string.lnk_existing_account))
                    }
                    VerticalSpacer72()
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
@Preview
private fun OnboardingScreenContentPreview() {
    OnboardingScreenContent(
        viewState = OnboardingViewState(),
        onboardingView = PreviewOnboardingViewAdapter,
        onShowAgreement = {},
        onShowLogin = {},
        scrollState = ScrollState(0),
    )
}

private object PreviewOnboardingViewAdapter : OnboardingView {
    override fun onIdentitySelected(value: UserIdentity) {}
    override fun onFirstNameChanged(value: String) {}
    override fun onLastNameChanged(value: String) {}
    override fun onEmailChanged(value: String) {}
    override fun onPhoneChanged(value: String) {}
    override fun onCompanyChanged(value: String) {}
    override fun onPasswordChanged(value: String) {}
    override fun onRepeatPasswordChanged(value: String) {}
    override fun onRegisterUserRequested() {}
    override fun acceptAgreement(agreement: Agreement) {}
    override fun removeAgreement(agreement: Agreement) {}
}