package com.example.expens_tracker.Classes

import com.example.expens_tracker.API.Models.Expense
import com.example.expens_tracker.R
import kotlin.math.roundToInt

object FinancialAI {

    data class AIAnalysis(
        val totalSpent: Double,
        val topCategory: String,
        val savingPotential: Double,
        val healthScore: Int,
        val warningResIds: List<Int>
    )

    fun analyzeExpenses(expenses: List<Expense>): AIAnalysis {
        if (expenses.isEmpty()) {
            return AIAnalysis(
                totalSpent = 0.0,
                topCategory = "None",
                savingPotential = 0.0,
                healthScore = 100,
                warningResIds = emptyList()
            )
        }

        val total = expenses.sumOf { it.amount }

        val categoryTotals = expenses.groupBy { it.category_name ?: "Other" }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val topCategory = categoryTotals.maxByOrNull { it.value }?.key ?: "Other"
        val topAmount = categoryTotals[topCategory] ?: 0.0

        val foodSpend = categoryTotals["Food & Drink"] ?: 0.0
        val shoppingSpend = categoryTotals["Shopping"] ?: 0.0
        val luxurySpending = foodSpend + shoppingSpend

        val savingPotential = luxurySpending * 0.2

        val warnings = mutableListOf<Int>()
        if (total > 0 && luxurySpending > total * 0.5) {
            warnings.add(R.string.warn_high_discretionary)
        }
        if (total > 1000) {
            warnings.add(R.string.warn_limit_approach)
        }

        val healthScore = if (total == 0.0) {
            100
        } else {
            (100 - (luxurySpending / total * 100))
                .roundToInt()
                .coerceIn(0, 100)
        }

        return AIAnalysis(
            totalSpent = total,
            topCategory = topCategory,
            savingPotential = savingPotential,
            healthScore = healthScore,
            warningResIds = warnings
        )
    }
}
