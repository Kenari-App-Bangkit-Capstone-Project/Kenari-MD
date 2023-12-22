package com.dicoding.kenari.view.discussion

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.dicoding.kenari.R
import com.dicoding.kenari.api.AddNewDiscussionRequest
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.databinding.ActivityAddDiscussionBinding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDiscussionActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityAddDiscussionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_discussion)
        binding = ActivityAddDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Buat Diskusi"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getSession().observe(this) { user ->
            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        binding.submitDiscussion.setOnClickListener {
            val content = binding.content.text.toString()
            val isAnonymous = binding.isAnonym.isChecked.toString()
            
            if (content.isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("Error!")
                    setMessage("Diskusi tidak boleh kosong!")
                    create()
                    show()
                }
            } else {
                val addNewDiscussionRequest = AddNewDiscussionRequest(content, isAnonymous)

                ApiConfig.instanceRetrofit.addNewDiscussion(addNewDiscussionRequest)
                    .enqueue(object : Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful) {
                                hideKeyboard()
                                Toast.makeText(this@AddDiscussionActivity, "Berhasil membuat diskusi baru!", Toast.LENGTH_SHORT).show()

                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
                            // Handle failure here if needed
                            Log.e("ChatbotActivity", "Error", t)
                            Toast.makeText(this@AddDiscussionActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
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
}