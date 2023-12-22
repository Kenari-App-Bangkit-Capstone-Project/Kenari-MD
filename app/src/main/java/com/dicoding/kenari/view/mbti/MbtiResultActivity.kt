package com.dicoding.kenari.view.mbti

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.dicoding.kenari.R
import com.dicoding.kenari.databinding.ActivityMbtiResultBinding

class MbtiResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMbtiResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbti_result)
        binding = ActivityMbtiResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MbtiResultActivity, MbtiActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        supportActionBar?.title = "Hasil Tes Kepribadian MBTI"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val personality = intent.getStringExtra("personality")
        val info = intent.getStringExtra("info")

        binding.personality.text = personality
        binding.info.text = info
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this@MbtiResultActivity, MbtiActivity::class.java)
        startActivity(intent)
        finish()
        return true
    }
}