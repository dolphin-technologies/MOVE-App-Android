package io.dolphin.move.android.features.changepassword.presentation

data class ChangePasswordViewState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val repeatPassword: String = "",
)
