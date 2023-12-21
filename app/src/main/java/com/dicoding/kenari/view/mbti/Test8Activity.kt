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
import com.dicoding.kenari.databinding.ActivityTest8Binding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch

class Test8Activity : AppCompatActivity() {
    private val viewModel by viewModels<MbtiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityTest8Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test8)
        binding = ActivityTest8Binding.inflate(layoutInflater)
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
            if (binding.C008.isChecked) {
                viewModel.addMBTI("C008")
            }
            if (binding.C021.isChecked) {
                viewModel.addMBTI("C021")
            }
            if (binding.C027.isChecked) {
                viewModel.addMBTI("C027")
            }
            if (binding.C047.isChecked) {
                viewModel.addMBTI("C047")
            }
            if (binding.C051.isChecked) {
                viewModel.addMBTI("C051")
            }
            if (binding.C055.isChecked) {
                viewModel.addMBTI("C055")
            }
            val intent = Intent(this, Test9Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}