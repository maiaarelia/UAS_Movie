package com.example.uas_movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.uas_movie.databinding.ActivityUserHomeBinding
import com.google.firebase.auth.FirebaseAuth

class UserHome : AppCompatActivity() {
    private lateinit var binding: ActivityUserHomeBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(UserMovie())


        binding.BottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home-> replaceFragment(UserMovie())
                R.id.nav_favor -> replaceFragment(UserFavorite())
                R.id.nav_profile-> replaceFragment(UserProfile())
                else-> {}
            }
            true
        }

        setSupportActionBar(findViewById(R.id.toolbar))



        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        var fragmentMovie = fragmentManager.beginTransaction()
        fragmentMovie.replace(R.id.frame_layout, fragment)
        fragmentMovie.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_admin, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Implement logout logic here
                // For example, show a confirmation dialog or directly logout the user
                // You may use FirebaseAuth.getInstance().signOut() if you are using Firebase
                FirebaseAuth.getInstance().signOut()
                saveLoginStatus(false)

                // Add any additional logic, such as returning to the login screen
                val intent = Intent(this, LoginRegisterTabLayout::class.java)
                startActivity(intent)
                finish() // Close the current activity
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.clear()
        editor.apply()
    }

}