package io.dolphin.move.android.features.forgotpassword.presentation

import androidx.annotation.StringRes
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.viewmodel.Event

sealed class ForgotPasswordEvent : Event() {

    object None : ForgotPasswordEvent()

    data class PasswordDropped(
        @StringRes override val titleId: Int? = R.string.lnk_forgot_password,
        @StringRes override val messageId: Int = R.string.val_forgot_password,
    ) : ForgotPasswordEvent()

    data class InvalidEmail(
        @StringRes override val messageId: Int = R.string.err_invalid_email,
    ) : ForgotPasswordEvent()

    data class Error(
        @StringRes override val titleId: Int? = R.string.err_tit_error,
        @StringRes override val messageId: Int = R.string.err_network_error,
    ) : ForgotPasswordEvent()

    data class Common(
        override val message: String?,
    ) : ForgotPasswordEvent()
}