package com.dicoding.kenari.view.mbti

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.kenari.R
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.databinding.ActivityMbtiBinding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch

class MbtiActivity : AppCompatActivity() {
    private val viewModel by viewModels<MbtiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMbtiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbti)
        binding = ActivityMbtiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Tes MBTI"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#20BAB3")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getSession().observe(this) { user ->
            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        binding.testButton.setOnClickListener {
            viewModel.resetMBTI()
            val intent = Intent(this, Test1Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}