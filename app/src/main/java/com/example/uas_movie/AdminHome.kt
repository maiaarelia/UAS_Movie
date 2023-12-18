package com.example.uas_movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_movie.databinding.ActivityAdminHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class AdminHome : AppCompatActivity(), MovieItemClickListener  {
    private lateinit var binding: ActivityAdminHomeBinding
    private lateinit var itemAdapterMovie: AdminMovieAdapter
    private lateinit var itemListMovie: ArrayList<MovieAdminData>
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var database : DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private  val firestore = FirebaseFirestore.getInstance()
    private val moviesCollection = firestore.collection("Movie")


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewItem = binding.rvMovie
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemListMovie = arrayListOf()
        itemAdapterMovie = AdminMovieAdapter(itemListMovie, this)
        recyclerViewItem.adapter = itemAdapterMovie

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        binding.fabTambah.setOnClickListener{
            startActivity(Intent(this, AdminAddMovie::class.java))
        }

        database = FirebaseDatabase.getInstance().getReference("Movie")


        moviesCollection.get().addOnSuccessListener {
                querySnapshots -> val movies = ArrayList<MovieAdminData>()

            for (doc in querySnapshots){
                val movie = doc.toObject(MovieAdminData::class.java)
                movies.add(movie)


            }
            itemAdapterMovie.setData(movies)
            itemAdapterMovie.notifyDataSetChanged()

        }


        // Inisialisasi FloatingActionButton
        val fabTambah: FloatingActionButton = findViewById(R.id.fab_tambah)

        // Menambahkan listener untuk FloatingActionButton
        fabTambah.setOnClickListener {
            // Intent untuk memulai aktivitas tambah movie
            val intent = Intent(this, AdminAddMovie::class.java)
            startActivity(intent)
        }
        setSupportActionBar(findViewById(R.id.toolbar))
    }


    override fun onEditButtonClick(movie: MovieAdminData) {
        // Handle Edit button click, navigate to AdminAddMovie with movie details
        val intent = Intent(this, AdminAddMovie::class.java)
        intent.putExtra("selectedMovie", movie) // Pass movie details to AdminAddMovie
        startActivity(intent)
    }

    override fun onDeleteButtonClick(movie: MovieAdminData) {
        // Handle Delete button click (if needed)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_admin, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                FirebaseAuth.getInstance().signOut()
                saveLoginStatus(false)

                // Add any additional logic, such as returning to the login screen
                val intent = Intent(this, LoginRegisterTabLayout::class.java)
                startActivity(intent)
                finish()
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

