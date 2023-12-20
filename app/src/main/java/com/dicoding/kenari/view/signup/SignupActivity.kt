package com.dicoding.kenari.view.signup

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
import com.dicoding.kenari.api.RegisterRequest
import com.dicoding.kenari.api.RegisterResponse
import com.dicoding.kenari.data.pref.UserModel
import com.dicoding.kenari.databinding.ActivitySignupBinding
import com.dicoding.kenari.view.ViewModelFactory
import com.dicoding.kenari.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isEmpty() ||email.isEmpty() || password.isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("Error!")
                    setMessage("Semua form harus tersis")
                    create()
                    show()
                }
            } else {
                val registerRequest = RegisterRequest(name, email, password)
                ApiConfig.instanceRetrofit.register(registerRequest)
                    .enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {

                            if (response.code() == 422) {
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle("Daftar akun gagal")
                                    setMessage("Email yang anda masukkan telah terdaftar di akun kami!")
                                    setPositiveButton("OK") { _, _ ->}
                                    create()
                                    show()
                                }
                            }

                            if (response.isSuccessful) {
                                val loginRequest = LoginRequest(email = email, password = password)
                                ApiConfig.instanceRetrofit.login(loginRequest)
                                    .enqueue(object : Callback<LoginResponse> {
                                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                                            if (response.isSuccessful) {
                                                val responseBody = response.body()

                                                if (responseBody != null) {
                                                    if (responseBody.data == null) {
                                                        Toast.makeText(this@SignupActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        val responseData = responseBody.data
                                                        viewModel.saveSession(UserModel(
                                                            email = email,
                                                            token = responseData.token.toString(),
                                                            userId = responseData.user?.userId.toString(),
                                                            name = responseData.user?.name.toString(),
                                                            label = responseData.user?.label.toString(),
                                                            address = responseData.user?.address.toString(),
                                                            university = responseData.user?.university.toString(),
                                                        ))

                                                        AlertDialog.Builder(this@SignupActivity).apply {
                                                            setTitle("Yeah!")
                                                            setMessage("Anda berhasil daftar akun. selamat datang $name")
                                                            setPositiveButton("Lanjutkan") { _, _ ->
                                                                val intent = Intent(context, MainActivity::class.java)
                                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                                startActivity(intent)
                                                                finish()
                                                            }
                                                            create()
                                                            show()
                                                        }

                                                        Toast.makeText(this@SignupActivity, "Selamat datang!", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                            // Handle failure here if needed
                                            Log.e("LoginActivity", "Error", t)
                                            Toast.makeText(this@SignupActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            // Handle failure here if needed
                            Log.e("LoginActivity", "Error", t)
                            Toast.makeText(this@SignupActivity, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    private fun playAnimation() {
        /*ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()*/

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                desc,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}