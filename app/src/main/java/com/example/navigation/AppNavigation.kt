package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.home.HomeScreen
import com.example.ui.apps.AppsScreen
import com.example.ui.files.FilesScreen
import com.example.ui.devices.DevicesScreen
import com.example.ui.history.HistoryScreen
import com.example.ui.transfer.TransferScreen
import com.example.ui.discovery.DiscoveryScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSend = { navController.navigate(Screen.Send.route) },
                onNavigateToReceive = { navController.navigate(Screen.Receive.route) },
                onNavigateToApps = { navController.navigate(Screen.Apps.route) },
                onNavigateToFiles = { navController.navigate(Screen.Files.route) },
                onNavigateToDevices = { navController.navigate(Screen.Devices.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }
        composable(Screen.Send.route) {
            DiscoveryScreen(isSender = true, onBack = { navController.popBackStack() })
        }
        composable(Screen.Receive.route) {
            DiscoveryScreen(isSender = false, onBack = { navController.popBackStack() })
        }
        composable(Screen.Apps.route) {
            AppsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Files.route) {
            FilesScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Devices.route) {
            DevicesScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.History.route) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Transfer.route) {
            TransferScreen(onBack = { navController.popBackStack() })
        }
    }
}
