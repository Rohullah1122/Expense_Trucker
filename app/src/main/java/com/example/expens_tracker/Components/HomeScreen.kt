package com.example.expens_tracker.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Correct import for List-based items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expens_tracker.Files.*
import com.example.expens_tracker.ui.theme.AppTheme
import com.example.expens_tracker.Components.CategoryDonutChart
import com.example.expens_tracker.ViewModel.ExpenseViewModel

//import com.example.expens_tracker.Components.
 // Ensure this class is exactly as expected

//work on android studio project make layout first

@Composable
fun HomeScreen(navController: NavController) {
    val dimens = AppTheme.dimens

    // 1. Get ViewModel
    val viewModel: ExpenseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    // 2. Collect state
    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // TODO: Replace with DataStore userId later
    val userId = 1  // temporary hardcoded user ID

    // 3. Load expenses when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadUserExpenses(userId)
    }

    // 4. Calculate category totals dynamically
    val categoryTotals = remember(expenses) {
        expenses.groupBy { it.category_name ?: "Other" }
            .mapValues { entry ->
                entry.value.sumOf { it.amount }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimens.medium)
    ) {
        item { HomeHeader() }

        // Show loading state
        if (loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimens.large),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        // Donut chart
        item {
            SummaryCard(content = {
                CategoryDonutChart(categoryTotals = categoryTotals)
            })
        }

        item { Spacer(modifier = Modifier.height(dimens.large)) }

        // Recent transactions
        item {
            TransactionListSection(
                navController = navController,
                transactions = expenses.take(5),   // REAL DATA
                fullListForNavigation = expenses,  // REAL DATA
                title = "Recent Activity"
            )
        }
    }
}
