package com.example.uas_movie.Database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.uas_movie.MovieAdminData

@Dao
interface MovieDao {
    // Mengambil semua data dari tabel MovieLocal
    @Query("SELECT * FROM MovieLocal")
    fun getAllMovies(): List<MovieEntity_Local>

    // Menyisipkan daftar entitas MovieLocal ke dalam tabel
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(localmovie: List<MovieEntity_Local>)

    // Menghapus semua data dari tabel MovieLocal
    @Query("DELETE FROM MovieLocal")
    fun deleteMovies()

    // Mengambil data entitas MovieLocal berdasarkan ID tertentu
    @Query("SELECT * FROM MovieLocal WHERE id = :id")
    fun getMovieById(id: String): MovieEntity_Local?
}
