package com.dicoding.kenari.view.discussion

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kenari.R
import com.dicoding.kenari.adapter.CommentAdapter
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.api.Comment
import com.dicoding.kenari.api.CommentRequest
import com.dicoding.kenari.api.GetDiscussionByIdResponse
import com.dicoding.kenari.databinding.ActivityDetailDiscussionBinding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DetailDiscussionActivity : AppCompatActivity() {


    private val viewModel by viewModels<DiscussionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailDiscussionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_discussion)

        binding = ActivityDetailDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Diskusi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getSession().observe(this) { user ->
            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        val discussionId = intent.getStringExtra("discussion_id")

        if (discussionId != null) {
            setupView(discussionId)
            setupAction(discussionId)
        }
    }

    private fun setupView(discussionId: String) {
        ApiConfig.instanceRetrofit.getDiscussionDetail(discussionId.toInt())
            .enqueue(object : Callback<GetDiscussionByIdResponse> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<GetDiscussionByIdResponse>, response: Response<GetDiscussionByIdResponse>) {

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            if (responseBody.data == null) {
                                Toast.makeText(this@DetailDiscussionActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            } else {
                                val diskusiData = responseBody.data.discussion
                                val commentsList: List<Comment>? = responseBody.data.comments

                                if (diskusiData?.isAnonymous.toBoolean()) {
                                    binding.discussionUsername.text = "Anonim"
                                } else {
                                    binding.discussionUsername.text = diskusiData?.user?.name
                                }
                                binding.discussionText.text = diskusiData?.content
                                binding.dicussionDate.text = dateFormatted(diskusiData?.createdAt)

                                val recyclerView: RecyclerView = findViewById(R.id.rv_comments)
                                val layoutManager = LinearLayoutManager(this@DetailDiscussionActivity)
                                val adapter = CommentAdapter(commentsList)

                                recyclerView.layoutManager = layoutManager
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GetDiscussionByIdResponse>, t: Throwable) {
                    // Handle failure here if needed
                    Log.e("ChatbotActivity", "Error", t)
                    Toast.makeText(this@DetailDiscussionActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupAction(discussionId: String) {
        binding.btnSend.setOnClickListener {
            val comment = binding.edtComment.text.toString()

            if (comment.isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("Error!")
                    setMessage("Komentar tidak boleh kosong!")
                    create()
                    show()
                }
            } else {
                val commentRequest = CommentRequest(comment)
                ApiConfig.instanceRetrofit.addDiscussionComment(discussionId.toInt(), commentRequest)
                    .enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful) {
                                binding.edtComment.text.clear()
                                hideKeyboard()
                                Toast.makeText(this@DetailDiscussionActivity, "Berhasil menambahkan komentar", Toast.LENGTH_SHORT).show()
                                setupView(discussionId)
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            // Handle failure here if needed
                            Log.e("ChatbotActivity", "Error", t)
                            Toast.makeText(this@DetailDiscussionActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                        }
                    })
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormatted(date: String?): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val instant = Instant.from(inputFormatter.parse(date))
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  •  HH:mm ")
        val normalizedDateString = outputFormatter.format(localDateTime)

        return normalizedDateString.toString()
    }
}