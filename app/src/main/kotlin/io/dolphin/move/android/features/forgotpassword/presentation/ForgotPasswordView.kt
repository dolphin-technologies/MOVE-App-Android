package io.dolphin.move.android.features.forgotpassword.presentation

interface ForgotPasswordView {
    fun onEmailChanged(value: String)
    fun requestDropPassword()
}