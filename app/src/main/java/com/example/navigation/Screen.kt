package com.example.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Receive : Screen("receive")
    object Send : Screen("send")
    object Apps : Screen("apps")
    object Files : Screen("files")
    object Devices : Screen("devices")
    object History : Screen("history")
    object Transfer : Screen("transfer")
}
