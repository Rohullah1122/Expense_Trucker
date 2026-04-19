package com.example.expens_tracker.Components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expens_tracker.ui.theme.AppTheme

// Make sure these routes match your Routes.kt object!
data class NavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun MainBottomNavBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    val items = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("Categories", Icons.Default.Category, "categories"),
        NavItem("Trends", Icons.Default.Assessment, "trends"),
        // 👈 The New AI Section
        NavItem("AI Coach", Icons.Default.AutoAwesome, "ai_insights"),
         NavItem("Settings", Icons.Default.Settings, "settings")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = AppTheme.dimens.small
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(if (item.label == "AI Coach") 28.dp else 24.dp) // Make AI slightly larger
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            )
        }
    }
}