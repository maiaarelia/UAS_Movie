package com.example.uas_movie

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.uas_movie.databinding.ActivityAdminAddMovieBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AdminAddMovie : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddMovieBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val movieCollection = firestore.collection("Movie")
    private val storageReference = FirebaseStorage.getInstance().getReference("images")
    private lateinit var imageUri: Uri
    private var updateId = ""
    private var imgPath: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Ambil data yang ada dari intent
        val selectedMovie = intent.getSerializableExtra("selectedMovie") as? MovieAdminData

        with(binding) {
            if (selectedMovie != null) {
                // Jika ada data complaint, set nilai awal untuk form editing
                inputMovie.setText(selectedMovie.title)
                genreMovie.setText(selectedMovie.genre)
                durationMovie.setText(selectedMovie.duration)
                ratingMovie.setText(selectedMovie.rating)
                deskripsiMovie.setText(selectedMovie.deskripsi)

                // Set the image using Glide (assuming you have the imageUrl in MovieAdminData)
                Glide.with(this@AdminAddMovie).load(selectedMovie.imageUrl).into(imageViewMovie)

                // Simpan ID untuk keperluan update
                updateId = selectedMovie.id
            }

            // masuk ke storage hp
            buttonUploadImage.setOnClickListener{
                val Img = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(Img, 0)
            }



            buttonSubmit.setOnClickListener {
                // Ambil data dari form
                val title = inputMovie.text.toString()
                val genre = genreMovie.text.toString()
                val duration = durationMovie.text.toString()
                val rating = ratingMovie.text.toString()
                val deskripsi = deskripsiMovie.text.toString()

                storageReference.putFile(imgPath!!)
                    .addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener {
                            val imageFile = it.toString()
                            val movie = MovieAdminData(
                                title = title,
                                genre = genre,
                                duration = duration,
                                rating = rating,
                                deskripsi = deskripsi,
                                imageUrl = imageFile
                            )
                            if (updateId.isNotEmpty()) {
                                // Jika ada updateId, berarti mode editing
                                movie.id = updateId
                                updateMovie(movie)
                            } else {
                                // Jika tidak ada updateId, berarti mode penambahan baru
                                addMovie(movie)
                            }
                            val intent = Intent(this@AdminAddMovie, AdminHome::class.java)
                            intent.putExtra("movieData", movie)
                            startActivity(intent)
                        }
                    }

                }
            }
        }


    private fun addMovie(movie: MovieAdminData) {
        // Add the complaint to Firestore
        movieCollection.add(movie)
            .addOnSuccessListener { documentReference ->
                val createdMovieId = documentReference.id
                movie.id = createdMovieId
                documentReference.set(movie)
                    .addOnFailureListener {
                        Log.d("AdminAddMovieActivity", "Error updating complaint ID: ", it)
                    }
            }
            .addOnFailureListener {
                Log.d("AdminAddMovieActivity", "Error adding movie: ", it)
            }
    }

    private fun updateMovie(movie: MovieAdminData) {
        // Update complaint di Firestore berdasarkan ID
        movieCollection.document(movie.id).set(movie)
            .addOnFailureListener {
                Log.d("AdminAddMovieActivity", "Error updating movie: ", it)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            imgPath = data?.data
            Glide.with(this).load(imgPath).into(binding.imageViewMovie)
        }
    }




    }
