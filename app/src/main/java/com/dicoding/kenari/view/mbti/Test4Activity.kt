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
import com.dicoding.kenari.databinding.ActivityTest4Binding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch

class Test4Activity : AppCompatActivity() {
    private val viewModel by viewModels<MbtiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityTest4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test4)
        binding = ActivityTest4Binding.inflate(layoutInflater)
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
            if (binding.C003.isChecked) {
                viewModel.addMBTI("C003")
            }
            if (binding.C009.isChecked) {
                viewModel.addMBTI("C009")
            }
            if (binding.C018.isChecked) {
                viewModel.addMBTI("C018")
            }
            if (binding.C019.isChecked) {
                viewModel.addMBTI("C019")
            }
            if (binding.C031.isChecked) {
                viewModel.addMBTI("C031")
            }
            if (binding.C035.isChecked) {
                viewModel.addMBTI("C035")
            }
            val intent = Intent(this, Test5Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}