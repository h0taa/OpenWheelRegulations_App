package com.openwheel.regulations.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.openwheel.regulations.ui.about.AboutScreen
import com.openwheel.regulations.ui.bookmarks.BookmarksScreen
import com.openwheel.regulations.ui.detail.DetailScreen
import com.openwheel.regulations.ui.home.HomeScreen
import com.openwheel.regulations.ui.settings.SettingsScreen
import com.openwheel.regulations.ui.splash.SplashScreen

/**
 * Root navigation graph.
 *
 * The whole graph is wrapped in a [SharedTransitionLayout] so the Home year-number
 * card can morph into the Detail hero via a shared element. Each screen receives the
 * [SharedTransitionScope] (from the layout) and its own [AnimatedContentScope] (from
 * the `composable` builder) so it can register shared bounds.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Splash.route
        ) {
            composable(NavRoutes.Splash.route) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(NavRoutes.Home.route) {
                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(NavRoutes.Home.route) {
                HomeScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    onYearClick = { year -> navController.navigate(NavRoutes.Detail.create(year)) },
                    onBookmarksClick = { navController.navigate(NavRoutes.Bookmarks.route) },
                    onSettingsClick = { navController.navigate(NavRoutes.Settings.route) }
                )
            }

            composable(
                route = NavRoutes.Detail.route,
                arguments = listOf(navArgument(NavRoutes.Detail.ARG_YEAR) {
                    type = NavType.IntType
                })
            ) {
                DetailScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(NavRoutes.Bookmarks.route) {
                BookmarksScreen(
                    onYearClick = { year -> navController.navigate(NavRoutes.Detail.create(year)) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(NavRoutes.Settings.route) {
                SettingsScreen(
                    onAboutClick = { navController.navigate(NavRoutes.About.route) },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(NavRoutes.About.route) {
                AboutScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
