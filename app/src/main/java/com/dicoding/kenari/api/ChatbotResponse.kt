package com.dicoding.kenari.api

import com.squareup.moshi.Json
import java.util.Date


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