package io.dolphin.move.android.features.deleteaccount.presentation

import androidx.annotation.StringRes
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.viewmodel.Event

sealed class DeleteAccountEvent : Event() {

    object None : DeleteAccountEvent()

    data class Success(
        @StringRes override val titleId: Int? = R.string.tit_delete_account,
        @StringRes override val messageId: Int = R.string.hint_delete_successful,
    ) : DeleteAccountEvent()

    data class Error(
        @StringRes override val titleId: Int? = R.string.err_tit_error,
        @StringRes override val messageId: Int = R.string.err_network_error,
    ) : DeleteAccountEvent()

    data class RequiredFields(
        @StringRes override val messageId: Int = R.string.err_required_fields,
    ) : DeleteAccountEvent()

    data class Common(
        override val message: String?,
    ) : DeleteAccountEvent()

}