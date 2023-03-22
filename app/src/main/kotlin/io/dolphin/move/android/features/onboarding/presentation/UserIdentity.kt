package io.dolphin.move.android.features.onboarding.presentation

import androidx.annotation.StringRes
import io.dolphin.move.android.R

private const val MALE = "male"
private const val FEMALE = "female"
private const val DIVERSE = "diverse"

enum class UserIdentity(
    @StringRes val textId: Int,
    val type: String,
) {
    MRS(R.string.lbl_mrs, "female"),
    MR(R.string.lbl_mr, "male"),
    NONBINARY(R.string.lbl_nonbinary, "diverse"),
    NONE(0, "none");

    companion object {
        fun getIdentityEnum(type: String?): UserIdentity {
            return when (type) {
                FEMALE -> MRS
                MALE -> MR
                DIVERSE -> NONBINARY
                else -> NONE
            }
        }
    }
}
