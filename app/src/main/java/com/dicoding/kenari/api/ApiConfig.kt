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

    private  val client : Retrofit

        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client : OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(AuthInterceptor())
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

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = "YOUR_BEARER_TOKEN"

        val newRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}