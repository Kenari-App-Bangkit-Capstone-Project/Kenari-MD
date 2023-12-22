package com.dicoding.kenari.view.mbti

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.dicoding.kenari.R
import com.dicoding.kenari.api.ApiConfig
import com.dicoding.kenari.databinding.ActivityTest2Binding
import com.dicoding.kenari.view.ViewModelFactory
import kotlinx.coroutines.launch

class Test2Activity : AppCompatActivity() {
    private val viewModel by viewModels<MbtiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityTest2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)
        binding = ActivityTest2Binding.inflate(layoutInflater)
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
            if (binding.C002.isChecked) {
                viewModel.addMBTI("C002")
            }
            if (binding.C016.isChecked) {
                viewModel.addMBTI("C016")
            }
            if (binding.C020.isChecked) {
                viewModel.addMBTI("C020")
            }
            if (binding.C045.isChecked) {
                viewModel.addMBTI("C045")
            }
            val intent = Intent(this, Test3Activity::class.java)
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
            val intent = Intent(this@Test2Activity, MbtiActivity::class.java)
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