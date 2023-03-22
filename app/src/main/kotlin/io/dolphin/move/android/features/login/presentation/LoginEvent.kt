package io.dolphin.move.android.features.login.presentation

import androidx.annotation.StringRes
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.viewmodel.Event

sealed class LoginEvent : Event() {

    object None : LoginEvent()

    data class Success(
        @StringRes override val titleId: Int? = R.string.val_tit_success,
        @StringRes override val messageId: Int = R.string.val_login_success,
    ) : LoginEvent()

    data class Error(
        @StringRes override val titleId: Int? = R.string.err_tit_error,
        @StringRes override val messageId: Int = R.string.err_network_error,
    ) : LoginEvent()

    data class NoTerms(
        @StringRes override val messageId: Int = R.string.err_tou_not_accepted,
    ) : LoginEvent()

    data class NoPolicy(
        @StringRes override val messageId: Int = R.string.err_dp_not_accepted,
    ) : LoginEvent()

    data class WrongCredentials(
        @StringRes override val messageId: Int = R.string.err_login_not_found,
    ) : LoginEvent()

    data class WrongPassword(
        @StringRes override val messageId: Int = R.string.err_wrong_password,
    ) : LoginEvent()

    data class InvalidEmail(
        @StringRes override val messageId: Int = R.string.err_invalid_email,
    ) : LoginEvent()

    data class Common(
        override val message: String?,
    ) : LoginEvent()
}