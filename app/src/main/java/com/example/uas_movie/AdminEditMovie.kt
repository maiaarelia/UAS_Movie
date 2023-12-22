// AdminEditMovie.kt

// Import necessary packages
package com.example.uas_movie

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uas_movie.databinding.ActivityAdminEditMovieBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

// Class definition for AdminEditMovie activity
class AdminEditMovie : AppCompatActivity() {
    // Declare and initialize variables for data binding, Firestore, and storage
    private lateinit var binding: ActivityAdminEditMovieBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val movieCollection = firestore.collection("Movie")
    private val storageReference = FirebaseStorage.getInstance().getReference("images")

    // Declare variables for image URI, movie ID for updating, and selected image URI
    private lateinit var imageUri: Uri
    private var updateId = ""
    private var imgPath: Uri? = null

    // Override the onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using data binding
        binding = ActivityAdminEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Temukan ImageViewBack dengan ID imageViewBack
        val imageViewBack: ImageView = findViewById(R.id.imageViewBack)

        // Tambahkan onClickListener untuk menangani klik pada gambar kembali
        imageViewBack.setOnClickListener {
            // Buat intent untuk memulai Admin Home Activity
            val intent = Intent(this@AdminEditMovie, AdminHome::class.java)

            // Mulai aktivitas baru
            startActivity(intent)

            // Selesaikan aktivitas saat ini (AdminAddMovie)
            finish()
        }

        // Get the selected movie from the intent
        val selectedMovie = intent.getSerializableExtra("selectedMovie") as? MovieAdminData

        // Use data binding to simplify UI interactions
        with(binding) {
            if (selectedMovie != null) {
                // If there is a selected movie, populate the form for editing
                inputMovie.setText(selectedMovie.title)
                genreMovie.setText(selectedMovie.genre)
                durationMovie.setText(selectedMovie.duration)
                ratingMovie.setText(selectedMovie.rating)
                deskripsiMovie.setText(selectedMovie.deskripsi)

                // Set the image using Glide (assuming you have the imageUrl in MovieAdminData)
                Glide.with(this@AdminEditMovie).load(selectedMovie.imageUrl).into(imageViewMovie)

                // Save the ID for updating
                updateId = selectedMovie.id
            }

            // Handle image upload button click
            buttonUploadImage.setOnClickListener {
                val Img = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(Img, 0)
            }

            // Handle submit button click
            buttonSubmit.setOnClickListener {
                // Retrieve form data
                val title = inputMovie.text.toString()
                val genre = genreMovie.text.toString()
                val duration = durationMovie.text.toString()
                val rating = ratingMovie.text.toString()
                val deskripsi = deskripsiMovie.text.toString()

                if (imgPath != null) {
                    // If a new image is selected, upload it and update the movie
                    storageReference.putFile(imgPath!!)
                        .addOnSuccessListener {
                            storageReference.downloadUrl.addOnSuccessListener {
                                val imageFile = it.toString()
                                val movie = MovieAdminData(
                                    id = updateId,
                                    title = title,
                                    genre = genre,
                                    duration = duration,
                                    rating = rating,
                                    deskripsi = deskripsi,
                                    imageUrl = imageFile
                                )
                                updateMovie(movie)
                                navigateToAdminHome(movie)
                            }
                        }
                } else {
                    // If no new image is selected, update the movie without changing the image
                    val movie = MovieAdminData(
                        id = updateId,
                        title = title,
                        genre = genre,
                        duration = duration,
                        rating = rating,
                        deskripsi = deskripsi,
                        imageUrl = selectedMovie?.imageUrl ?: ""
                    )
                    updateMovie(movie)
                    navigateToAdminHome(movie)
                }
            }
        }
    }

    // Function to update movie details in Firestore
    private fun updateMovie(movie: MovieAdminData) {
        // Update the movie in Firestore based on the ID
        movieCollection.document(movie.id).set(movie)
            .addOnFailureListener {
                Log.d("AdminEditMovieActivity", "Error updating movie: ", it)
            }
    }

    // Function to navigate back to AdminHome and pass the updated movie data
    private fun navigateToAdminHome(movie: MovieAdminData) {
        val intent = Intent(this@AdminEditMovie, AdminHome::class.java)
        intent.putExtra("movieData", movie)
        startActivity(intent)
    }

    // Override the onActivityResult method to handle image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imgPath = data?.data
            Glide.with(this).load(imgPath).into(binding.imageViewMovie)
        }
    }
}
