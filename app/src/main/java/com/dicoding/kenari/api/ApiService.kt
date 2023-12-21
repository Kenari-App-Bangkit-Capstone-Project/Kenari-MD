package com.dicoding.kenari.api

import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Body
import com.dicoding.kenari.api.LoginResponse
import retrofit2.http.GET
import retrofit2.http.Path

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

data class CommentRequest(
    val comment: String
)

data class AddNewDiscussionRequest(
    val content: String,
    val isAnonymous: String
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

    @GET("discussion/detail/{id}")
    fun getDiscussionDetail(@Path("id") discussionId: Int): Call<GetDiscussionByIdResponse>

    @POST("discussion/comment/add/{id}")
    fun addDiscussionComment(
        @Path("id") discussionId: Int,
        @Body commentRequest: CommentRequest
    ): Call<Any>

    @POST("discussion/create")
    fun addNewDiscussion(@Body addNewDiscussionRequest: AddNewDiscussionRequest): Call<Any>
}


//API Service ML
data class ModelRequest(
    val user_input: String
)
interface ApiServiceML {
    @POST("chatbot/response")
    fun getModelResponse(@Body modelRequest: ModelRequest): Call<ModelResponse>
}