package io.dolphin.move.android.features.changepassword.presentation

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.collectAsEffect
import io.dolphin.move.android.basepresentation.components.MoveButton
import io.dolphin.move.android.basepresentation.components.MoveTextField
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.SimpleOkDialog
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer24
import io.dolphin.move.android.features.login.presentation.LoginRouter
import io.dolphin.move.android.ui.theme.color_bg_card

@ExperimentalMaterial3Api
@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    loginRouter: LoginRouter
) {
    val scrollState = rememberScrollState()
    val viewState by viewModel.changePasswordViewState.observeAsState(ChangePasswordViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)

    val (event, onEventChanged) = remember {
        mutableStateOf<ChangePasswordEvent>(ChangePasswordEvent.None)
    }
    viewModel.events.collectAsEffect {
        onEventChanged(it)
    }
    if (showProgress) {
        ProgressDialog()
    }

    if (event !is ChangePasswordEvent.None) {
        val title = event.titleId?.let { stringResource(id = it) }
        val description = event.message
            ?: event.messageId?.let { stringResource(id = it) }
        when (event) {
            is ChangePasswordEvent.Success -> {
                SimpleOkDialog(
                    title = title,
                    description = description,
                ) {
                    viewModel.onLogoutRequested()
                }
            }
            is ChangePasswordEvent.LogoutSuccess -> {
                SimpleOkDialog(
                    title = title,
                    description = description,
                ) {
                    loginRouter.showLoginScreenAtStart()
                }
            }
            else -> {
                SimpleOkDialog(
                    title = title,
                    description = description,
                ) {
                    onEventChanged(ChangePasswordEvent.None)
                }
            }
        }
    }

    ChangePasswordContent(
        viewState = viewState,
        changePasswordView = viewModel,
        scrollState = scrollState,
    )
}

@ExperimentalMaterial3Api
@Composable
private fun ChangePasswordContent(
    viewState: ChangePasswordViewState,
    changePasswordView: ChangePasswordView,
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
                MoveTextField(
                    value = viewState.currentPassword,
                    label = "${stringResource(R.string.lbl_current_password)} *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        changePasswordView.onCurrentPasswordChanged(it)
                    },
                )
                VerticalSpacer24()
                MoveTextField(
                    value = viewState.newPassword,
                    label = "${stringResource(R.string.lbl_set_new_password)} *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        changePasswordView.onNewPasswordChanged(it)
                    },
                )
                VerticalSpacer24()
                MoveTextField(
                    value = viewState.repeatPassword,
                    label = "${stringResource(R.string.lbl_repeat_password)} *",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        changePasswordView.onRepeatPasswordChanged(it)
                    },
                )
                VerticalSpacer24()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MoveButton(
                        buttonText = stringResource(R.string.btn_save_new_password).uppercase(),
                        onClick = changePasswordView::onSaveNewPassword
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun ChangePasswordPreview() {
    ChangePasswordContent(
        viewState = ChangePasswordViewState(),
        changePasswordView = ChangePasswordViewAdapter,
        scrollState = ScrollState(0),
    )
}

private object ChangePasswordViewAdapter : ChangePasswordView {
    override fun onCurrentPasswordChanged(value: String) {}
    override fun onNewPasswordChanged(value: String) {}
    override fun onRepeatPasswordChanged(value: String) {}
    override fun onLogoutRequested() {}
    override fun onSaveNewPassword() {}
}

