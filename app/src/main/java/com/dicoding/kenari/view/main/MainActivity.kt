package com.dicoding.kenari.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.kenari.R
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.databinding.ActivityMainBinding
import com.dicoding.kenari.view.ViewModelFactory
import com.dicoding.kenari.view.about.AboutActivity
import com.dicoding.kenari.view.chatbot.ChatbotActivity
import com.dicoding.kenari.view.discussion.DiscussionActivity
import com.dicoding.kenari.view.mbti.MbtiActivity
import com.dicoding.kenari.view.mbti.Test1Activity
import com.dicoding.kenari.view.test.TestActivity
import com.dicoding.kenari.view.welcome.WelcomeActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btnLogout -> {
                    viewModel.logout()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.btnAbout -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

            lifecycleScope.launch {
                ApiConfig.initialize(user.token)
            }
        }

        topAppBar = binding.topAppBar

        viewModel.getSession().observe(this) { user ->
            topAppBar.subtitle = "  ${user.name}"
        }

        val btn1: Button = findViewById(R.id.button1)
        btn1.setOnClickListener {
            val intent = Intent(this, DiscussionActivity::class.java)
            startActivity(intent)
        }

        val btn2: Button = findViewById(R.id.button2)
        btn2.setOnClickListener {
            val intent = Intent(this, MbtiActivity::class.java)
            startActivity(intent)
        }

        val btn3: Button = findViewById(R.id.button3)
        btn3.setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
        }

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
