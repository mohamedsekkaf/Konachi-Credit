package com.example.app_konachi_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var email : EditText;
    private lateinit var pass : EditText;
    lateinit var btnlogin : Button;
    lateinit var Go_register : TextView;
    private lateinit var auth: FirebaseAuth;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email);
        pass = findViewById(R.id.passowrd);
        btnlogin = findViewById(R.id.btnlogin);
        Go_register = findViewById(R.id.Go_register);

        auth = FirebaseAuth.getInstance();

        /*if (auth.currentUser != null) {
            redirectToMain()
        } else {
            // Set up your login UI here...
        }*/

        Go_register.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnlogin.setOnClickListener {

            login()
        }
    }

    private fun login() {
        val emails = email.text.toString()
        val passW = pass.text.toString()
        if(emails.isBlank() || passW.isBlank()){
            Toast.makeText(this, "Email and Password can't be blank",
                Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(emails, passW).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()

            } else
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}