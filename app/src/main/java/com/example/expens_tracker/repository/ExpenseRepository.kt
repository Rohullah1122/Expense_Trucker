package com.example.expens_tracker.repository

import com.example.expens_tracker.API.ApiService
import com.example.expens_tracker.API.Models.Expense

class ExpenseRepository(private val api: ApiService) {

    suspend fun getExpensesByUser(userId: Int) =
        api.getExpensesByUser(userId)
}
