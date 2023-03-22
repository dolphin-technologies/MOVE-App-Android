package io.dolphin.move.android.features.forgotpassword.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.collectAsEffect
import io.dolphin.move.android.basepresentation.components.MoveButton
import io.dolphin.move.android.basepresentation.components.MoveTextField
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.SimpleOkDialog
import io.dolphin.move.android.basepresentation.components.TextBold
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer12
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer24
import io.dolphin.move.android.ui.theme.color_bg_card

@ExperimentalMaterial3Api
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onSuccessChange: () -> Unit,
) {

    val viewState by viewModel.forgotPasswordViewState.observeAsState(ForgotPasswordViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)

    val (event, onEventChanged) = remember {
        mutableStateOf<ForgotPasswordEvent>(ForgotPasswordEvent.None)
    }

    if (showProgress) {
        ProgressDialog()
    }

    viewModel.events.collectAsEffect {
        onEventChanged(it)
    }

    if (event !is ForgotPasswordEvent.None) {
        val title = event.titleId?.let { stringResource(id = it) }
        val description = event.message ?: event.messageId?.let { stringResource(it) }
        SimpleOkDialog(
            title = title,
            description = description,
        ) {
            onEventChanged(ForgotPasswordEvent.None)
            if (event is ForgotPasswordEvent.PasswordDropped) {
                onSuccessChange()
            }
        }
    }

    ForgotPasswordContent(
        viewState = viewState,
        forgotPasswordView = viewModel,
    )
}

@ExperimentalMaterial3Api
@Composable
private fun ForgotPasswordContent(
    viewState: ForgotPasswordViewState,
    forgotPasswordView: ForgotPasswordView,
) {

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        VerticalSpacer12()
        TextBold(
            text = stringResource(id = R.string.lbl_password),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
        )
        VerticalSpacer16()
        TextNormal(
            text = stringResource(id = R.string.txt_login_welcometext),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
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
                        imeAction = ImeAction.Done,
                    ),
                    onValueChange = {
                        forgotPasswordView.onEmailChanged(it)
                    },
                )
                VerticalSpacer24()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MoveButton(
                        buttonText = stringResource(R.string.btn_ok),
                        onClick = forgotPasswordView::requestDropPassword,
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun ForgotPasswordPreview() {
    ForgotPasswordContent(
        viewState = ForgotPasswordViewState(),
        forgotPasswordView = PreviewForgotPasswordViewAdapter,
    )
}

private object PreviewForgotPasswordViewAdapter : ForgotPasswordView {
    override fun requestDropPassword() {}
    override fun onEmailChanged(value: String) {}
}
