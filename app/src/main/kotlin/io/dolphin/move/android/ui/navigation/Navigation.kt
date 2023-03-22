package io.dolphin.move.android.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import io.dolphin.move.android.R
import io.dolphin.move.android.basepresentation.AppEvent
import io.dolphin.move.android.basepresentation.collectAsEffect
import io.dolphin.move.android.basepresentation.components.MoveTopBar
import io.dolphin.move.android.domain.entities.Agreement
import io.dolphin.move.android.features.changepassword.presentation.ChangePasswordScreen
import io.dolphin.move.android.features.deleteaccount.presentation.DeleteAccountScreen
import io.dolphin.move.android.features.forgotpassword.presentation.ForgotPasswordScreen
import io.dolphin.move.android.features.launch.presentation.LaunchScreen
import io.dolphin.move.android.features.login.presentation.LoginScreen
import io.dolphin.move.android.features.messages.presentation.messagedetails.MessageDetailsScreen
import io.dolphin.move.android.features.messages.presentation.messages.MessagesScreen
import io.dolphin.move.android.features.more.presentation.MoreScreen
import io.dolphin.move.android.features.onboarding.presentation.OnboardingScreen
import io.dolphin.move.android.features.permissions.presentation.PermissionsScreen
import io.dolphin.move.android.features.profile.presentation.ProfileScreen
import io.dolphin.move.android.features.timeline.presentation.TimelineScreen
import io.dolphin.move.android.features.tripdetails.presentation.TripDetailsScreen
import io.dolphin.move.android.features.webview.presentation.WebScreen
import io.dolphin.move.android.ui.MainActivityViewModel
import io.dolphin.move.android.ui.MainActivityViewState
import io.dolphin.move.android.ui.navigation.Routes.ForgotPassword
import io.dolphin.move.android.ui.navigation.Routes.Login
import io.dolphin.move.android.ui.navigation.Routes.Onboarding
import io.dolphin.move.android.ui.navigation.Routes.Splash
import io.dolphin.move.android.ui.navigation.Routes.Web
import io.dolphin.move.android.ui.theme.battleship_grey
import io.dolphin.move.android.ui.theme.dusty_teal
import io.dolphin.move.android.ui.theme.dusty_teal_3
import io.dolphin.move.android.ui.theme.pale_grey
import io.dolphin.move.android.ui.theme.styleNormal

private val screenList = listOf(
    MoveScreens.Status,
    MoveScreens.Timeline,
    MoveScreens.More,
)

private val noBottomNavigationRoutes = listOf(
    Splash,
    Onboarding.route,
    Login.route,
    ForgotPassword.route,
    Web.route,
    Routes.MessagesRoot.Messages.route,
    Routes.MessagesRoot.MessageDetails.route,
)

