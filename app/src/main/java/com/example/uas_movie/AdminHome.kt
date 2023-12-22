package com.example.uas_movie

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_movie.databinding.ActivityAdminHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class AdminHome : AppCompatActivity(), MovieItemClickListener {
    private lateinit var binding: ActivityAdminHomeBinding
    private lateinit var itemAdapterMovie: AdminMovieAdapter
    private lateinit var itemListMovie: ArrayList<MovieAdminData>
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var database : DatabaseReference
    private val firestore = FirebaseFirestore.getInstance()
    private val moviesCollection = firestore.collection("Movie")
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView
        recyclerViewItem = binding.rvMovie
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        // Inisialisasi ArrayList dan Adapter untuk RecyclerView
        itemListMovie = arrayListOf()
        itemAdapterMovie = AdminMovieAdapter(itemListMovie, this)
        recyclerViewItem.adapter = itemAdapterMovie

        // Inisialisasi PrefManager
        prefManager = PrefManager.getInstance(this@AdminHome)

        // Set onClickListener untuk FloatingActionButton (tambah movie)
        binding.fabTambah.setOnClickListener {
            startActivity(Intent(this, AdminAddMovie::class.java))
        }

        // Ambil data movie dari Firestore
        moviesCollection.get().addOnSuccessListener { querySnapshots ->
            val movies = ArrayList<MovieAdminData>()

            for (doc in querySnapshots) {
                val movie = doc.toObject(MovieAdminData::class.java)
                movies.add(movie)
            }

            // Set data ke dalam adapter dan refresh tampilan RecyclerView
            itemAdapterMovie.setData(movies)
            itemAdapterMovie.notifyDataSetChanged()
        }

        // Set Toolbar sebagai ActionBar
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    // Implementasi interface MovieItemClickListener untuk meng-handle event klik pada item RecyclerView
    override fun onEditButtonClick(movie: MovieAdminData) {
        val intent = Intent(this, AdminEditMovie::class.java)
        intent.putExtra("selectedMovie", movie)
        startActivity(intent)
    }

    // Implementasi interface MovieItemClickListener untuk meng-handle event klik tombol delete pada item RecyclerView
    override fun onDeleteButtonClick(movie: MovieAdminData) {
        // Munculkan dialog konfirmasi sebelum menghapus
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete this movie?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                deleteMovieFromDatabase(movie)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Delete Movie")
        alert.show()
    }

    // Hapus data movie dari Firestore
    private fun deleteMovieFromDatabase(movie: MovieAdminData) {
        moviesCollection.document(movie.id)
            .delete()
            .addOnSuccessListener {
                // Hapus item dari RecyclerView dan refresh tampilan
                itemListMovie.remove(movie)
                itemAdapterMovie.notifyDataSetChanged()
                Toast.makeText(this, "Movie deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete movie", Toast.LENGTH_SHORT).show()
            }
    }

    // Inflate menu dan tambahkan ke ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_admin, menu)
        return true
    }

    // Handle item yang dipilih pada menu ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Logout dan kembalikan ke halaman login
                FirebaseAuth.getInstance().signOut()
                prefManager.setLoggedIn(false)
                val intent = Intent(this@AdminHome, LoginRegisterTabLayout::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
