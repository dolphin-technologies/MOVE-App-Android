package io.dolphin.move.android.features.login.presentation

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val isTermsAccepted: Boolean = false,
    val isPolicyAccepted: Boolean = false,
)