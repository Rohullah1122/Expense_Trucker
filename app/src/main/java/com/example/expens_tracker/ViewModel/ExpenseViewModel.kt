package com.example.expens_tracker.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expens_tracker.API.ApiClient
import com.example.expens_tracker.API.ApiService
import com.example.expens_tracker.API.Models.Expense
import com.example.expens_tracker.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel : ViewModel() {

    private val api = ApiClient.instance.create(ApiService::class.java)
    private val repo = ExpenseRepository(api)

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadUserExpenses(userId: Int) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = repo.getExpensesByUser(userId)

                if (response.isSuccessful && response.body() != null) {
                    _expenses.value = response.body()!!
                }

            } catch (e: Exception) {
                // handle error
            }

            _loading.value = false
        }
    }
}
