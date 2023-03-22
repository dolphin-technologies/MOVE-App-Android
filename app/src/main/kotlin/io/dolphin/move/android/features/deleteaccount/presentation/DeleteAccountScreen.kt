package io.dolphin.move.android.features.deleteaccount.presentation

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.collectAsEffect
import io.dolphin.move.android.basepresentation.components.MoveButton
import io.dolphin.move.android.basepresentation.components.MoveTextField
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.SimpleOkDialog
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer12
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer24
import io.dolphin.move.android.features.login.presentation.LoginRouter
import io.dolphin.move.android.ui.theme.color_bg_card

@ExperimentalMaterial3Api
@Composable
fun DeleteAccountScreen(
    viewModel: DeleteAccountViewModel = hiltViewModel(),
    loginRouter: LoginRouter
) {
    val scrollState = rememberScrollState()
    val viewState by viewModel.deleteAccountViewState.observeAsState(DeleteAccountViewState())
    val showProgress by viewModel.showProgress.observeAsState(true)

    val (event, onEventChanged) = remember {
        mutableStateOf<DeleteAccountEvent>(DeleteAccountEvent.None)
    }
    viewModel.events.collectAsEffect {
        onEventChanged(it)
    }
    if (showProgress) {
        ProgressDialog()
    }

    if (event !is DeleteAccountEvent.None) {
        val title = event.titleId?.let { stringResource(id = it) }
        val description = event.message
            ?: event.messageId?.let { stringResource(id = it) }
        SimpleOkDialog(
            title = title,
            description = description,
        ) {
            onEventChanged(DeleteAccountEvent.None)
            if (event == DeleteAccountEvent.Success()) {
                loginRouter.showLoginScreenAtStart()
            }
        }
    }

    DeleteAccountContent(
        viewState = viewState,
        deleteAccountView = viewModel,
        scrollState = scrollState,
    )
}

@ExperimentalMaterial3Api
@Composable
private fun DeleteAccountContent(
    viewState: DeleteAccountViewState,
    deleteAccountView: DeleteAccountView,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Card (
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 0.dp),
            colors = CardDefaults.cardColors(containerColor = color_bg_card)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextNormal(
                    text = stringResource(id = R.string.hint_delete_warning),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                VerticalSpacer12()
                MoveTextField(
                    value = viewState.password,
                    label = "${stringResource(R.string.lbl_password)} *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        deleteAccountView.onPasswordChanged(it)
                    },
                )
                VerticalSpacer24()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MoveButton(
                        buttonText = stringResource(R.string.lnk_delete_account),
                        onClick = {
                            deleteAccountView.requestDeleteAccount()
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun DeleteAccountPreview() {
    DeleteAccountContent(
        viewState = DeleteAccountViewState(),
        deleteAccountView = PreviewDeleteAccountViewAdapter,
        scrollState = ScrollState(0),
    )
}

private object PreviewDeleteAccountViewAdapter : DeleteAccountView {
    override fun onPasswordChanged(value: String) {}
    override fun requestDeleteAccount() {}
}
