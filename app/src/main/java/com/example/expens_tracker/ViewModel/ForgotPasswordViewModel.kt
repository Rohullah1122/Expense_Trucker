package com.example.expens_tracker.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expens_tracker.API.ApiClient
import com.example.expens_tracker.API.ApiService
import com.example.expens_tracker.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {

    private val api = ApiClient.instance.create(ApiService::class.java)
    private val repo = UserRepository(api)

    private val _state = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val state: StateFlow<ForgotPasswordState> = _state

    fun sendResetEmail(email: String) {
        _state.value = ForgotPasswordState.Loading

        viewModelScope.launch {
            try {
                val response = repo.forgotPassword(email)

                if (response.isSuccessful) {
                    _state.value = ForgotPasswordState.Success("Reset link sent to your email")
                } else {
                    _state.value = ForgotPasswordState.Error("Failed to send reset email")
                }

            } catch (e: Exception) {
                _state.value = ForgotPasswordState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    data class Success(val message: String) : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
}
