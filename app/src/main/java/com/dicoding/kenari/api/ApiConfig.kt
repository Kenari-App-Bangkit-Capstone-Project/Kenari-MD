package com.dicoding.kenari.api

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "https://backend-dot-capstoneke1.et.r.appspot.com/api/v1/"

    val apiClient: ApiClient = ApiClient()

    fun initialize(userToken: String) {
        apiClient.userToken = userToken
    }

    private val client : Retrofit

        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val authInterceptor = AuthInterceptor(apiClient.userToken)

            val client : OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(authInterceptor)
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

    val instanceRetrofit : ApiService
        get() = client.create(ApiService::class.java)
}

object MLApiConfig {
    private const val BASE_URL = "https://kenari-chatbot-api-vm2wxzpccq-et.a.run.app"

    private val client : Retrofit

        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY


            val client : OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

    val instanceRetrofit : ApiServiceML
        get() = client.create(ApiServiceML::class.java)
}


class AuthInterceptor(private val userToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $userToken")
            .build()

        return chain.proceed(newRequest)
    }
}

// Kelas ApiClient
class ApiClient {
    var userToken: String = ""
}