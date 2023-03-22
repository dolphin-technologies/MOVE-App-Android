package io.dolphin.move.android.features.onboarding.presentation

data class OnboardingViewState(
    val identity: UserIdentity = UserIdentity.NONE,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val companyName: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isTermsAccepted: Boolean = false,
    val isPolicyAccepted: Boolean = false,
)

