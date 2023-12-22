package com.example.uas_movie

import android.content.Context
import android.content.SharedPreferences

// PrefManager adalah kelas utilitas untuk mengelola SharedPreferences terkait autentikasi pengguna.
class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    companion object {
        // Nama berkas SharedPreferences
        private const val PREFS_FILENAME = "AuthAppPrefs"
        // Kunci-kunci untuk menyimpan data di SharedPreferences
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        // Singleton instance untuk PrefManager
        @Volatile
        private var instance: PrefManager? = null

        // Mendapatkan instance PrefManager
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    // Inisialisasi SharedPreferences pada saat pembuatan objek PrefManager
    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    // Fungsi untuk menyimpan status login ke SharedPreferences
    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Fungsi untuk mendapatkan status login dari SharedPreferences
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Fungsi untuk menyimpan username ke SharedPreferences
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    // Fungsi untuk menyimpan password ke SharedPreferences
    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    // Fungsi untuk mendapatkan username dari SharedPreferences
    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "") ?: ""
    }

    // Fungsi untuk mendapatkan password dari SharedPreferences
    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }

    // Fungsi untuk menghapus semua data dari SharedPreferences
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
