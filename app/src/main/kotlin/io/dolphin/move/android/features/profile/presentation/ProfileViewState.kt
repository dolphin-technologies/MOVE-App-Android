package io.dolphin.move.android.features.profile.presentation

import io.dolphin.move.android.features.onboarding.presentation.UserIdentity

data class ProfileViewState(
    val identity: UserIdentity = UserIdentity.NONE,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val companyName: String = "",
    val oldEmail: String = "",
    val password: String = "",
    val showPassword: Boolean = false
)
