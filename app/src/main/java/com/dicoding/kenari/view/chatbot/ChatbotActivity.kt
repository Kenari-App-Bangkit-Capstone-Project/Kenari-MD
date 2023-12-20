package com.dicoding.kenari.view.chatbot

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kenari.R
import com.dicoding.kenari.adapter.ChatbotAdapter
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.api.ChatbotHistoryResponse
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatbotActivity : AppCompatActivity() {
    private val viewModel by viewModels<ChatbotViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        supportActionBar?.title = "Chatbot"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getSession().observe(this) { user ->
            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        ApiConfig.instanceRetrofit.getChatbotHistory()
            .enqueue(object : Callback<ChatbotHistoryResponse> {
                override fun onResponse(call: Call<ChatbotHistoryResponse>, response: Response<ChatbotHistoryResponse>) {

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            if (responseBody.data == null) {
                                Toast.makeText(this@ChatbotActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            } else {
                                val chatbotHistory = responseBody.data.chatHistories

                                Log.i("chatbotHistory", chatbotHistory.toString())

                                val recyclerView: RecyclerView = findViewById(R.id.rv_chat)
                                val layoutManager = LinearLayoutManager(this@ChatbotActivity)
                                val adapter = chatbotHistory?.let { ChatbotAdapter(it) }

                                recyclerView.layoutManager = layoutManager
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ChatbotHistoryResponse>, t: Throwable) {
                    // Handle failure here if needed
                    Log.e("ChatbotActivity", "Error", t)
                    Toast.makeText(this@ChatbotActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                }
            })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
