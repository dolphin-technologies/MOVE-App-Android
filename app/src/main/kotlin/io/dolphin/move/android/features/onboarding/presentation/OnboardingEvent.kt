package io.dolphin.move.android.features.onboarding.presentation

import androidx.annotation.StringRes
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.viewmodel.Event

sealed class OnboardingEvent : Event() {

    object None : OnboardingEvent()

    data class Success(
        @StringRes override val titleId: Int? = R.string.val_tit_success,
        @StringRes override val messageId: Int = R.string.val_registration_success,
    ) : OnboardingEvent()

    data class Error(
        @StringRes override val titleId: Int? = R.string.err_tit_error,
        @StringRes override val messageId: Int = R.string.err_network_error,
    ) : OnboardingEvent()

    data class NoTerms(
        @StringRes override val messageId: Int = R.string.err_tou_not_accepted,
    ) : OnboardingEvent()

    data class NoPolicy(
        @StringRes override val messageId: Int = R.string.err_dp_not_accepted,
    ) : OnboardingEvent()

    data class InvalidEmail(
        @StringRes override val messageId: Int = R.string.err_invalid_email,
    ) : OnboardingEvent()

    data class EmailAlreadyInUse(
        @StringRes override val messageId: Int = R.string.err_email_already_in_use,
    ) : OnboardingEvent()

    data class RequiredFields(
        @StringRes override val messageId: Int = R.string.err_required_fields,
    ) : OnboardingEvent()

    data class PasswordMismatch(
        @StringRes override val messageId: Int = R.string.err_password_mismatch,
    ) : OnboardingEvent()

    data class InvalidPassword(
        @StringRes override val messageId: Int = R.string.err_password_policy_error,
    ) : OnboardingEvent()

    data class SelectGender(
        @StringRes override val messageId: Int = R.string.err_missing_gender,
    ) : OnboardingEvent()
}