package com.example.uas_movie

import java.io.Serializable

data class MovieAdminData (
    var id: String = "",
    val title : String = "",
    val genre : String= "",
    val duration : String= "",
    val rating : String= "",
    val deskripsi : String= "",
    val imageUrl : String= ""
) : Serializable



