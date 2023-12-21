package com.dicoding.kenari.api

import com.squareup.moshi.Json
import java.util.Date

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

// Chatbot History Response
data class ChatbotHistoryResponse(
    @Json(name = "message")
    val message: String? = null,

    @Json(name = "data")
    val data: ChatbotHistoryData? = null
)

data class ChatbotHistoryData(
    @Json(name = "chatHistories")
    val chatHistories: List<ChatbotHistory>? = null
)

data class ChatbotHistory(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "user_id")
    val user_id: String? = null,

    @Json(name = "user_input")
    val user_input : String? = null,

    @Json(name = "response")
    val response: String? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "createdAt")
    val createdAt: Date? = null
)

//  ML Chatbot Response
data class ModelResponse(
    val tag: String,
    val user_input: String,
    val model_response: String
)

data class SaveChatResponse(
    val message: String,
    val data: ChatLogData
)

data class ChatLogData(
    val log_chat: LogChat
)

data class LogChat(
    val user_input: String,
    val response: String,
    val user_id: String
)