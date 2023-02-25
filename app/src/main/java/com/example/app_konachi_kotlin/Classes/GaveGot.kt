package com.example.app_konachi_kotlin.Classes

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GaveGot(val creditorkey:String, val type: String, val time: String, var amount: Double) {




    /*@RequiresApi(Build.VERSION_CODES.O)
    val now = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    @RequiresApi(Build.VERSION_CODES.O)
    val formattedDateTime = now.format(formatter)
    @RequiresApi(Build.VERSION_CODES.O)*/
    constructor() : this("","","2023-2-19",0.0)
}