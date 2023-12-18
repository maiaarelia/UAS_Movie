package com.example.uas_movie

import java.io.Serializable

data class MovieAdminData (
    var id: String = "",
    val title : String?= null,
    val genre : String?= null,
    val duration : String?= null,
    val rating : String?= null,
    val deskripsi : String?= null,
    val imageUrl : String?= null
) : Serializable



