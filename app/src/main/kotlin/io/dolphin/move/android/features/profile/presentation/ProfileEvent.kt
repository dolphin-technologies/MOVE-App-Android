package io.dolphin.move.android.features.profile.presentation

import androidx.annotation.StringRes
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.viewmodel.Event

sealed class ProfileEvent : Event() {

    object None : ProfileEvent()

    data class LogoutSuccess(
        @StringRes override val titleId: Int? = R.string.btn_logout,
        @StringRes override val messageId: Int = R.string.val_tit_success,
    ) : ProfileEvent()

    data class SaveSuccess(
        @StringRes override val titleId: Int? = null,
        @StringRes override val messageId: Int = R.string.val_data_saved,
    ) : ProfileEvent()

    data class CheckEmail(
        @StringRes override val titleId: Int? = null,
        @StringRes override val messageId: Int = R.string.val_check_your_mailbox,
    ) : ProfileEvent()

    data class Error(
        @StringRes override val titleId: Int? = R.string.err_tit_error,
        @StringRes override val messageId: Int = R.string.err_network_error,
    ) : ProfileEvent()

    data class InvalidEmail(
        @StringRes override val messageId: Int = R.string.err_invalid_email,
    ) : ProfileEvent()

    data class RequiredFields(
        @StringRes override val messageId: Int = R.string.err_required_fields,
    ) : ProfileEvent()

    data class Common(
        override val message: String?,
    ) : ProfileEvent()
}