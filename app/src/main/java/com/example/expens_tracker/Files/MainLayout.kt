package com.example.expens_tracker.Files

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.expens_tracker.Components.HomeScreen
import com.example.expens_tracker.Components.MainBottomNavBar
import com.example.expens_tracker.ui.theme.AppTheme
// 1. Add this import at the top
import androidx.compose.ui.platform.LocalContext

@Composable
fun MainLayout(navController: NavController) {
    var currentRoute by remember { mutableStateOf("home") }
    val dimens = AppTheme.dimens

    // 2. Grab the context right here
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            MainBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { currentRoute = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentRoute) {
                "home" -> HomeScreen(navController)
                "categories" -> CategoryScreen(navController)
                "trends" -> TrendsScreen(navController)
                "ai_insights" -> AIScreen(navController)
                // 3. Pass the context to SettingsScreen
                "settings" -> SettingsScreen(navController)
                else -> Box(modifier = Modifier.padding(dimens.medium)) { /* Placeholder */ }
            }
        }
    }
}

