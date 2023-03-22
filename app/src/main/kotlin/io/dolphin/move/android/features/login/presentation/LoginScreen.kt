package io.dolphin.move.android.features.login.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer32
import io.dolphin.move.android.basepresentation.getPolicyString
import io.dolphin.move.android.basepresentation.getTermsString
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.features.forgotpassword.presentation.ForgotPasswordRouter
import io.dolphin.move.android.features.login.presentation.components.LoginHeaderGroup
import io.dolphin.move.android.features.onboarding.presentation.OnboardingRouter
import io.dolphin.move.android.features.permissions.presentation.StatusRouter
import io.dolphin.move.android.features.timeline.presentation.TimelineRouter
import io.dolphin.move.android.features.webview.presentation.WebRouter
import io.dolphin.move.android.ui.theme.color_bg_card

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    webRouter: WebRouter,
    onboardingRouter: OnboardingRouter,
    dashboardRouter: StatusRouter,
    forgotPasswordRouter: ForgotPasswordRouter,
    timelineRouter: TimelineRouter,
) {
    val scrollState = rememberScrollState()
    val viewState by viewModel.loginViewState.observeAsState(LoginViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)

    val (event, onEventChanged) = remember {
        mutableStateOf<LoginEvent>(LoginEvent.None)
    }

    if (showProgress) {
        ProgressDialog()
    }

    viewModel.events.collectAsEffect { onEventChanged(it) }
    if (event !is LoginEvent.None) {
        val title = event.titleId?.let { stringResource(id = it) }
        val description = event.message
            ?: event.messageId?.let { stringResource(id = it) }
        SimpleOkDialog(
            title = title,
            description = description,
        ) {
            if (event is LoginEvent.Success) {
                timelineRouter.showTimelineScreen()
            }
            onEventChanged(LoginEvent.None)
            if (event == LoginEvent.Success()) {
                dashboardRouter.showStatusScreen()
            }
        }
    }

    LoginScreenContent(
        onShowAgreement = webRouter::showAgreementInApp,
        onShowRegister = onboardingRouter::showOnboardingScreen,
        onForgotPassword = forgotPasswordRouter::showForgotPasswordScreen,
        viewState = viewState,
        loginView = viewModel,
        scrollState = scrollState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreenContent(
    onShowAgreement: (Agreement) -> Unit,
    onShowRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    viewState: LoginViewState,
    loginView: LoginView,
    scrollState: ScrollState,
) {

    val clickableTermsMessage = getTermsString()
    val clickablePolicyMessage = getPolicyString()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
    ) {
        LoginHeaderGroup()
        VerticalSpacer16()
        Card (
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = color_bg_card)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                MoveTextField(
                    value = viewState.email,
                    label = stringResource(R.string.lbl_your_email),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    onValueChange = {
                        loginView.onEmailChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.password,
                    label = stringResource(R.string.lbl_your_password),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        loginView.onPasswordChanged(it)
                    },
                )
                Column {
                    VerticalSpacer16()
                    MoveCheckItem(
                        isChecked = viewState.isTermsAccepted,
                        annotatedString = clickableTermsMessage,
                        onCheckedChange = {
                            if (it) {
                                loginView.acceptAgreement(Agreement.TERMS_OF_USE)
                            } else {
                                loginView.removeAgreement(Agreement.TERMS_OF_USE)
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
                    MoveCheckItem(
                        isChecked = viewState.isPolicyAccepted,
                        annotatedString = clickablePolicyMessage,
                        onCheckedChange = {
                            if (it) {
                                loginView.acceptAgreement(Agreement.PRIVACY_POLICY)
                            } else {
                                loginView.removeAgreement(Agreement.PRIVACY_POLICY)
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
                VerticalSpacer32()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MoveButton(buttonText = stringResource(R.string.tit_login)) {
                        loginView.requestLogin()
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextButton(
                onClick = onShowRegister
            ) {
                TextLink(text = stringResource(R.string.lnk_dont_have_account))
            }
            TextButton(
                onClick = onForgotPassword
            ) {
                TextLink(text = stringResource(R.string.lnk_forgot_password_q))
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreenContent(
        onShowAgreement = {},
        onShowRegister = {},
        onForgotPassword = {},
        viewState = LoginViewState(),
        loginView = PreviewLoginViewAdapter,
        scrollState = ScrollState(0),
    )
}

private object PreviewLoginViewAdapter : LoginView {
    override fun requestLogin() {}
    override fun onEmailChanged(value: String) {}
    override fun onPasswordChanged(value: String) {}
    override fun removeAgreement(agreement: Agreement) {}
    override fun acceptAgreement(agreement: Agreement) {}
}
