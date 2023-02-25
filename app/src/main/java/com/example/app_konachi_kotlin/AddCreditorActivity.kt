package com.example.app_konachi_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.app_konachi_kotlin.Classes.Creditor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AddCreditorActivity : AppCompatActivity() {
    lateinit var name : EditText;
    lateinit var phone : EditText;
    lateinit var address : EditText;
    lateinit var email : EditText;
    lateinit var AddCreditor : Button;
    lateinit var auth: FirebaseAuth;
    lateinit var back : ImageView;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_creditor)
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        email = findViewById(R.id.emailCreditor);
        AddCreditor = findViewById(R.id.AddCreditor)
        back = findViewById(R.id.back)


        AddCreditor.setOnClickListener(){
            AddCreditorFun()
        }
        back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun AddCreditorFun(){

        val  nameValue = name.text.toString()
        val phoneValue =phone.text.toString()
        val addressValue =address.text.toString()
        val emailValue =email.text.toString()

        var database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        if (nameValue.isBlank() || emailValue.isBlank() || addressValue.isBlank() || phoneValue.isBlank()){
            Toast.makeText(this, "Complete All Case ", Toast.LENGTH_SHORT).show()

        }else {

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("creditors")
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid
            val key = myRef.push().key
            val creditor = Creditor(userId.toString(),nameValue, emailValue, phoneValue, addressValue, 0)

            if (isEmailValid(emailValue)){
            myRef.child(key!!).setValue(creditor).addOnCompleteListener() {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Creditor Added Successfully ", Toast.LENGTH_SHORT).show()
                    name.text = null
                    phone.text = null
                    address.text = null
                    email.text = null
                    val intent = Intent(this, SecendMain::class.java)
                    intent.putExtra("creditorKey", key)
                    this.startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
            }else{
                Toast.makeText(this, "Enter   Correct Email Format", Toast.LENGTH_SHORT).show()
            }


        }

    }

        fun isEmailValid(email: String): Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }
}
