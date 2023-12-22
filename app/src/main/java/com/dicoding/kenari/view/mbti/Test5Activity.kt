package com.dicoding.kenari.view.mbti

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        supportActionBar?.title = "Tes Kepribadian MBTI"
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
            val intent = Intent(this, Test6Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        showExitConfirmationDialog()
        return true
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin keluar? Data tes Anda tidak akan disimpan.")
        builder.setPositiveButton("Ya, keluar dari tes") { _, _ ->
            val intent = Intent(this@Test5Activity, MbtiActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}