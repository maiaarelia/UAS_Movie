package com.example.uas_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.uas_movie.databinding.ActivityDetailMovieBinding

class DetailMovie : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive data from Intent
        val movieTitle = intent.getStringExtra("MOVIE_TITLEAAAAA")
        val movieRating = intent.getStringExtra("MOVIE_RATING")
        val movieDuration = intent.getStringExtra("MOVIE_DURATION")
        val movieGenre = intent.getStringExtra("MOVIE_GENRE")
        val movieDescription = intent.getStringExtra("MOVIE_DESCRIPTION")
        val movieImageUrl = intent.getStringExtra("MOVIE_IMAGE_URL")
        // Temukan ImageViewBack dengan ID imageViewBack
        val imageViewBack: ImageView = findViewById(R.id.imageViewBack)

        // Tambahkan onClickListener untuk menangani klik pada gambar kembali
        imageViewBack.setOnClickListener {
            // Buat intent untuk memulai Admin Home Activity
            val intent = Intent(this@DetailMovie, UserHome::class.java)

            // Mulai aktivitas baru
            startActivity(intent)

            // Selesaikan aktivitas saat ini (AdminAddMovie)
            finish()
        }

        // Update UI with received data
        binding.movieTitle.text = movieTitle
        binding.ratingView.text = movieRating.toString()
        binding.viewDuration.text = "$movieDuration min"
        binding.viewGenre.text = movieGenre
        binding.textViewDeskripsi.text = movieDescription
        Glide.with(this)
            .load(movieImageUrl)
            .placeholder(R.drawable.default_poster) // Placeholder image while loading
            .error(R.drawable.bg_movie) // Image to display in case of error
            .into(binding.movieImage)
    }



}
