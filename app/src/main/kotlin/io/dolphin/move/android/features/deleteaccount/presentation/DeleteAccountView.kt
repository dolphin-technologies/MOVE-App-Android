package io.dolphin.move.android.features.deleteaccount.presentation

interface DeleteAccountView {

    fun onPasswordChanged(value: String)

    fun requestDeleteAccount()

}