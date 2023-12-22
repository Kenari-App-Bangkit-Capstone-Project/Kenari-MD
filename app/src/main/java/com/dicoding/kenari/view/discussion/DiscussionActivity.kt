package com.dicoding.kenari.view.discussion

import android.content.Intent
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
import com.dicoding.kenari.adapter.DiscussionAdapter
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.api.Discussion
import com.dicoding.kenari.api.GetAllDiscussionsResponse
import com.dicoding.kenari.databinding.ActivityDiscussionBinding
import com.dicoding.kenari.view.ViewModelFactory
import com.dicoding.kenari.view.chatbot.ChatbotActivity
import com.dicoding.kenari.view.main.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscussionActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDiscussionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)
        binding = ActivityDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Forum Diskusi Mental Health"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getSession().observe(this) { user ->
            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddDiscussionActivity::class.java)
            startActivity(intent)
        }

        setupView()
    }

    private fun setupView() {
        ApiConfig.instanceRetrofit.getAllDiscussion()
            .enqueue(object : Callback<GetAllDiscussionsResponse> {
                override fun onResponse(call: Call<GetAllDiscussionsResponse>, response: Response<GetAllDiscussionsResponse>) {

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            if (responseBody.data == null) {
                                Toast.makeText(this@DiscussionActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            } else {
                                val discussionList: List<Discussion>? = responseBody.data.discussions

                                val recyclerView: RecyclerView = findViewById(R.id.rv_discussion)
                                val layoutManager = LinearLayoutManager(this@DiscussionActivity)
                                val adapter = DiscussionAdapter(this@DiscussionActivity, discussionList)

                                recyclerView.layoutManager = layoutManager
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GetAllDiscussionsResponse>, t: Throwable) {
                    // Handle failure here if needed
                    Log.e("ChatbotActivity", "Error", t)
                    Toast.makeText(this@DiscussionActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this@DiscussionActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        finishAffinity()
        return true
    }
}
