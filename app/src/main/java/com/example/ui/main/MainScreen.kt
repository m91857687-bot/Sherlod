package com.example.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigation.Screen
import com.example.ui.home.HomeScreen
import com.example.ui.apps.AppsScreen
import com.example.ui.files.FilesScreen
import com.example.ui.devices.DevicesScreen
import com.example.ui.history.HistoryScreen
import com.example.ui.transfer.TransferScreen
import com.example.ui.discovery.DiscoveryScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.pc.ShareToPCScreen
import com.example.ui.qr.QRScannerScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem("transfer", "النقل", Icons.Default.SwapHoriz),
        BottomNavItem("files_tab", "الملفات", Icons.Default.Folder),
        BottomNavItem("profile", "حسابي", Icons.Default.Person)
    )

    // Show bottom bar only on top-level routes
    val showBottomBar = items.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "transfer",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("transfer") {
                HomeScreen(
                    onNavigateToSend = { navController.navigate(Screen.Send.route) },
                    onNavigateToReceive = { navController.navigate(Screen.Receive.route) },
                    onNavigateToApps = { navController.navigate(Screen.Apps.route) },
                    onNavigateToFiles = { navController.navigate(Screen.Files.route) },
                    onNavigateToDevices = { navController.navigate(Screen.Devices.route) },
                    onNavigateToHistory = { navController.navigate(Screen.History.route) },
                    onNavigateToSharePC = { navController.navigate(Screen.ShareToPC.route) },
                    onNavigateToQRScanner = { navController.navigate(Screen.QRScanner.route) }
                )
            }
            composable("files_tab") {
                FilesScreen(onBack = { navController.popBackStack() })
            }
            composable("profile") {
                ProfileScreen()
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
            composable(Screen.ShareToPC.route) {
                ShareToPCScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.QRScanner.route) {
                QRScannerScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