@ExperimentalMaterial3Api
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
@Preview
fun MainNavigation(
    viewModel: MainActivityViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val router: MainRouter = MainRouterImpl(navController)
    val viewState by viewModel.mainViewState.observeAsState(MainActivityViewState())
    viewModel.appEvents.collectAsEffect { appEvent ->
        when (appEvent) {
            AppEvent.Logout -> router.showLoginScreenAtStart()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val hideNavBar = currentDestination?.hierarchy?.any {
        it.route in noBottomNavigationRoutes
    } ?: false

    Scaffold(
        topBar = {
            MoveTopBar(
                title = stringResource(viewState.titleId),
                showBack = viewState.showBackButton,
                showMessages = viewState.showMessagesIcon,
                newMessagesCount = viewState.unreadMessagesCount,
                onBack = {
                    navController.popBackStack()
                    viewModel.onBack()
                },
                onMessagesClick = router::showMessagesScreen,
            )
        },
        bottomBar = {
            if (!hideNavBar) {
                NavigationBar(
                    containerColor = pale_grey,
                ) {
                    screenList.forEach { moveScreen ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = dusty_teal,
                                selectedTextColor = dusty_teal,
                                indicatorColor = dusty_teal_3,
                                unselectedIconColor = battleship_grey,
                                unselectedTextColor = battleship_grey,
                            ),
                            icon = {
                                Box(
                                    modifier = Modifier.size(20.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        painter = painterResource(id = moveScreen.iconId),
                                        contentDescription = null,
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = stringResource(id = moveScreen.titleId),
                                    fontSize = 12.sp,
                                    style = styleNormal,
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == moveScreen.route } == true,
                            onClick = {
                                viewModel.updateTitleId(moveScreen.startScreenTitleId)
                                navController.navigate(moveScreen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = Splash,
            modifier = Modifier.padding(it)
        ) {
            composable(Splash) {
                viewModel.showMessagesIcon(false)
                LaunchScreen(
                    startRouter = router
                )
            }

            // Onboarding
            composable(Onboarding.route) {
                viewModel.updateTitleId(R.string.tit_register)
                viewModel.showBackButton(false)
                viewModel.showMessagesIcon(false)
                OnboardingScreen(
                    webRouter = router,
                    loginRouter = router,
                    dashboardRouter = router
                )
            }

            // In app web
            composable(
                route = Web.getFullRoute(),
                arguments = listOf(
                    navArgument(Web.Args.agreement) {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument(Web.Args.url) {
                        type = NavType.StringType
                        nullable = true
                    },
                )
            ) { backStackEntry ->
                val agreement =
                    backStackEntry.arguments?.getString(Web.Args.agreement)?.let(Agreement::valueOf)
                val url = backStackEntry.arguments?.getString(Web.Args.url)
                agreement?.let { agreementToShow ->
                    viewModel.updateTitleId(
                        when (agreementToShow) {
                            Agreement.TERMS_OF_USE -> R.string.tit_terms_of_use
                            Agreement.PRIVACY_POLICY -> R.string.tit_data_privacy
                            Agreement.PRIVACY_ANALYTICS -> R.string.tit_additional_analytics
                            Agreement.INFO_HELP -> R.string.tit_infos_help
                            Agreement.IMPRINT_ABOUT -> R.string.tit_imprint_contact
                        }
                    )
                    viewModel.showBackButton(true)
                }

                viewModel.showMessagesIcon(false)
                WebScreen(
                    agreement = agreement,
                    directUrl = url,
                )
            }

            // Login
            composable(Login.route) {
                viewModel.updateTitleId(R.string.tit_login)
                viewModel.showMessagesIcon(false)
                LoginScreen(
                    webRouter = router,
                    onboardingRouter = router,
                    dashboardRouter = router,
                    forgotPasswordRouter = router,
                    timelineRouter = router
                )
                viewModel.showBackButton(false)
            }

            // Forgot password
            composable(ForgotPassword.route) {
                viewModel.updateTitleId(R.string.tit_forgot_password)
                viewModel.showMessagesIcon(false)
                ForgotPasswordScreen(
                    onSuccessChange = router::backToLoginScreen,
                )
            }

            // All routes of the Timeline tree must be in a nested navigation graph
            navigation(
                route = Routes.TimelineRoot.route,
                startDestination = Routes.TimelineRoot.Timeline.route,
            ) {
                // Timeline
                composable(Routes.TimelineRoot.Timeline.route) {
                    viewModel.updateTitleId(R.string.tit_timeline)
                    viewModel.showBackButton(false)
                    viewModel.showMessagesIcon(true)
                    TimelineScreen(tripDetailsRouter = router, statusRouter = router)
                }

                // Trip details
                composable(
                    Routes.TimelineRoot.TripDetails.getFullRoute(),
                    arguments = listOf(
                        navArgument(Routes.TimelineRoot.TripDetails.Args.tripId) {
                            type = NavType.LongType
                            nullable = false
                        },
                    ),
                ) {
                    viewModel.updateTitleId(R.string.tit_trip_details)
                    viewModel.showBackButton(true)
                    viewModel.showMessagesIcon(true)
                    TripDetailsScreen()
                }
            }

            // Permission
            composable(Routes.Dashboard.route) {
                viewModel.showBackButton(false)
                viewModel.showMessagesIcon(true)
                viewModel.updateTitleId(R.string.tit_move)
                PermissionsScreen()
            }

            // More Screen
            composable(Routes.More.route) {
                viewModel.updateTitleId(R.string.tit_more)
                MoreScreen(
                    profileRouter = router,
                    webRouter = router
                )
                viewModel.showMessagesIcon(true)
                viewModel.showBackButton(false)
            }

            // All routes of the Messages tree must be in a nested navigation graph
            navigation(
                route = Routes.MessagesRoot.route,
                startDestination = Routes.MessagesRoot.Messages.route,
            ) {
                // Messages
                composable(
                    route = Routes.MessagesRoot.Messages.route,
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern =
                                "${DEEP_LINK_ROOT}/${Routes.MessagesRoot.Messages.route}"
                        },
                    )
                ) {
                    viewModel.updateTitleId(R.string.tit_messages)
                    viewModel.showBackButton(true)
                    viewModel.showMessagesIcon(true)
                    MessagesScreen(
                        messagesRouter = router,
                    )
                }

                // Message details
                composable(
                    Routes.MessagesRoot.MessageDetails.getFullRoute(),
                    arguments = listOf(
                        navArgument(Routes.MessagesRoot.MessageDetails.Args.messageId) {
                            type = NavType.LongType
                            nullable = false
                        },
                    ),
                ) {
                    viewModel.showBackButton(true)
                    viewModel.showMessagesIcon(true)
                    MessageDetailsScreen {
                        navController.popBackStack(Routes.MessagesRoot.Messages.route, false)
                    }
                }
            }

            composable(Routes.Profile.route) {
                viewModel.updateTitleId(R.string.tit_my_profile)
                ProfileScreen(
                    changePasswordRouter = router,
                    deleteAccountRouter = router,
                    loginRouter = router
                )
                viewModel.showBackButton(true)
            }

            composable(Routes.ChangePassword.route) {
                viewModel.updateTitleId(R.string.tit_change_password)
                ChangePasswordScreen(
                    loginRouter = router
                )
                viewModel.showBackButton(true)
            }

            composable(Routes.DeleteAccount.route) {
                viewModel.updateTitleId(R.string.tit_delete_account)
                DeleteAccountScreen(
                    loginRouter = router
                )
                viewModel.showBackButton(true)
            }
        }
    }
}
