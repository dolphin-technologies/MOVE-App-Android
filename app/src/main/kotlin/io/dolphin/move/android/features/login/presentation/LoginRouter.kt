package io.dolphin.move.android.features.login.presentation

interface LoginRouter {
    fun backToLoginScreen()
    fun showLoginScreen()
    fun showLoginScreenAtStart()
    fun showLoginScreenAtLogout()
}