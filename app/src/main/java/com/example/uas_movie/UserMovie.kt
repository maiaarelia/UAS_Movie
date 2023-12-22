package com.example.uas_movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_movie.Database.MovieDB
import com.example.uas_movie.Database.MovieDao
import com.example.uas_movie.Database.MovieEntity_Local
import com.example.uas_movie.databinding.FragmentUserMovieBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserMovie : Fragment() {
    // Inisialisasi variabel binding untuk menghubungkan dengan layout XML menggunakan View Binding
    private lateinit var binding: FragmentUserMovieBinding

    // Inisialisasi adapter untuk RecyclerView
    private lateinit var itemAdapter: MovieUserAdapter

    // Inisialisasi ArrayList untuk menyimpan data movie
    private lateinit var itemList: ArrayList<MovieAdminData>

    // Inisialisasi RecyclerView
    private lateinit var recyclerViewItem: RecyclerView

    // Objek Firebase Firestore untuk mengakses database
    private val firestore = FirebaseFirestore.getInstance()

    // Referensi ke koleksi "Movie" di Firestore
    private val moviesCollection = firestore.collection("Movie")

    // Inisialisasi DAO (Data Access Object) untuk mengakses database lokal
    private lateinit var movieDao: MovieDao

    // ExecutorService untuk menjalankan operasi di latar belakang
    private lateinit var executorService: ExecutorService

    // Metode yang dipanggil saat tampilan fragment dibuat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menggunakan View Binding untuk menghubungkan layout
        binding = FragmentUserMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Metode yang dipanggil setelah tampilan fragment dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        recyclerViewItem = binding.rvMovie
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi ArrayList dan Adapter untuk RecyclerView
        itemList = arrayListOf()
        itemAdapter = MovieUserAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

        // Inisialisasi MovieDB dan MovieDao
        executorService = Executors.newSingleThreadExecutor()
        val db = MovieDB.getDatabase(requireContext())
        movieDao = db!!.movieDao()!!

        // Mendapatkan data dari Firestore dan menampilkan ke RecyclerView
        moviesCollection.get().addOnSuccessListener { querySnapshots ->
            val movies = ArrayList<MovieAdminData>()

            for (doc in querySnapshots) {
                val movie = doc.toObject(MovieAdminData::class.java)
                movies.add(movie)
            }
            itemAdapter.setData(movies)
            itemAdapter.notifyDataSetChanged()
        }

        // Memanggil fungsi untuk menyalin data dari Firestore ke database lokal
        with(binding) {
            fetchDataFromFirestore()
        }
    }

    // Fungsi untuk mengambil data dari Firestore dan menyalinnya ke database lokal
    private fun fetchDataFromFirestore() {
        Log.d("Firestore", "Masuk ke fungsi")

        moviesCollection.get().addOnSuccessListener { documents ->
            val movieModels = mutableListOf<MovieAdminData>()

            for (document in documents) {
                val movie = document.toObject<MovieAdminData>()
                Log.d("Firestore", "Hasil: $movie")
                movieModels.add(movie)
                Log.d("Firestore", "Sukses menambahkan data ke model")
            }

            // Mengonversi data dari model ke entitas dan menyalinnya ke database lokal menggunakan Coroutine
            val movieEntitites = convertToMovieEntity(movieModels)
            CoroutineScope(Dispatchers.IO).launch {
                movieDao.insert(movieEntitites)
                withContext(Dispatchers.Main) {
                    Log.d("Firestore", "Sukses menyalin data")
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Error getting document: $exception")
        }
    }

    // Fungsi untuk mengonversi data dari model ke entitas lokal
    private fun convertToMovieEntity(movieModels: List<MovieAdminData>): List<MovieEntity_Local> {
        val movieEntities = mutableListOf<MovieEntity_Local>()

        for (movieModel in movieModels) {
            val movieEntity = MovieEntity_Local(
                movieModel.id,
                movieModel.title,
                movieModel.genre,
                movieModel.duration,
                movieModel.rating,
                movieModel.deskripsi,
                movieModel.imageUrl
            )
            movieEntities.add(movieEntity)
            Log.d("Firestore", "Berhasil jadi object")
        }
        return movieEntities
    }
}
