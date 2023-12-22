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
import com.example.uas_movie.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

// FragmentRegister adalah kelas untuk menangani tampilan dan logika Fragment Register
class FragmentRegister : Fragment() {
    private lateinit var auth: FirebaseAuth
    //    Firebase Authentication (FirebaseAuth) digunakan untuk
    //    mengelola proses otentikasi dalam aplikasi Android. Dengan Firebase Authentication,
    //    pengembang dapat memanfaatkan infrastruktur otentikasi yang aman dan terkelola oleh Firebase,
    //    Dalam kode di bawah, kita menggunakan Firebase Authentication
    //    untuk melakukan proses login dengan email dan password.
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout FragmentRegisterBinding dengan tampilan fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan instance FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Mendapatkan instance FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()

        // Mendapatkan instance PrefManager
        prefManager = PrefManager.getInstance(requireContext())

        with(binding) {
            // Menangani klik pada tombol register
            buttonRegister.setOnClickListener {
                val id = UUID.randomUUID().toString()
                val username = username.text.toString().trim()
                val email = email.text.toString().trim()
                val phone = nomor.text.toString().trim()
                val password = passwordAkun.text.toString()
                val passwordUlang = passwordUlang.text.toString()

                // Memastikan email, username, phone, dan password tidak kosong
                if (email.isNotEmpty() && username.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                    // Melakukan proses registrasi menggunakan FirebaseAuth
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Menyimpan data pengguna ke Firestore
                                val newAccount = User(id, email, username, "user", phone, password, passwordUlang)
                                saveUserDataToFirestore(newAccount)

                                // Menyimpan status login ke SharedPreferences
                                prefManager.saveUsername(email)
                                prefManager.setLoggedIn(true)

                                // Navigasi ke HomeActivity atau AdminActivity berdasarkan userType
                                navigateToHomeOrAdmin("user")
                            } else {
                                // Menampilkan pesan kesalahan jika registrasi gagal
                                Toast.makeText(
                                    requireContext(),
                                    "Registration failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("RegisterFragment", "Error creating user", task.exception)
                            }
                        }
                } else {
                    // Menampilkan pesan jika ada field yang belum diisi
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Menyimpan data pengguna ke Firestore
    private fun saveUserDataToFirestore(account: User) {
        firestore.collection("users")
            .add(account)
            .addOnSuccessListener { documentReference ->
                Log.d("RegisterFragment", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("RegisterFragment", "Error adding document to Firestore", e)
                Toast.makeText(
                    requireContext(),
                    "Error adding document to Firestore: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Menyimpan status login ke SharedPreferences
    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    // Navigasi ke HomeActivity atau AdminActivity berdasarkan userType
    private fun navigateToHomeOrAdmin(userType: String) {
        val intent = if (userType == "admin") {
            Intent(requireContext(), AdminHome::class.java)
        } else {
            Intent(requireContext(), UserHome::class.java)
        }

        startActivity(intent)
        requireActivity().finish()
    }
}
