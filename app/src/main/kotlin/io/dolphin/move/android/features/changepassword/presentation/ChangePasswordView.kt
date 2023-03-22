package io.dolphin.move.android.features.changepassword.presentation

interface ChangePasswordView {

    fun onCurrentPasswordChanged(value: String)

    fun onNewPasswordChanged(value: String)

    fun onRepeatPasswordChanged(value: String)

    fun onLogoutRequested()

    fun onSaveNewPassword()

}