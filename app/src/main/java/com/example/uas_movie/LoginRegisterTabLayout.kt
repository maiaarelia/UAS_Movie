package com.example.uas_movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uas_movie.databinding.ActivityLoginRegisterTabLayoutBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoginRegisterTabLayout : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterTabLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegisterTabLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

            val adapter =  FragmentLoginRegisterAdapter(this)
            binding.viewPagerLogreg.adapter = adapter
            TabLayoutMediator(binding.tabLayoutLogreg, binding.viewPagerLogreg) { tab, position ->
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> "Invalid"
                }
            }.attach()
        }
//    }
}