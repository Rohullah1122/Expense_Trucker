package com.example.expens_tracker.Files

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expens_tracker.Components.SummaryCard
import com.example.expens_tracker.Components.TrendBarChart
import com.example.expens_tracker.Components.TrendStatCard
import com.example.expens_tracker.ViewModel.ExpenseViewModel

@Composable
fun TrendsScreen(navController: NavController) {

    // Load backend expenses
    val viewModel: ExpenseViewModel = viewModel()
    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserExpenses(1)
    }

    // -----------------------------
    // 1. Group expenses by month
    // -----------------------------
    val monthlyData = remember(expenses) {
        expenses
            .groupBy { it.expense_date.substring(0, 7) } // "2026-03"
            .toSortedMap()
            .map { (monthKey, list) ->

                val monthNumber = monthKey.split("-")[1]
                val monthLabel = when (monthNumber) {
                    "01" -> "Jan"
                    "02" -> "Feb"
                    "03" -> "Mar"
                    "04" -> "Apr"
                    "05" -> "May"
                    "06" -> "Jun"
                    "07" -> "Jul"
                    "08" -> "Aug"
                    "09" -> "Sep"
                    "10" -> "Oct"
                    "11" -> "Nov"
                    "12" -> "Dec"
                    else -> monthKey
                }

                val total = list.sumOf { it.amount }
                monthLabel to total
            }
    }

    // -----------------------------
    // 2. Stats
    // -----------------------------
    val highestMonth = monthlyData.maxByOrNull { it.second }
    val lowestMonth = monthlyData.minByOrNull { it.second }
    val grandTotal = monthlyData.sumOf { it.second }

    val highestMonthLabel = highestMonth?.first ?: "N/A"
    val lowestMonthLabel = lowestMonth?.first ?: "N/A"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        item {
            Text(
                text = "Trends",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // -----------------------------
        // 3. Bar Chart
        // -----------------------------
        item {
            SummaryCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (loading) {
                        CircularProgressIndicator()
                    } else {
                        TrendBarChart(
                            monthlyTotals = monthlyData,
                            onMonthClick = { selectedMonth ->
                                navController.navigate("month_summary/$selectedMonth")
                            }
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // -----------------------------
        // 4. Highest & Lowest Spending
        // -----------------------------
        item {
            TrendStatCard(
                title = "Highest Spending",
                amount = String.format("%.2f", highestMonth?.second ?: 0.0),
                dateRange = "Peak Month: $highestMonthLabel",
                onClick = {
                    if (highestMonthLabel != "N/A") {
                        navController.navigate("month_summary/$highestMonthLabel")
                    }
                }
            )

            TrendStatCard(
                title = "Lowest Spending",
                amount = String.format("%.2f", lowestMonth?.second ?: 0.0),
                dateRange = "Lowest Month: $lowestMonthLabel",
                onClick = {
                    if (lowestMonthLabel != "N/A") {
                        navController.navigate("month_summary/$lowestMonthLabel")
                    }
                }
            )
        }

        // -----------------------------
        // 5. Grand Total
        // -----------------------------
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Total Spending (All Months)")
                    Text(
                        text = "$${String.format("%.2f", grandTotal)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
