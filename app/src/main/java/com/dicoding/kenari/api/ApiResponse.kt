package com.dicoding.kenari.api

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "message")
    val message: String? = null,

    @Json(name = "data")
    val data: LoginData? = null
)

data class RegisterResponse(
    @Json(name = "message")
    val message: String? = null,
)

data class LoginData(
    @Json(name = "token")
    val token: String? = null
)

