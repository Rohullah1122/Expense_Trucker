package com.example.expens_tracker.API

import com.example.expens_tracker.API.Models.Expense
import com.example.expens_tracker.API.Response.ForgotPasswordRequest
import com.example.expens_tracker.API.Response.LoginRequest
import com.example.expens_tracker.API.Response.RegisterRequest
import com.example.expens_tracker.API.Response.ResetPasswordRequest
import com.example.expens_tracker.API.Response.UserResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // REGISTER
    @POST("users/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    // LOGIN
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<UserResponse>

    // FORGOT PASSWORD
    @POST("users/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Map<String, String>>

    // RESET PASSWORD
    @POST("users/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Map<String, String>>


    // GET EXPENSES BY USER (THE ONLY ONE USER NEEDS)
    @GET("expenses/user/{id}")
    suspend fun getExpensesByUser(@Path("id") userId: Int): Response<List<Expense>>
}

