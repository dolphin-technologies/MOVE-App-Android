package io.dolphin.move.android.features.profile.presentation

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.collectAsEffect
import io.dolphin.move.android.basepresentation.components.ConfirmDialog
import io.dolphin.move.android.basepresentation.components.MoveButton
import io.dolphin.move.android.basepresentation.components.MoveTextField
import io.dolphin.move.android.basepresentation.components.ProgressDialog
import io.dolphin.move.android.basepresentation.components.SimpleOkDialog
import io.dolphin.move.android.basepresentation.components.TextLink
import io.dolphin.move.android.basepresentation.components.TextMedium
import io.dolphin.move.android.basepresentation.components.TextNormal
import io.dolphin.move.android.basepresentation.components.spacers.VerticalSpacer16
import io.dolphin.move.android.features.changepassword.presentation.ChangePasswordRouter
import io.dolphin.move.android.features.deleteaccount.presentation.DeleteAccountRouter
import io.dolphin.move.android.features.login.presentation.LoginRouter
import io.dolphin.move.android.features.onboarding.presentation.UserIdentity
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.color_bg_card
import io.dolphin.move.android.ui.theme.dark_indigo
import io.dolphin.move.android.ui.theme.dusty_teal

private val identityOptions = UserIdentity.values().dropLast(1)

@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    changePasswordRouter: ChangePasswordRouter,
    deleteAccountRouter: DeleteAccountRouter,
    loginRouter: LoginRouter
) {
    val scrollState = rememberScrollState()
    val viewState by viewModel.profileViewState.observeAsState(ProfileViewState())
    val showProgress by viewModel.showProgress.observeAsState(false)

    val (event, onEventChanged) = remember {
        mutableStateOf<ProfileEvent>(ProfileEvent.None)
    }
    viewModel.events.collectAsEffect {
        onEventChanged(it)
    }
    if (showProgress) {
        ProgressDialog()
    }

    if (event !is ProfileEvent.None) {
        val title = event.titleId?.let { stringResource(id = it) }
        val description = event.message
            ?: event.messageId?.let { stringResource(id = it) }
        when (event) {
            is ProfileEvent.LogoutSuccess -> {
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
                    onEventChanged(ProfileEvent.None)
                }
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_CREATE) {
                    viewModel.getContract()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    ProfileScreenContent(
        onChangePassword = changePasswordRouter::showChangePasswordScreen,
        onDeleteAccount = deleteAccountRouter::showDeleteAccountScreen,
        viewState = viewState,
        profileView = viewModel,
        scrollState = scrollState,
    )
}

@ExperimentalMaterial3Api
@Composable
private fun ProfileScreenContent(
    onChangePassword: () -> Unit,
    onDeleteAccount: () -> Unit,
    viewState: ProfileViewState,
    profileView: ProfileView,
    scrollState: ScrollState,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    if (showLogoutDialog) {
        ConfirmDialog(
            title = stringResource(id = R.string.hint_attention),
            description = stringResource(id = R.string.hint_logout_warning),
            onConfirm = {
                showLogoutDialog = false
                profileView.onLogoutRequested()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = color_bg_card)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                VerticalSpacer16()
                TextMedium(
                    text = stringResource(R.string.subtit_your_data),
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
                                        profileView.onIdentitySelected(userIdentity)
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
                        profileView.onFirstNameChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.lastName,
                    label = "${stringResource(R.string.lbl_your_lastname)} *",
                    onValueChange = {
                        profileView.onLastNameChanged(it)
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
                        profileView.onEmailChanged(it)
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
                        profileView.onPhoneChanged(it)
                    },
                )
                VerticalSpacer16()
                MoveTextField(
                    value = viewState.companyName,
                    label = stringResource(R.string.lbl_company_name),
                    onValueChange = {
                        profileView.onCompanyChanged(it)
                    },
                )
                VerticalSpacer16()
                if (viewState.showPassword) {
                    MoveTextField(
                        value = viewState.password,
                        label = "${stringResource(R.string.lbl_your_password)} *",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = {
                            profileView.onPasswordChanged(it)
                        },
                    )
                    VerticalSpacer16()
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    VerticalSpacer16()
                    MoveButton(
                        buttonText = stringResource(R.string.btn_save).uppercase(),
                        onClick = profileView::onSaveUserRequested,
                    )
                    VerticalSpacer16()
                    MoveButton(
                        buttonText = stringResource(R.string.btn_logout).uppercase(),
                        onClick = { showLogoutDialog = true }
                    )
                    VerticalSpacer16()
                    TextButton(
                        onClick = onChangePassword
                    ) {
                        TextLink(text = stringResource(R.string.lnk_change_password))
                    }
                    TextButton(
                        onClick = onDeleteAccount
                    ) {
                        TextLink(text = stringResource(R.string.lnk_delete_account))
                    }
                    VerticalSpacer16()
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
@Preview
private fun ProfileScreenContentPreview() {
    ProfileScreenContent(
        onChangePassword = {},
        onDeleteAccount = {},
        viewState = ProfileViewState(),
        profileView = PreviewProfileViewAdapter,
        scrollState = ScrollState(0),
    )
}

private object PreviewProfileViewAdapter : ProfileView {
    override fun onIdentitySelected(value: UserIdentity) {}
    override fun onFirstNameChanged(value: String) {}
    override fun onLastNameChanged(value: String) {}
    override fun onEmailChanged(value: String) {}
    override fun onPhoneChanged(value: String) {}
    override fun onCompanyChanged(value: String) {}
    override fun onPasswordChanged(value: String) {}
    override fun onSaveUserRequested() {}
    override fun onLogoutRequested() {}
}