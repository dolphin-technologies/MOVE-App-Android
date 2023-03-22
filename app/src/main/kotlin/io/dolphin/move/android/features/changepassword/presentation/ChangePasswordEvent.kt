package io.dolphin.move.android.features.changepassword.presentation

import androidx.annotation.StringRes
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.viewmodel.Event

sealed class ChangePasswordEvent : Event() {

    object None : ChangePasswordEvent()

    data class Success(
        @StringRes override val titleId: Int? = R.string.val_tit_success,
        @StringRes override val messageId: Int = R.string.val_password_reset_success,
    ) : ChangePasswordEvent()

    data class LogoutSuccess(
        @StringRes override val titleId: Int? = R.string.btn_logout,
        @StringRes override val messageId: Int = R.string.val_tit_success,
    ) : ChangePasswordEvent()

    data class PasswordMismatch(
        @StringRes override val titleId: Int? = null,
        @StringRes override val messageId: Int = R.string.err_password_mismatch,
    ) : ChangePasswordEvent()

    data class Error(
        @StringRes override val titleId: Int? = R.string.err_tit_error,
        @StringRes override val messageId: Int = R.string.err_network_error,
    ) : ChangePasswordEvent()

    data class RequiredFields(
        @StringRes override val messageId: Int = R.string.err_required_fields,
    ) : ChangePasswordEvent()

    data class Common(
        override val message: String?,
    ) : ChangePasswordEvent()
}