package com.dicoding.kenari.view.mbti

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.kenari.R
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.api.MbtiResultRequest
import com.dicoding.kenari.api.MbtiResultResponse
import com.dicoding.kenari.api.ModelResponse
import com.dicoding.kenari.api.SaveChatResponse
import com.dicoding.kenari.api.SaveChatResponseRequest
import com.dicoding.kenari.databinding.ActivityTest10Binding
import com.dicoding.kenari.view.ViewModelFactory
import com.dicoding.kenari.view.discussion.DiscussionActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Test10Activity : AppCompatActivity() {

    private val viewModel by viewModels<MbtiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityTest10Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test10)
        binding = ActivityTest10Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Tes MBTI"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getSession().observe(this) { user ->
            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        binding.next.setOnClickListener {
            if (binding.C036.isChecked) {
                viewModel.addMBTI("C036")
            }
            if (binding.C049.isChecked) {
                viewModel.addMBTI("C049")
            }
            if (binding.C057.isChecked) {
                viewModel.addMBTI("C057")
            }
            viewModel.getSelectedMBTI().observe(this) { selectedMBTI ->
                val selectedCharacter = MbtiResultRequest(selectedMBTI.distinct())

                ApiConfig.instanceRetrofit.mbtiResult(selectedCharacter)
                    .enqueue(object : Callback<MbtiResultResponse> {
                        override fun onResponse(call: Call<MbtiResultResponse>, response: Response<MbtiResultResponse>) {

                            if (response.isSuccessful) {
                                val responseBody = response.body()

                                if (responseBody != null) {
                                    val personality = responseBody.data.typeResult.type
                                    val info = responseBody.data.typeResult.information

                                    viewModel.resetMBTI()

                                    val intent = Intent(this@Test10Activity, MbtiResultActivity::class.java)
                                    intent.putExtra("personality", personality)
                                    intent.putExtra("info", info)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }

                        override fun onFailure(call: Call<MbtiResultResponse>, t: Throwable) {
                            // Handle failure here if needed
                            Log.e("ChatbotActivity", "Error", t)
                            Toast.makeText(this@Test10Activity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}