package com.example.app_konachi_kotlin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.app_konachi_kotlin.Classes.GaveGot
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Update : AppCompatActivity() {
    lateinit var namecreditor : TextView;
    lateinit var type_up : View
    lateinit var type_down : View
    lateinit var igavegot : TextView
    lateinit var timew : TextView
    lateinit var dh : TextView
    lateinit var amountw : TextView
    lateinit var delete : Button;
    lateinit var update : Button;
    lateinit var back_to_home : ImageView;


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        type_up = findViewById(R.id.type_up)
        type_down = findViewById(R.id.type_down)
        igavegot = findViewById(R.id.igavegot)
        timew = findViewById(R.id.time)
        amountw = findViewById(R.id.amount)
        dh = findViewById(R.id.amount)
        delete = findViewById(R.id.delete);
        back_to_home = findViewById(R.id.back_to_home);

        back_to_home.setOnClickListener(){

        }
        delete.setOnClickListener(){
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("GaveGot")
            val gavegotKey = intent.getStringExtra("gavegotKey")

            if (gavegotKey != null) {
                myRef.child(gavegotKey).removeValue()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        namecreditor = findViewById(R.id.namecreditor)
        val database = FirebaseDatabase.getInstance();

        val myRef = database.getReference("creditors")
        val creditorKey = intent.getStringExtra("creditorKey")


        if (creditorKey != null) {
            myRef.child(creditorKey!!).child("name").addValueEventListener(object:
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //gavegot.clear()
                    val name = dataSnapshot.getValue(String::class.java)

                    namecreditor.text = name.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
        }

        val myRef1 = database.getReference("GaveGot")
        val gavegotKey = intent.getStringExtra("gavegotKey")
        if (gavegotKey != null) {
            myRef1.child(gavegotKey).addValueEventListener(object:
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val gavegot = dataSnapshot.getValue(GaveGot::class.java)
                    val type  = gavegot?.type;
                    val time = gavegot?.time;
                    val amount = gavegot?.amount;
                    if (type == "gave"){
                        type_down.visibility = View.GONE
                        type_up.visibility = View.VISIBLE
                        igavegot.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                        amountw.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                        dh.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                        igavegot.text  = type;
                        amountw.text = amount.toString();
                        timew.text = time;
                    }else{
                        type_down.visibility = View.VISIBLE
                        type_up.visibility = View.GONE
                        igavegot.setTextColor(ContextCompat.getColor(applicationContext, R.color.verre))
                        amountw.setTextColor(ContextCompat.getColor(applicationContext, R.color.verre))
                        dh.setTextColor(ContextCompat.getColor(applicationContext, R.color.verre))
                        igavegot.text  = type;
                        amountw.text = amount.toString();
                        timew.text = time;
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}