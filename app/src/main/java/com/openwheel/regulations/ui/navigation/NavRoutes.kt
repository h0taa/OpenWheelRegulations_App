package com.openwheel.regulations.ui.navigation

/** All navigation destinations. */
sealed class NavRoutes(val route: String) {

    data object Splash : NavRoutes("splash")
    data object Home : NavRoutes("home")
    data object Bookmarks : NavRoutes("bookmarks")
    data object Settings : NavRoutes("settings")
    data object About : NavRoutes("about")

    data object Detail : NavRoutes("detail/{year}") {
        const val ARG_YEAR = "year"
        fun create(year: Int) = "detail/$year"
    }
}
