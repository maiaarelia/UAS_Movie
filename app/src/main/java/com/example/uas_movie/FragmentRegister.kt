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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRegister.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRegister : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        firestore = FirebaseFirestore.getInstance()

        with(binding) {
            buttonRegister.setOnClickListener {
                val id =  String()
                val username = username.text.toString().trim()
                val email = email.text.toString().trim()
                val phone = nomor.text.toString().trim()
                val password = passwordAkun.text.toString()
                val passwordUlang = passwordUlang.text.toString()

                if (email.isNotEmpty() && username.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Save user data to Firestore
                                val newAccount = User(id, email, username, "user", phone, password, passwordUlang)
                                saveUserDataToFirestore(newAccount)

                                // Save login status to SharedPreferences
                                saveLoginStatus(true)

                                // Navigate to HomeActivity or AdminActivity based on userType
                                navigateToHomeOrAdmin("user")
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Registration failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("RegisterFragment", "Error creating user", task.exception)
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

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