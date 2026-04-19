package com.example.expens_tracker.Files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expens_tracker.Components.*
import com.example.expens_tracker.ViewModel.ExpenseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CategoryScreen(navController: NavController) {
    val viewModel: ExpenseViewModel = viewModel()

    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserExpenses(1)
    }

    // Group by backend category_name
    val categoryTotals = remember(expenses) {
        expenses.groupBy { it.category_name ?: "Other" }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    val totalSpending = categoryTotals.values.sum()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            MonthSelector(
                currentMonth = "April 2026",
                onPrevious = {},
                onNext = {}
            )
        }

        item {
            SummaryCard {
                CategoryDonutChart(
                    categoryTotals = categoryTotals,
                    modifier = Modifier.size(220.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Spending by Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(categoryTotals.toList()) { (category, amount) ->
            CategoryProgressRow(
                category = category,
                amount = amount,
                totalSpending = totalSpending
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
