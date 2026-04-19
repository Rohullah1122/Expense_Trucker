package com.example.expens_tracker.Components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expens_tracker.API.Models.Expense
import com.example.expens_tracker.ViewModel.ExpenseViewModel
import com.example.expens_tracker.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MonthlyActivityScreen(navController: NavController) {

    val viewModel: ExpenseViewModel = viewModel()
    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Load expenses for the logged-in user
    LaunchedEffect(Unit) {
        viewModel.loadUserExpenses(1)
    }

    // Filter for current month (example: April 2026)
    val currentMonth = "2026-04"
    val filteredExpenses = expenses.filter { it.expense_date.startsWith(currentMonth) }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "This Month's Activity",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            TransactionListSection(
                navController = navController,
                transactions = filteredExpenses,
                fullListForNavigation = filteredExpenses,
                title = "This Month"
            )
        }
    }
}



@Composable
fun MonthlySummaryHeader(expenses: List<Expense>) {
    val totalExpenses = expenses.sumOf { it.amount }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Income", style = MaterialTheme.typography.labelSmall)
                Text("$4,500.00", fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Expenses", style = MaterialTheme.typography.labelSmall)
                Text(
                    "$${String.format("%.2f", totalExpenses)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
