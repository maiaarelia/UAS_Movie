package com.example.uas_movie.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Mendeklarasikan kelas sebagai entitas untuk tabel database
@Entity(tableName = "MovieLocal")
data class MovieEntity_Local (
    // Menandai field sebagai primary key untuk tabel
    @PrimaryKey
    val id: String,
    // Field-field yang merepresentasikan kolom-kolom di dalam tabel
    val title : String,
    val genre : String,
    val duration : String,
    val rating : String,
    val deskripsi : String,
    val imageUrl : String
)
