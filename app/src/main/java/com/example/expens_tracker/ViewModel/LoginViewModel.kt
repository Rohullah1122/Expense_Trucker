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


class LoginViewModel : ViewModel() {

    private val api = ApiClient.instance.create(ApiService::class.java)
    private val repo = UserRepository(api)

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val response = repo.login(email, password)

                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()!!
                    _loginState.value = LoginState.Success(userResponse)
                } else {
                    _loginState.value = LoginState.Error("Invalid email or password")
                }

            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }
}


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: UserResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}

