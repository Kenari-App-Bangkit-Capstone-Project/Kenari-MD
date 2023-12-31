package com.dicoding.kenari.api

import com.squareup.moshi.Json

// Login Response
data class LoginResponse(
    @Json(name = "message")
    val message: String? = null,

    @Json(name = "data")
    val data: LoginData? = null
)

data class LoginData(
    @Json(name = "token")
    val token: String? = null,
    @Json(name = "user")
    val user: UserData? = null
)

data class UserData(
    @Json(name = "user_id")
    val userId: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "email")
    val email: String? = null,

    @Json(name = "label")
    val label: String? = null,

    @Json(name = "address")
    val address: String? = null,

    @Json(name = "university")
    val university: String? = null,

    @Json(name = "personality")
    val personality: String? = null
)


// Register Response
data class RegisterResponse(
    @Json(name = "message")
    val message: String? = null,
)