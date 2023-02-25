package com.example.app_konachi_kotlin

import android.Manifest
import android.Manifest.permission.CALL_PHONE
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_konachi_kotlin.Classes.Creditor
import com.example.app_konachi_kotlin.Classes.CreditorAdapter
import com.example.app_konachi_kotlin.Classes.GaveGot
import com.example.app_konachi_kotlin.Classes.GaveGotAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.FileOutputStream
import kotlin.math.absoluteValue


class SecendMain : AppCompatActivity() {
    lateinit var i_gave: Button;
    lateinit var i_got: Button
    lateinit var namecreditor : TextView;
    var  amounttotal : Int  = 0;
    lateinit var dh :TextView;
    lateinit var gavegot :TextView;
    lateinit var topgot : View;
    lateinit var downgave : View;
    lateinit var back_to_home : View;
    lateinit var totalamount : TextView;
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GaveGotAdapter
    lateinit var creditorKey1: String
    val gavegotKeyList = mutableListOf<String>()
    private val gavegots = mutableListOf<GaveGot>()
    lateinit var btnCall : Button;
    lateinit var btnMail : Button;
    var callNumber = "212"
    var mailSend ="mohamed@gmail.com"


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secend_main)
        i_gave = findViewById(R.id.i_gave);
        i_got = findViewById(R.id.i_got);
        back_to_home = findViewById(R.id.back_to_home);
        dh = findViewById(R.id.dh);
        gavegot = findViewById(R.id.gavegot);
        totalamount = findViewById(R.id.totalamount)
        namecreditor = findViewById(R.id.namecreditor);
        btnCall =findViewById(R.id.btnCall);
        btnMail =findViewById(R.id.btnMail);


        val database = FirebaseDatabase.getInstance();
        val myRef = database.getReference("creditors")
        val creditorKey1 = intent.getStringExtra("creditorKey")
        back_to_home.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        i_gave.setOnClickListener() {
            val intent = Intent(this, Calcule_Activity::class.java)
            intent.putExtra("creditorKey", creditorKey1)
            this.startActivity(intent)
            finish()
        }
        i_got.setOnClickListener() {
            val intent = Intent(this, Calculate_Got::class.java)
            intent.putExtra("creditorKey", creditorKey1)
            this.startActivity(intent)
            finish()
        }

        var creditorKey = intent.getStringExtra("creditorKey")
         if (creditorKey != null) {
            myRef.child(creditorKey).addValueEventListener(object:
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //gavegot.clear()
                    val creditor = dataSnapshot.getValue(Creditor::class.java)

                    namecreditor.text = creditor?.name.toString()
                    callNumber = creditor?.phone.toString()
                     mailSend = creditor?.email.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
        }

        btnCall.setOnClickListener(){
            val creditorPhoneNumber = callNumber
            // Create an Intent to start the phone app and call the creditor's phone number
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$creditorPhoneNumber")

            // Check if the CALL_PHONE permission is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // Start the phone app with the intent
                startActivity(intent)
            } else {
                // Request the CALL_PHONE permission if it is not granted
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
        }
        btnMail.setOnClickListener(){
            val creditorEmail = mailSend
            val amount = amounttotal.absoluteValue

            // Create image
            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            //border
            paint.color = Color.BLUE
            paint.strokeWidth = 10f
            canvas.drawRect(10f, 10f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

// Draw background color
            paint.color = Color.WHITE
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

// Calculate center coordinates of canvas
            val centerX = canvas.width / 2f
            val centerY = canvas.height / 2f

// Draw text
            paint.color = Color.BLACK
            paint.textSize = 30f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("Your credit is", centerX, centerY - 30f, paint)
            paint.color = Color.RED
            paint.textSize = 30f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("$amount DH", centerX, centerY + 10f, paint)
            paint.color = Color.BLACK
            paint.textSize = 30f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("Please pay it", centerX, centerY + 50f, paint)

            // Draw line
            paint.color = Color.GRAY
            paint.strokeWidth = 2f
            canvas.drawLine(0f, centerY + 80f, canvas.width.toFloat(), centerY + 80f, paint)

            paint.color = Color.BLACK
            paint.textSize = 30f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("This Nessage Generate By", centerX, centerY + 130f, paint)
            paint.color = Color.BLUE
            paint.textSize = 30f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("App Konachi", centerX, centerY + 170f, paint)

            // Save the bitmap to a file
            val file = File(getExternalFilesDir(null), "credit.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()


            val uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", file)
            // End Create Image

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(creditorEmail))
                putExtra(Intent.EXTRA_SUBJECT, "Balance of Credit From App Konachi")
                putExtra(Intent.EXTRA_TEXT, "Your Credit is $amount DH Please Pay it")
                putExtra(Intent.EXTRA_STREAM, uri)
            }

            // Check if there is an email app installed on the device
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            } else {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }




        this.creditorKey1 = intent.getStringExtra("creditorKey").toString()
        totalAmount()

        recyclerView = findViewById(R.id.recycler_view_secend)
        loadCreditors()



    }





    private fun loadCreditors() {
        val myRef = FirebaseDatabase.getInstance().reference.child("GaveGot")
        myRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gavegots.clear()

                gavegotKeyList.clear()
                for (snapshot in dataSnapshot.children) {
                    val gavegotKey = snapshot.key
                    val gavegot = snapshot.getValue(GaveGot::class.java)
                    if (gavegot != null && gavegot.creditorkey == creditorKey1) {
                        gavegots.add(gavegot!!)
                        gavegotKeyList.add(gavegotKey!!)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CreditorListActivity", "Failed to read creditors", error.toException())
            }
        })
        adapter = GaveGotAdapter(this,this, gavegots,gavegotKeyList,creditorKey1)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun totalAmount() {
        val myRef = FirebaseDatabase.getInstance().reference.child("GaveGot")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gavegots.clear()
                val creditorKey1 = intent.getStringExtra("creditorKey")
                gavegotKeyList.clear()
                amounttotal = 0
                for (snapshot in dataSnapshot.children) {
                    val gavegotKey = snapshot.key

                    val gavegot = snapshot.getValue(GaveGot::class.java)
                    if (gavegot != null && gavegot.creditorkey == creditorKey1) {
                        if(gavegot.type == "gave"){
                            amounttotal -= gavegot.amount.toInt()
                        }
                        if(gavegot.type == "got"){
                            amounttotal += gavegot.amount.toInt()
                        }
                        /*gavegots.add(gavegot!!)
                        gavegotKeyList.add(gavegotKey!!)*/
                    }
                }
                if (amounttotal >= 0){
                    totalamount.text = amounttotal.toString()
                    gavegot.text = "I Got"
                    totalamount.setTextColor(ContextCompat.getColor(applicationContext, R.color.verre))
                    gavegot.setTextColor(ContextCompat.getColor(applicationContext, R.color.verre))
                    dh.setTextColor(ContextCompat.getColor(applicationContext, R.color.verre))

                }else{
                    totalamount.text = amounttotal.absoluteValue.toString()
                    gavegot.text = "I Gave"
                    totalamount.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                    gavegot.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                    dh.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                }
                btnMail.isEnabled = amounttotal < 0
                val database = FirebaseDatabase.getInstance();
                val myRef = database.getReference("creditors")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CreditorListActivity", "Failed to read creditors", error.toException())
            }
        })
    }
}


