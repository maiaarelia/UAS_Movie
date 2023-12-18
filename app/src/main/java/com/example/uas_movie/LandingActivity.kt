package com.example.uas_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uas_movie.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fungsi onClick untuk tombol btn_start
        binding.btnStart.setOnClickListener {
            // Intent untuk memulai aktivitas tab_layout
            val intent = Intent(this, LoginRegisterTabLayout::class.java)
            startActivity(intent)
        }
    }
}