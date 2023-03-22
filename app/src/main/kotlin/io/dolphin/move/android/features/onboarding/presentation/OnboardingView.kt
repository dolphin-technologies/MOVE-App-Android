package io.dolphin.move.android.features.onboarding.presentation

import io.dolphin.move.android.domain.entities.Agreement

interface OnboardingView {
    fun onIdentitySelected(value: UserIdentity)

    fun onFirstNameChanged(value: String)

    fun onLastNameChanged(value: String)

    fun onEmailChanged(value: String)

    fun onPhoneChanged(value: String)

    fun onCompanyChanged(value: String)

    fun onPasswordChanged(value: String)

    fun onRepeatPasswordChanged(value: String)

    fun onRegisterUserRequested()

    fun acceptAgreement(agreement: Agreement)

    fun removeAgreement(agreement: Agreement)
}