package com.mealplanner1234.app.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mealplanner1234.app.ui.detail.InvoiceDetailScreen
import com.mealplanner1234.app.ui.form.InvoiceFormScreen
import com.mealplanner1234.app.ui.home.HomeScreen
import com.mealplanner1234.app.ui.settings.PrivacyPolicyScreen
import com.mealplanner1234.app.ui.settings.SettingsScreen

private const val ROUTE_HOME = "home"
private const val ROUTE_SETTINGS = "settings"
private const val ROUTE_PRIVACY = "privacy"
private const val ROUTE_FORM = "form"
private const val ROUTE_DETAIL = "detail/{invoiceId}"

@Composable
fun InvoicesApp(viewModel: InvoiceViewModel) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val showBottomBar = route == ROUTE_HOME || route == ROUTE_SETTINGS

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    NavigationBarItem(
                        selected = route == ROUTE_HOME,
                        onClick = { navController.navigateTab(ROUTE_HOME) },
                        icon = { Icon(Icons.Outlined.ReceiptLong, contentDescription = "Invoices") },
                        label = { Text("Invoices") }
                    )
                    NavigationBarItem(
                        selected = route == ROUTE_SETTINGS,
                        onClick = { navController.navigateTab(ROUTE_SETTINGS) },
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(ROUTE_HOME) {
                HomeScreen(
                    viewModel = viewModel,
                    onAdd = { navController.navigate(ROUTE_FORM) },
                    onOpen = { id -> navController.navigate("detail/$id") }
                )
            }
            composable(ROUTE_SETTINGS) {
                SettingsScreen(
                    viewModel = viewModel,
                    onPrivacyPolicy = { navController.navigate(ROUTE_PRIVACY) }
                )
            }
            composable(ROUTE_PRIVACY) {
                PrivacyPolicyScreen(onBack = { navController.popBackStack() })
            }
            composable(ROUTE_FORM) {
                InvoiceFormScreen(
                    viewModel = viewModel,
                    invoiceId = null,
                    onDone = { navController.popBackStack() }
                )
            }
            composable(
                route = "form/{invoiceId}",
                arguments = listOf(navArgument("invoiceId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("invoiceId")
                InvoiceFormScreen(
                    viewModel = viewModel,
                    invoiceId = id,
                    onDone = { navController.popBackStack() }
                )
            }
            composable(
                route = ROUTE_DETAIL,
                arguments = listOf(navArgument("invoiceId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("invoiceId") ?: return@composable
                InvoiceDetailScreen(
                    viewModel = viewModel,
                    invoiceId = id,
                    onEdit = { editId -> navController.navigate("form/$editId") },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

private fun androidx.navigation.NavHostController.navigateTab(target: String) {
    navigate(target) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
