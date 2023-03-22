package io.dolphin.move.android.ui.navigation

const val DEEP_LINK_ROOT = "notification://internal.move.deeplink"

object Routes {
    // Root
    const val Splash = "Splash"

    // Onboarding
    object Onboarding {
        const val route = "Onboarding"
    }

    // Login
    object Login {
        const val route = "Login"
    }

    // Dashboard
    object Dashboard {
        const val route = "Dashboard"
    }

    // Forgot password
    object ForgotPassword {
        const val route = "ForgotPassword"
    }

    // Timeline root
    object TimelineRoot {
        const val route = "TimelineRoot"

        object Timeline {
            const val route = "Timeline"
        }

        object TripDetails {
            const val route = "TripDetails"

            object Args {
                const val tripId = "tripId"
            }

            fun getFullRoute() = buildString {
                append(route)
                appendRequiredArgs(listOf(Args.tripId))
            }
        }
    }

    object Profile {
        const val route = "Profile"
    }

    object ChangePassword {
        const val route = "ChangePassword"
    }

    object DeleteAccount {
        const val route = "DeleteAccount"
    }
    object MessagesRoot {
        const val route = "MessagesRoot"

        object Messages {
            const val route = "Messages"
        }

        object MessageDetails {
            const val route = "MessageDetails"

            object Args {
                const val messageId = "messageId"
            }

            fun getFullRoute() = buildString {
                append(route)
                appendRequiredArgs(listOf(Args.messageId))
            }
        }
    }


    // In app webview
    object Web {
        const val route = "Web"

        object Args {
            const val agreement = "agreement"
            const val url = "url"
        }

        fun getFullRoute() = buildString {
            append(route)
            appendOptionalArgs(listOf(Args.agreement, Args.url))
        }
    }

    object More {
        const val route = "More"
    }

    fun String.replaceNullable(oldValue: String, newValue: String?): String {
        return if (newValue == null) {
            this
        } else {
            replace(oldValue, newValue)
        }
    }

    private fun StringBuilder.appendRequiredArgs(listOfRequiredArgs: List<String>) {
        listOfRequiredArgs.forEach {
            append("/")
            append("{$it}")
        }
    }

    private fun StringBuilder.appendOptionalArgs(listOfOptionalArgs: List<String>) {
        append("?")
        listOfOptionalArgs.forEachIndexed { index, s ->
            if (index > 0) append("&")
            append("$s={$s}")
        }
    }
}