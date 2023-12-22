package com.example.uas_movie.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity_Local::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase() {
    // Mendeklarasikan metode abstrak untuk mendapatkan objek MovieDao
    abstract fun movieDao(): MovieDao?

    // Mendeklarasikan objek MovieDB sebagai singleton
    companion object {
        @Volatile
        private var INSTANCE: MovieDB? = null

        // Metode untuk mendapatkan instance tunggal dari MovieDB
        fun getDatabase(context: Context): MovieDB? {
            // Melakukan pengecekan apakah instance MovieDB sudah dibuat
            if (INSTANCE == null) {
                // Synchronized block untuk memastikan bahwa hanya satu thread yang dapat membuat instance
                synchronized(MovieDB::class.java) {
                    // Membuat instance MovieDB menggunakan databaseBuilder
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDB::class.java, "MovieLocal"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
