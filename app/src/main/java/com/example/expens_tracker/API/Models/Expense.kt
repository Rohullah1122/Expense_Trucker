package com.example.expens_tracker.API.Models

data class Expense(
    val id: Int,
    val user_id: Int,
    val category_id: Int,
    val amount: Double,
    val description: String,
    val expense_date: String,
    val category_name: String?
)
