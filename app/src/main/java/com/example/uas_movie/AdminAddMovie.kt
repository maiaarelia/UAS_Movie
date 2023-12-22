package com.example.uas_movie

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.example.uas_movie.databinding.ActivityAdminAddMovieBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class AdminAddMovie : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddMovieBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val movieCollection = firestore.collection("Movie")
    private val storageReference = FirebaseStorage.getInstance().getReference("images")
    private var imgPath: Uri? = null
    private val channelId = "TEST_NOTIF"
    private val notifId = 90


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Temukan ImageViewBack dengan ID imageViewBack
        val imageViewBack: ImageView = findViewById(R.id.imageViewBack)

        // Tambahkan onClickListener untuk menangani klik pada gambar kembali
        imageViewBack.setOnClickListener {
            // Buat intent untuk memulai Admin Home Activity
            val intent = Intent(this@AdminAddMovie, AdminHome::class.java)

            // Mulai aktivitas baru
            startActivity(intent)

            // Selesaikan aktivitas saat ini (AdminAddMovie)
            finish()
        }


        // Create the notification channel
        createNotificationChannel()

        // Ambil data yang ada dari intent
        val selectedMovie = intent.getSerializableExtra("selectedMovie") as? MovieAdminData

        with(binding) {
            // Handle image upload button click
            buttonUploadImage.setOnClickListener {
                val Img = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(Img, 0)
            }

            // Handle submit button click
            buttonSubmit.setOnClickListener {
                // Ambil data dari form
                val title = inputMovie.text.toString()
                val genre = genreMovie.text.toString()
                val duration = durationMovie.text.toString()
                val rating = ratingMovie.text.toString()
                val deskripsi = deskripsiMovie.text.toString()

                if (imgPath != null) {
                    // If a new image is selected, upload it and add the new movie
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
                                addMovie(movie)
                                navigateToAdminHome(movie)
                            }
                        }
                } else {
                    // Handle the case where no new image is selected
                    val movie = MovieAdminData(
                        title = title,
                        genre = genre,
                        duration = duration,
                        rating = rating,
                        deskripsi = deskripsi,
                        imageUrl = ""
                    )
                    addMovie(movie)
                    navigateToAdminHome(movie)
                }
            }

        }
    }

    private fun addMovie(movie: MovieAdminData) {
        // Add the movie to Firestore
        movieCollection.add(movie)
            .addOnSuccessListener { documentReference ->
                val createdMovieId = documentReference.id
                movie.id = createdMovieId
                documentReference.set(movie)
                    .addOnSuccessListener {
                        showNotification()
                    }
            }
            .addOnFailureListener {
                Log.d("AdminAddMovieActivity", "Error adding movie: ", it)
            }
    }

    private fun navigateToAdminHome(movie: MovieAdminData) {
        // Navigate back to AdminHome and pass the new movie data
        val intent = Intent(this@AdminAddMovie, AdminHome::class.java)
        intent.putExtra("movieData", movie)
        startActivity(intent)
    }


    // Membuat notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Menampilkan notification bahwa data berhasil ditambahkan
    private fun showNotification() {
        val notification = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notifImage = BitmapFactory.decodeResource(
            resources, R.drawable.baseline_notifications_active_24
        )

        val builder = NotificationCompat.Builder(this@AdminAddMovie, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle("Notification")
            .setContentText("Data added successfully")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(notifImage)
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notification.notify(notifId, builder.build())
    }

    // Menangani hasil dari pemilihan gambar dari galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imgPath = data?.data
            Glide.with(this).load(imgPath).into(binding.imageViewMovie)
        }
    }
}