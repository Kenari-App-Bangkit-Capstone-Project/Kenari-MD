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
import com.dicoding.kenari.databinding.ActivityTest9Binding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch

class Test9Activity : AppCompatActivity() {
    private val viewModel by viewModels<MbtiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityTest9Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test9)
        binding = ActivityTest9Binding.inflate(layoutInflater)
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
            if (binding.C016.isChecked) {
                viewModel.addMBTI("C016")
            }
            if (binding.C020.isChecked) {
                viewModel.addMBTI("C020")
            }
            if (binding.C046.isChecked) {
                viewModel.addMBTI("C046")
            }
            if (binding.C053.isChecked) {
                viewModel.addMBTI("C053")
            }
            val intent = Intent(this, Test10Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}