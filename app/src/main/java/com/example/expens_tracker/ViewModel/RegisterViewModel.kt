package com.example.expens_tracker.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expens_tracker.API.ApiClient
import com.example.expens_tracker.API.ApiService
import com.example.expens_tracker.API.Response.UserResponse
import com.example.expens_tracker.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val api = ApiClient.instance.create(ApiService::class.java)
    private val repo = UserRepository(api)

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(name: String, email: String, password: String) {
        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                val response = repo.register(name, email, password)

                if (response.isSuccessful && response.body() != null) {
                    _registerState.value = RegisterState.Success(response.body()!!)
                } else {
                    _registerState.value = RegisterState.Error("Registration failed")
                }

            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: UserResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
