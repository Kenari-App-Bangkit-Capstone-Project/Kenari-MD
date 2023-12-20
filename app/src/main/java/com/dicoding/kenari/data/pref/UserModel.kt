package com.dicoding.kenari.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val userId: String,
    val name: String,
    val label: String = "",
    val address: String = "",
    val university: String = "",
    val personality: String = "",
    val isLogin: Boolean = false
)