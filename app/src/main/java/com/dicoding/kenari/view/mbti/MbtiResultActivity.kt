package com.dicoding.kenari.view.mbti

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.kenari.R
import com.dicoding.kenari.databinding.ActivityMbtiResultBinding

class MbtiResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMbtiResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbti_result)
        binding = ActivityMbtiResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Hasil Tes"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val personality = intent.getStringExtra("personality")
        val info = intent.getStringExtra("info")

        binding.personality.text = personality
        binding.info.text = info
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}