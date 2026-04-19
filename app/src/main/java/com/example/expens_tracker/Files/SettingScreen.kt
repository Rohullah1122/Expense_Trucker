package com.example.expens_tracker.Files

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import kotlinx.coroutines.launch

// Internal Imports
import com.example.expens_tracker.Components.SettingsToggleItem
import com.example.expens_tracker.Components.SettingsClickableItem
import com.example.expens_tracker.Components.MonthlyBudgetDialog
import com.example.expens_tracker.Data.PdfGenerator
import com.example.expens_tracker.Data.SettingsProvider
import com.example.expens_tracker.Data.SecurityManager
import com.example.expens_tracker.ViewModel.ExpenseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    // 1. Context + Scope
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val scope = rememberCoroutineScope()

    // 2. DataStore Provider
    val settingsProvider = remember { SettingsProvider(context) }

    // 3. Collect DataStore values
    val isDarkMode by settingsProvider.darkModeFlow.collectAsState(initial = false)
    val monthlyBudget by settingsProvider.monthlyBudgetFlow.collectAsState(initial = 2000.0)
    val dailyLimit by settingsProvider.dailyLimitFlow.collectAsState(initial = 50.0)
    val biometricEnabled by settingsProvider.biometricFlow.collectAsState(initial = false)

    // 4. UI State
    var showBudgetDialog by remember { mutableStateOf(false) }

    // 5. Load real expenses for PDF export
    val viewModel: ExpenseViewModel = viewModel()
    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserExpenses(1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ---------------- SECTION: APP EXPERIENCE ----------------
            item { SectionHeader("App Experience") }

            item {
                SettingsToggleItem(
                    title = "Dark Mode",
                    icon = Icons.Default.DarkMode,
                    isChecked = isDarkMode,
                    onCheckedChange = { scope.launch { settingsProvider.toggleDarkMode(it) } }
                )
            }

            // ---------------- SECTION: FINANCIAL PLAN ----------------
            item { SectionHeader("Financial Plan") }

            item {
                SettingsClickableItem(
                    title = "Monthly Budget",
                    subtitle = "Current: $${String.format("%.2f", monthlyBudget)}",
                    icon = Icons.Default.AccountBalanceWallet,
                    onClick = { showBudgetDialog = true }
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Daily Limit: $${dailyLimit.toInt()}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Slider(
                            value = dailyLimit.toFloat(),
                            onValueChange = { scope.launch { settingsProvider.saveDailyLimit(it.toDouble()) } },
                            valueRange = 10f..500f,
                            steps = 48
                        )

                        Text(
                            "AI Coach warns you if you spend more than this in 24h.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            // ---------------- SECTION: DATA & SECURITY ----------------
            item { SectionHeader("Data & Security") }

            item {
                SettingsToggleItem(
                    title = "Biometric Lock",
                    icon = Icons.Default.Fingerprint,
                    isChecked = biometricEnabled,
                    onCheckedChange = { enable ->
                        if (enable) {
                            activity?.let {
                                SecurityManager.showBiometricPrompt(it) {
                                    scope.launch { settingsProvider.saveBiometricSetting(true) }
                                }
                            }
                        } else {
                            scope.launch { settingsProvider.saveBiometricSetting(false) }
                        }
                    }
                )
            }

            // ---------------- EXPORT PDF ----------------
            item {
                SettingsClickableItem(
                    title = "Export to PDF",
                    subtitle = "Get a professional expense report",
                    icon = Icons.Default.PictureAsPdf,
                    onClick = {
                        if (loading) {
                            Toast.makeText(context, "Loading expenses...", Toast.LENGTH_SHORT).show()
                        } else {
                            PdfGenerator.createExpensePdf(context, expenses)
                        }
                    }
                )
            }
        }
    }

    // 6. Monthly Budget Dialog
    if (showBudgetDialog) {
        MonthlyBudgetDialog(
            currentBudget = monthlyBudget,
            onDismiss = { showBudgetDialog = false },
            onSave = { newAmount ->
                scope.launch { settingsProvider.saveMonthlyBudget(newAmount) }
                showBudgetDialog = false
            }
        )
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
