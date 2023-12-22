package com.example.uas_movie

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotifReceiver:  BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = intent?.getStringExtra("MESSAGE")
        if (msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }
}



//Kelas NotifReceiver pada kode
//yang yang memungkinkan aplikasi menerima dan menanggapi
//pesan dari sistem atau aplikasi lain. Dalam hal ini, kelas NotifReceiver
//berfungsi sebagai penerima broadcast yang akan menanggapi suatu pesan yang dikirimkan oleh sistem