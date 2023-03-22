package io.dolphin.move.android.ui.navigation

import androidx.navigation.NavController
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.features.changepassword.presentation.ChangePasswordRouter
import io.dolphin.move.android.features.deleteaccount.presentation.DeleteAccountRouter
import io.dolphin.move.android.features.forgotpassword.presentation.ForgotPasswordRouter
import io.dolphin.move.android.features.login.presentation.LoginRouter
import io.dolphin.move.android.features.messages.presentation.MessagesRouter
import io.dolphin.move.android.features.onboarding.presentation.OnboardingRouter
import io.dolphin.move.android.features.permissions.presentation.StatusRouter
import io.dolphin.move.android.features.profile.presentation.ProfileRouter
import io.dolphin.move.android.features.timeline.presentation.TimelineRouter
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsRouter
import io.dolphin.move.android.features.webview.presentation.WebRouter
import io.dolphin.move.android.ui.navigation.Routes.ChangePassword
import io.dolphin.move.android.ui.navigation.Routes.Dashboard
import io.dolphin.move.android.ui.navigation.Routes.DeleteAccount
import io.dolphin.move.android.ui.navigation.Routes.ForgotPassword
import io.dolphin.move.android.ui.navigation.Routes.Login
import io.dolphin.move.android.ui.navigation.Routes.Onboarding
import io.dolphin.move.android.ui.navigation.Routes.Profile
import io.dolphin.move.android.ui.navigation.Routes.Splash
import io.dolphin.move.android.ui.navigation.Routes.Web
import io.dolphin.move.android.ui.navigation.Routes.replaceNullable

interface MainRouter : OnboardingRouter,
    WebRouter,
    LoginRouter,
    ForgotPasswordRouter,
    StatusRouter,
    StartRouter,
    TimelineRouter,
    TripDetailsRouter,
    ProfileRouter,
    ChangePasswordRouter,
    DeleteAccountRouter,
    MessagesRouter

class MainRouterImpl(
    private val navController: NavController,
) : MainRouter {

    override fun showOnboardingScreen() {
        navController.navigate(Onboarding.route)
    }

    override fun showAgreementInApp(agreement: Agreement) {
        val route = Web.getFullRoute()
            .replaceNullable("{${Web.Args.agreement}}", agreement.name)
            .replaceNullable("{${Web.Args.url}}", null)
        navController.navigate(route)
    }

    override fun showUrlInApp(url: String) {
        val route = Web.getFullRoute()
            .replaceNullable("{${Web.Args.agreement}}", null)
            .replaceNullable("{${Web.Args.url}}", url)
        navController.navigate(route)
    }

    override fun showLoginScreen() {
        navController.navigate(Login.route)
    }

    override fun backToLoginScreen() {
        navController.popBackStack(Login.route, false)
    }

    override fun showLoginScreenAtStart() {
        navController.popBackStack(Splash, true)
        navController.navigate(Login.route)
    }

    override fun showLoginScreenAtLogout() {
        navController.navigate(Login.route) {
            // Clear all backstack
            popUpTo(0)
        }
    }

    override fun showForgotPasswordScreen() {
        navController.navigate(ForgotPassword.route)
    }

    override fun showTimelineScreen() {
        navController.navigate(Routes.TimelineRoot.Timeline.route)
    }

    override fun showStatusScreen() {
        navController.navigate(Dashboard.route) {
            // If we are on the dashboard the next 'back' should leave the app.
            popUpTo(0)
        }
    }

    override fun showStartScreen(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            showStatusScreen()
        } else {
            showLoginScreenAtStart()
        }
    }

    override fun showTimelineScreen(tripId: Long) {
        navController.navigate(
            Routes.TimelineRoot.TripDetails.getFullRoute()
                .replace("{${Routes.TimelineRoot.TripDetails.Args.tripId}}", tripId.toString()),
        )
    }

    override fun showProfileScreen() {
        navController.navigate(Profile.route)
    }

    override fun showChangePasswordScreen() {
        navController.navigate(ChangePassword.route)
    }

    override fun showDeleteAccountScreen() {
        navController.navigate(DeleteAccount.route)
    }

    override fun showMessagesScreen() {
        if (navController.backQueue.any { it.destination.route == Routes.MessagesRoot.Messages.route }) {
            navController.popBackStack(Routes.MessagesRoot.Messages.route, false)
        } else {
            navController.navigate(Routes.MessagesRoot.Messages.route)
        }
    }

    override fun showMessageDetailsScreen(id: Long) {
        navController.navigate(
            Routes.MessagesRoot.MessageDetails.getFullRoute()
                .replace("{${Routes.MessagesRoot.MessageDetails.Args.messageId}}", id.toString()),
        )
    }
}

