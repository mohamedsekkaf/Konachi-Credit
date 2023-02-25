package com.example.app_konachi_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


public class RegisterActivity : AppCompatActivity() {
    private lateinit var name : String;
    private lateinit var email : EditText;
    private lateinit var password : EditText;
    lateinit var ConfirmPassword :EditText;
    lateinit var btnregister :Button;
    lateinit var Go_login :TextView;
    private lateinit var auth:FirebaseAuth;

    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        name ="mohamed"
        email = findViewById(R.id.email);
        password = findViewById(R.id.passowrd);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        btnregister  = findViewById(R.id.register)
        Go_login = findViewById(R.id.Go_login)

        auth = Firebase.auth
        Go_login.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnregister.setOnClickListener{
            RegisterUser()
        }
    }
    private fun RegisterUser() {
        val etemail = email.text.toString();
        val pass = password.text.toString();
        val cpass = ConfirmPassword.text.toString();
        if(etemail.isBlank() || pass.isBlank() || cpass.isBlank()){
            Toast.makeText(this, "Email and Password can't be blank",
                Toast.LENGTH_SHORT).show()
            return
        }
        if(pass != cpass ){
            Toast.makeText(this,"Password and Confirm Password do not match",
                Toast.LENGTH_SHORT).show()
        }
        auth.createUserWithEmailAndPassword(etemail,pass).addOnCompleteListener(RegisterActivity()){
            if (it.isSuccessful){
                Toast.makeText(this, "Successfully Register",
                    Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this, it.exception?.message,
                    Toast.LENGTH_SHORT).show()
            }
        }

    }
}




