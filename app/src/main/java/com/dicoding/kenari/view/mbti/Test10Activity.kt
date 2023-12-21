package com.dicoding.kenari.view.mbti

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.api.MbtiResultRequest
import com.dicoding.kenari.api.MbtiResultResponse
import com.dicoding.kenari.databinding.ActivityTest10Binding
import com.dicoding.kenari.view.ViewModelFactory
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
        binding = ActivityTest10Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        viewModel.getSession().observe(this) { user ->
            initializeApiConfig(user.token)
        }

        binding.next.setOnClickListener {
            handleNextButtonClick()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.title = "Tes MBTI"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initializeApiConfig(token: String) {
        lifecycleScope.launch {
            ApiConfig.initialize(token)
        }
    }

    private fun handleNextButtonClick() {
        if (binding.C036.isChecked) {
            viewModel.addMBTI("C036")
        }
        if (binding.C049.isChecked) {
            viewModel.addMBTI("C049")
        }
        if (binding.C057.isChecked) {
            viewModel.addMBTI("C057")
        }

        var selectedCharacter: List<String>? = null

        viewModel.getSelectedMBTI().observe(this) { selectedMBTI ->
            selectedCharacter = selectedMBTI.distinct()
        }


        if (selectedCharacter != null && selectedCharacter!!.isNotEmpty()) {
            val selectedChar = MbtiResultRequest(selectedCharacter!!)
            ApiConfig.instanceRetrofit.mbtiResult(selectedChar)
                .enqueue(object : Callback<MbtiResultResponse> {
                    override fun onResponse(call: Call<MbtiResultResponse>, response: Response<MbtiResultResponse>) {

                        if (response.isSuccessful) {
                            val responseBody = response.body()

                            if (responseBody != null) {
                                val personality = responseBody.data.typeResult.type
                                val info = responseBody.data.typeResult.information

                                viewModel.resetMBTI()
                                Toast.makeText(this@Test10Activity, "Tes telah selesai, berikut hasilnya!", Toast.LENGTH_SHORT).show()

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
        } else {
            Toast.makeText(this@Test10Activity, "Gagal mengambil data, silahkan ulangi tes!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Test10Activity, MbtiActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
