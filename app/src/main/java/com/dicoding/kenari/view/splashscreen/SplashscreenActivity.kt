package com.dicoding.kenari.view.splashscreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dicoding.kenari.R
import com.dicoding.kenari.view.main.MainActivity

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashscreenActivity, MainActivity::class.java))
                finish()
            }, 3000)
        }
    }
}