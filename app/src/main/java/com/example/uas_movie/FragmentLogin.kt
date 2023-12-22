package com.example.uas_movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uas_movie.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentLogin : Fragment() {

    private lateinit var auth: FirebaseAuth
//    Firebase Authentication (FirebaseAuth) digunakan untuk
//    mengelola proses otentikasi dalam aplikasi Android. Dengan Firebase Authentication,
//    pengembang dapat memanfaatkan infrastruktur otentikasi yang aman dan terkelola oleh Firebase,
//    Dalam kode di bawah, kita menggunakan Firebase Authentication
//    untuk melakukan proses login dengan email dan password.
    private lateinit var binding: FragmentLoginBinding
//    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        prefManager = PrefManager.getInstance(requireContext())

        auth = FirebaseAuth.getInstance()

//        Log.d("After Save", sharedPreferences.all.toString())
        if (prefManager.isLoggedIn()){
            navigateToUserOrAdmin(prefManager.getUsername())
        }

        with(binding) {
            buttonLogin.setOnClickListener {
                val enteredEmail = email.text.toString()
                val enteredPassword = password.text.toString()

                if (enteredEmail.isNotEmpty() && enteredPassword.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                        .addOnCompleteListener(requireActivity()) { task ->
                            val currentUser = auth.currentUser
                            if (task.isSuccessful && currentUser != null) {
                                // Save login status to SharedPreferences
//                                saveUsername(enteredEmail)
//                                saveLoginStatus(true)
                                prefManager.saveUsername(enteredEmail)
                                prefManager.setLoggedIn(true)

                                // Navigate to HomeActivity or AdminActivity based on userType
                                navigateToUserOrAdmin(enteredEmail)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Login failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }  else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

//    private fun saveLoginStatus(isLoggedIn: Boolean) {
//        val editor = sharedPreferences.edit()
//        editor.putBoolean("isLoggedIn", isLoggedIn)
//        editor.apply()
//    }


    private fun navigateToUserOrAdmin(email: String) {
        val userType = getUserTypeFromEmail(email)

        val intent = if (userType == "admin") {
            Intent(requireContext(), AdminHome::class.java)
        } else {
            Intent(requireContext(), UserHome::class.java)
        }

        startActivity(intent)
        requireActivity().finish()
    }

//    private fun saveUsername(username: String) {
//        val editor = sharedPreferences.edit()
//        editor.putString("USERNAME_KEY", username)
//        editor.apply()
//    }

//    fun getUsername(): String {
//        return sharedPreferences.getString("USERNAME_KEY", "") ?: ""
//    }


    private fun getUsernameFromEmail(email: String): String {
        return email.substringBefore('@')
    }


    private fun getUserTypeFromEmail(email: String): String {
        return if (email.contains("admin")) {
            "admin"
        } else {
            "user"
        }
    }

}


