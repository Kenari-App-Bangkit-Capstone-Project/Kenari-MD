package com.dicoding.kenari.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.api.LoginRequest
import com.dicoding.kenari.api.LoginResponse
import com.dicoding.kenari.data.pref.UserModel
import com.dicoding.kenari.databinding.ActivityLoginBinding
import com.dicoding.kenari.view.ViewModelFactory
import com.dicoding.kenari.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("Error!")
                    setMessage("Email dan password harus terisi")
                    create()
                    show()
                }
            } else {
                val loginRequest = LoginRequest(email = email, password = password)
                ApiConfig.instanceRetrofit.login(loginRequest)
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                            if (response.code() == 403) {
                                AlertDialog.Builder(this@LoginActivity).apply {
                                    setTitle("Login Gagal")
                                    setMessage("Email atau Password yang anda masukkan tidak cocok")
                                    setPositiveButton("OK") { _, _ ->}
                                    create()
                                    show()
                                }
                            }

                            if (response.isSuccessful) {
                                val responseBody = response.body()

                                if (responseBody != null) {
                                    if (responseBody.data == null) {
                                        Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                    } else {
                                        viewModel.saveSession(UserModel(email, responseBody.data.token.toString()))

                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()

                                        Toast.makeText(this@LoginActivity, "Login Berhasil, selamat datang kembali!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            // Handle failure here if needed
                            Log.e("LoginActivity", "Error", t)
                            Toast.makeText(this@LoginActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        /*val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)*/
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                /*message,*/
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

}