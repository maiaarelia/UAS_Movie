package com.example.uas_movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_movie.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfile : Fragment() {
    // Inisialisasi variabel binding untuk menghubungkan dengan layout XML menggunakan View Binding
    private lateinit var binding: FragmentUserProfileBinding

    // Inisialisasi objek FirebaseAuth untuk mengelola otentikasi pengguna
    private lateinit var auth: FirebaseAuth

    // Inisialisasi objek FirebaseFirestore untuk mengakses database Firestore
    private lateinit var firestore: FirebaseFirestore

    // Metode yang dipanggil saat tampilan fragment dibuat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout FragmentUserProfileBinding dengan tampilan fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()

        // Memuat dan menampilkan data pengguna
        loadUserData()

        return binding.root
    }

    // Fungsi untuk memuat data pengguna dari Firestore dan menampilkannya di tampilan
    private fun loadUserData() {
        // Mendapatkan alamat email pengguna yang saat ini masuk
        val userEmail = auth.currentUser?.email
        userEmail?.let { email ->
            // Query Firestore untuk mencari data pengguna berdasarkan alamat email
            firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    // Jika ditemukan data pengguna
                    if (!documents.isEmpty) {
                        // Mengambil dokumen pertama (asumsi hanya ada satu)
                        val document = documents.documents[0]
                        // Mengonversi dokumen ke objek User
                        val account = document.toObject(User::class.java)
                        account?.let {
                            // Menetapkan nilai-nilai ke TextView yang sesuai
                            binding.textViewName.text = "${it.username}"
                            binding.textViewEmail.text = "${it.email}"
                            binding.textViewPhone.text = "${it.nomor_telp}"
                        }
                    } else {
                        // Menampilkan pesan jika data pengguna tidak ditemukan
                        Toast.makeText(
                            requireContext(),
                            "User data not found",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("UserProfile", "User data not found")
                    }
                }
                .addOnFailureListener { e ->
                    // Menampilkan pesan jika terjadi kesalahan saat memuat data pengguna
                    Toast.makeText(
                        requireContext(),
                        "Error loading user data: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("UserProfile", "Error loading user data", e)
                }
        }
    }
}
