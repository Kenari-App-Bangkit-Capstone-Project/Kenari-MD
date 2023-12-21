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
import com.dicoding.kenari.databinding.ActivityTest5Binding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch

class Test5Activity : AppCompatActivity() {
    private val viewModel by viewModels<MbtiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityTest5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test5)
        binding = ActivityTest5Binding.inflate(layoutInflater)
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
            if (binding.C012.isChecked) {
                viewModel.addMBTI("C012")
            }
            if (binding.C026.isChecked) {
                viewModel.addMBTI("C026")
            }
            if (binding.C041.isChecked) {
                viewModel.addMBTI("C041")
            }
            if (binding.C044.isChecked) {
                viewModel.addMBTI("C044")
            }
            if (binding.C050.isChecked) {
                viewModel.addMBTI("C050")
            }
            if (binding.C052.isChecked) {
                viewModel.addMBTI("C052")
            }
            val intent = Intent(this, Test6Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}