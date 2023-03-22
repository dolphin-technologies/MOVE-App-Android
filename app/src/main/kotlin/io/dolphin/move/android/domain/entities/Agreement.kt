package io.dolphin.move.android.domain.entities

enum class Agreement(val url: String, val type: String?) {
    TERMS_OF_USE(io.dolphin.move.android.basedata.TERMS_OF_USE, "tou"),
    PRIVACY_POLICY(io.dolphin.move.android.basedata.PRIVACY_POLICY, "privacy"),
    PRIVACY_ANALYTICS(io.dolphin.move.android.basedata.PRIVACY_ANALYTICS, null),
    INFO_HELP(io.dolphin.move.android.basedata.INFO_HELP, null),
    IMPRINT_ABOUT(io.dolphin.move.android.basedata.IMPRINT_ABOUT, null),
}

sealed class Agreement2 {
    abstract val agreement: Agreement
    open val url: String = ""

    data class TermsOfUse(
        override val agreement: Agreement,
        override val url: String,
    ) : Agreement2()

    data class PrivacyPolicy(
        override val agreement: Agreement,
        override val url: String,
    ) : Agreement2()
}