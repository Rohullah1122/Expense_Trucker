package com.example.expens_tracker.API.Response

data class UserResponse(
    val id: Int,
    val name: String?,
    val email: String,
    val message: String?
)



data class LoginRequest(
    val email: String,
    val password: String
)



data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)


data class ForgotPasswordRequest(
    val email: String
)


data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)


