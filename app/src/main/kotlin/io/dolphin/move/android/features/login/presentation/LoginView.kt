package io.dolphin.move.android.features.login.presentation

import io.dolphin.move.android.domain.entities.Agreement

interface LoginView {
    fun requestLogin()
    fun onEmailChanged(value: String)
    fun onPasswordChanged(value: String)
    fun removeAgreement(agreement: Agreement)
    fun acceptAgreement(agreement: Agreement)
}