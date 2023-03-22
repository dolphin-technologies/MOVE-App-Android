package io.dolphin.move.android.basepresentation

import androidx.core.util.PatternsCompat

fun isEmailValid(value: String?): Boolean {
    val emailValue = value?.trim()
    if (emailValue.isNullOrBlank()) return false
    return PatternsCompat.EMAIL_ADDRESS.matcher(emailValue).matches()
}