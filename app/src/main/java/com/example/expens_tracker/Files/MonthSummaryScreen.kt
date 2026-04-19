package com.example.expens_tracker.Files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expens_tracker.Components.TransactionListSection
import com.example.expens_tracker.ViewModel.ExpenseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MonthSummaryScreen(navController: NavController, monthName: String) {
    val viewModel: ExpenseViewModel = viewModel()

    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserExpenses(1)
    }

    // Filter by month (backend date format: YYYY-MM-DD)
    val filteredExpenses = remember(expenses) {
        expenses.filter { it.expense_date.contains(monthName, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "$monthName Summary",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            item {
                TransactionListSection(
                    navController = navController,
                    transactions = filteredExpenses,
                    fullListForNavigation = filteredExpenses,
                    title = "Spending Breakdown"
                )
            }

            item {
                val total = filteredExpenses.sumOf { it.amount }
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total for $monthName", style = MaterialTheme.typography.labelLarge)
                        Text(
                            text = "$${String.format("%.2f", total)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
