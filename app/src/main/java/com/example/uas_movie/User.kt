package com.example.uas_movie

data class User (
    var id: String = "",
    var email: String = "",
    var username: String = "",
    var role: String = "user",
    var nomor_telp: String = "",
    var password: String = "",
    var passwordUlang: String = ""
)