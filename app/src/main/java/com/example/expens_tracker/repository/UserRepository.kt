package com.example.expens_tracker.repository

import com.example.expens_tracker.API.ApiService
import com.example.expens_tracker.API.Response.LoginRequest
import com.example.expens_tracker.API.Response.RegisterRequest
import com.example.expens_tracker.API.Response.ForgotPasswordRequest
import com.example.expens_tracker.API.Response.ResetPasswordRequest

class UserRepository(private val api: ApiService) {

    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun register(name: String, email: String, password: String) =
        api.register(RegisterRequest(name, email, password))

    suspend fun forgotPassword(email: String) =
        api.forgotPassword(ForgotPasswordRequest(email))

    suspend fun resetPassword(token: String, newPassword: String) =
        api.resetPassword(ResetPasswordRequest(token, newPassword))
}
