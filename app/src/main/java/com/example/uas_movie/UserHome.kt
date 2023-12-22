package com.example.uas_movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.uas_movie.databinding.ActivityUserHomeBinding
import com.google.firebase.auth.FirebaseAuth

class UserHome : AppCompatActivity() {
    // Inisialisasi variabel binding untuk menghubungkan dengan layout XML menggunakan View Binding
    private lateinit var binding: ActivityUserHomeBinding

    // Inisialisasi PrefManager untuk mengelola preferensi pengguna
    private lateinit var prefManager: PrefManager

    // Metode yang dipanggil saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding untuk menghubungkan layout
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi PrefManager
        prefManager = PrefManager.getInstance(this@UserHome)

        // Menampilkan fragment pertama (UserMovie) saat aktivitas dibuat
        replaceFragment(UserMovie())

        // Menangani peristiwa saat item dipilih pada BottomNavigationView
        binding.BottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> replaceFragment(UserMovie())
                R.id.nav_favor -> replaceFragment(UserFavorite())
                R.id.nav_profile -> replaceFragment(UserProfile())
                else -> {} // Jika ada item lain, tidak dilakukan apa-apa
            }
            true
        }

        // Mengatur toolbar sebagai ActionBar
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    // Metode untuk menggantikan fragment yang sedang ditampilkan
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    // Metode untuk membuat dan menginflasi menu di ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_admin, menu)
        return true
    }

    // Metode untuk menangani item yang dipilih pada ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Logout pengguna menggunakan Firebase Authentication
                FirebaseAuth.getInstance().signOut()

                // Mengatur status login ke false menggunakan PrefManager
                prefManager.setLoggedIn(false)

                // Membuka aktivitas LoginRegisterTabLayout dan menutup aktivitas ini
                val intent = Intent(this@UserHome, LoginRegisterTabLayout::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
