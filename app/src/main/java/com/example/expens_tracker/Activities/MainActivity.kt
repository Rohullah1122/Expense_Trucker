package com.example.expens_tracker.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expens_tracker.Classes.Routes
import com.example.expens_tracker.Files.*
import com.example.expens_tracker.Components.MonthlyActivityScreen
import com.example.expens_tracker.Data.SettingsProvider
import com.example.expens_tracker.ui.theme.Expens_trackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val context = LocalContext.current

            // DataStore provider
            val settingsProvider = remember { SettingsProvider(context) }

            // Dark mode state
            val isDarkMode by settingsProvider.darkModeFlow.collectAsState(initial = false)

            Expens_trackerTheme(darkTheme = isDarkMode) {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.LOGIN
                ) {
                    // Auth Screens
                    composable(Routes.LOGIN) { LoginUI(navController) }
                    composable(Routes.REGISTER) { RegisterUI(navController) }
                    composable(Routes.FORGOT_PASSWORD) { ForgotPasswordUI(navController) }

                    // Main App Layout
                    composable(Routes.MainLayout) { MainLayout(navController) }

                    // Monthly Activity Screen (backend version)
                    composable(Routes.ThismonthTransaction) {
                        MonthlyActivityScreen(navController)
                    }

                    // Month Summary Screen
                    composable(
                        route = "month_summary/{monthName}",
                        arguments = listOf(navArgument("monthName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val month = backStackEntry.arguments?.getString("monthName") ?: ""
                        MonthSummaryScreen(navController, month)
                    }
                }
            }
        }
    }
}
