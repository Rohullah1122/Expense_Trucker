package com.example.expens_tracker.Files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expens_tracker.Components.InsightCard
import com.example.expens_tracker.ViewModel.ExpenseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expens_tracker.Classes.FinancialAI
import com.example.expens_tracker.Classes.GeminiManager

@Composable
fun AIScreen(navController: NavController) {
    val viewModel: ExpenseViewModel = viewModel()

    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Load expenses for user 1 (temporary)
    LaunchedEffect(Unit) {
        viewModel.loadUserExpenses(1)
    }

    // AI feedback
    var aiFeedback by remember { mutableStateOf("Analyzing your spending...") }

    // Run AI analysis when expenses load
    LaunchedEffect(expenses) {
        if (expenses.isNotEmpty()) {
            val analysis = FinancialAI.analyzeExpenses(expenses)
            aiFeedback = GeminiManager.getFinancialAdvice(analysis)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (loading) {
            item {
                CircularProgressIndicator()
            }
        }

        item {
            InsightCard(
                title = "Gemini Pro Tip",
                suggestion = aiFeedback,
                icon = Icons.Default.AutoAwesome,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            val analysis = FinancialAI.analyzeExpenses(expenses)
            InsightCard(
                title = "Potential Savings",
                suggestion = "You spend most on ${analysis.topCategory}. You could save ${analysis.savingPotential} per month.",
                icon = Icons.Default.TrendingUp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
