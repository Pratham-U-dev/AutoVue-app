package com.example.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Analytics : Screen("analytics")
    data object Diagnostics : Screen("diagnostics")
    data object Settings : Screen("settings")
}
