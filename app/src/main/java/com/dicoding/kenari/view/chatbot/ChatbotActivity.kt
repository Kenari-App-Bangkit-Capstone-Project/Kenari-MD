package com.dicoding.kenari.view.chatbot

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kenari.R
import com.dicoding.kenari.adapter.ChatbotAdapter
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.api.ChatbotHistoryResponse
import com.dicoding.kenari.api.MLApiConfig
import com.dicoding.kenari.api.ModelRequest
import com.dicoding.kenari.api.ModelResponse
import com.dicoding.kenari.api.SaveChatResponse
import com.dicoding.kenari.api.SaveChatResponseRequest
import com.dicoding.kenari.databinding.ActivityChatbotBinding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ChatbotChat(
    val user_input: String,
    val response: String
)

class ChatbotActivity : AppCompatActivity() {
    private val viewModel by viewModels<ChatbotViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityChatbotBinding

    private val chatbotList: MutableList<ChatbotChat> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Chatbot"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getSession().observe(this) { user ->
            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
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

                                val nonNullableChatbotHistory = chatbotHistory ?: emptyList()

                                for (chatHistory in nonNullableChatbotHistory) {
                                    val userInput = chatHistory.user_input ?: "No user input"
                                    val response = chatHistory.response ?: "No response"
                                    val chatMessage = ChatbotChat(userInput, response)
                                    chatbotList.add(chatMessage)
                                }

                                updateChatList(chatbotList)
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

    private fun setupAction() {
        val edtMessage: EditText = findViewById(R.id.edt_message)

        edtMessage.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Handle Enter key pressed
                sendMessage()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val userInput = binding.edtMessage.text.toString()

        if (userInput.isEmpty()) {
            Toast.makeText(this@ChatbotActivity, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            binding.edtMessage.text.clear()

            val chatMessage = ChatbotChat(userInput, "Loading...")
            chatbotList.add(chatMessage)

            updateChatList(chatbotList)

            hideKeyboard()

            val modelRequest = ModelRequest(userInput)
            MLApiConfig.instanceRetrofit.getModelResponse(modelRequest)
                .enqueue(object : Callback<ModelResponse> {
                    override fun onResponse(call: Call<ModelResponse>, response: Response<ModelResponse>) {

                        if (response.isSuccessful) {
                            val responseBody = response.body()

                            if (responseBody != null) {
                                val lastChat = chatbotList.last()
                                val updatedChat = lastChat.copy(response = responseBody.model_response)
                                chatbotList[chatbotList.size - 1] = updatedChat

                                updateChatList(chatbotList)

                                val saveChatResponseRequest = SaveChatResponseRequest(
                                    userInput, responseBody.model_response
                                )
                                ApiConfig.instanceRetrofit.saveChatResponse(saveChatResponseRequest)
                                    .enqueue(object : Callback<SaveChatResponse> {
                                        override fun onResponse(call: Call<SaveChatResponse>, response: Response<SaveChatResponse>) {

                                            if (response.isSuccessful) {
                                                Log.i("Sukses simpan chat", response.body().toString())
                                            }
                                        }

                                        override fun onFailure(call: Call<SaveChatResponse>, t: Throwable) {
                                            // Handle failure here if needed
                                            Log.e("ChatbotActivity", "Error", t)
                                            Toast.makeText(this@ChatbotActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }
                    }

                    override fun onFailure(call: Call<ModelResponse>, t: Throwable) {
                        // Handle failure here if needed
                        Log.e("ChatbotActivity", "Error", t)
                        Toast.makeText(this@ChatbotActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun updateChatList(chatbotList: List<ChatbotChat>) {
        val recyclerView: RecyclerView = findViewById(R.id.rv_chat)
        val layoutManager = LinearLayoutManager(this@ChatbotActivity)
        val adapter = ChatbotAdapter(chatbotList)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        recyclerView.post {
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
