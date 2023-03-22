package io.dolphin.move.android.features.profile.presentation

import io.dolphin.move.android.features.onboarding.presentation.UserIdentity

interface ProfileView {
    fun onIdentitySelected(value: UserIdentity)

    fun onFirstNameChanged(value: String)

    fun onLastNameChanged(value: String)

    fun onEmailChanged(value: String)

    fun onPhoneChanged(value: String)

    fun onCompanyChanged(value: String)

    fun onPasswordChanged(value: String)

    fun onSaveUserRequested()

    fun onLogoutRequested()
}