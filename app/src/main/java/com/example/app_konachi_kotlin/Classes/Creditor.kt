package com.example.app_konachi_kotlin.Classes

import android.widget.EditText

data class Creditor(val userid : String, val name: String, val email : String, val phone: String, val address: String,
                    var amount:Int) {




    constructor() : this("","", "", "","",0)
}