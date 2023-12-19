package com.dicoding.kenari.api

import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body
import com.dicoding.kenari.api.LoginResponse

data class LoginRequest(
    val email: String,
    val password: String
)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

interface ApiService {

    @POST("auth/signin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

}