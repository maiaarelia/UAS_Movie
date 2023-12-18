package com.example.uas_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            // Start the main activity after the delay
            startActivity(Intent(this@MainActivity, LandingActivity::class.java))
            finish() // Close the splash screen activity
        }, 3000)

    }
}