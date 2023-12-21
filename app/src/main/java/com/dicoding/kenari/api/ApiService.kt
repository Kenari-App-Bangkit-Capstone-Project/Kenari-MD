package com.dicoding.kenari.api

import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body
import com.dicoding.kenari.api.LoginResponse
import retrofit2.http.GET

data class LoginRequest(
    val email: String,
    val password: String
)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class SaveChatResponseRequest(
    val user_input: String,
    val response: String,
)

interface ApiService {

//    Auth
    @POST("auth/signin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

//    Chatbot
    @GET("chatbot/chat")
    fun getChatbotHistory(): Call<ChatbotHistoryResponse>
    
    @POST("chatbot/save")
    fun saveChatResponse(@Body saveChatResponseRequest: SaveChatResponseRequest): Call<SaveChatResponse>

//    Diskusi
    @GET("discussion/all")
    fun getAllDiscussion(): Call<GetAllDiscussionsResponse>
}


//API Service ML
data class ModelRequest(
    val user_input: String
)
interface ApiServiceML {
    @POST("chatbot/response")
    fun getModelResponse(@Body modelRequest: ModelRequest): Call<ModelResponse>
}