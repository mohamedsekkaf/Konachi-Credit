package com.example.app_konachi_kotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_konachi_kotlin.Classes.Creditor
import com.example.app_konachi_kotlin.Classes.CreditorAdapter
import com.example.app_konachi_kotlin.Classes.GaveGot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.math.absoluteValue


class MainActivity : AppCompatActivity() {

    lateinit var add: Button;
    lateinit var name : TextView;
    lateinit var amount : TextView;
    private lateinit var auth: FirebaseAuth
    lateinit var logout : ImageView;
    lateinit var searchinput : EditText;
    lateinit var search : Button ;
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CreditorAdapter
    private val creditors = mutableListOf<Creditor>()
    val creditorKeyList = mutableListOf<String>()

    lateinit var amount_total_got :TextView
    lateinit var amount_total_gave :TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add = findViewById(R.id.add);
        logout = findViewById(R.id.logout);
        amount_total_gave = findViewById(R.id.amount_total_gave)
        amount_total_got = findViewById(R.id.amount_total_got)
        search = findViewById(R.id.search)
        searchinput = findViewById(R.id.searchinput)
        if (!isNetworkConnected()) {
            Toast.makeText(this, "Please connect to the internet to use this app", Toast.LENGTH_LONG).show()
            finish() // Close the app
        }
        auth = FirebaseAuth.getInstance()

        logout.setOnClickListener() {
                auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (auth.currentUser == null) {
            redirectToLogin()
        }
        add.setOnClickListener() {
            val intent = Intent(this, AddCreditorActivity::class.java)
            startActivity(intent)
        }
        searchinput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString() ?: ""
                search(query)
            }
        })

        recyclerView = findViewById(R.id.recycler_view)
        loadCreditors()
        calculeTotalAmount()

    }
    private fun loadCreditors() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val creditorRef = FirebaseDatabase.getInstance().reference.child("creditors")

        creditorRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                creditors.clear()
                creditorKeyList.clear()
                for (snapshot in dataSnapshot.children) {
                    val creditorKey = snapshot.key
                    val creditor = snapshot.getValue(Creditor::class.java)
                    if (creditor != null && creditor.userid == userId) {
                        creditors.add(creditor!!)
                        creditorKeyList.add(creditorKey!!)
                       //creditorKey_to_balance = creditorKey;
                    }
                }
                var amount_total_got_result = 0;
                var amount_total_gave_result = 0;
                for (key in creditorKeyList) {
                    val myRef = FirebaseDatabase.getInstance().reference.child("GaveGot")
                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var amounttotal = 0

                            for (snapshot in dataSnapshot.children) {
                                val gavegotKey = snapshot.key
                                val gavegot = snapshot.getValue(GaveGot::class.java)
                                if (gavegot != null && gavegot.creditorkey == key) {
                                    if (gavegot.type == "gave") {
                                        amount_total_gave_result -= gavegot.amount.toInt()
                                    }
                                    if (gavegot.type == "got") {
                                        amount_total_got_result += gavegot.amount.toInt()
                                    }
                                }
                            }
                            amount_total_gave.text = amount_total_gave_result.absoluteValue.toString()
                            amount_total_got.text = amount_total_got_result.toString()
                            val database = FirebaseDatabase.getInstance();
                            val myRef = database.getReference("creditors")

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("CreditorListActivity", "Failed to read creditors", error.toException())
                        }
                    })
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CreditorListActivity", "Failed to read creditors", error.toException())
            }
        })

        adapter = CreditorAdapter(this,this, creditors, creditorKeyList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun search(query:String){
        val myRef = FirebaseDatabase.getInstance().reference.child("creditors")
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val creditorRef = FirebaseDatabase.getInstance().reference.child("creditors")

        creditorRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                creditors.clear()
                creditorKeyList.clear()
                for (snapshot in dataSnapshot.children) {
                    val creditorKey = snapshot.key
                    val creditor = snapshot.getValue(Creditor::class.java)
                    if (creditor != null && creditor.userid == userId && creditor.name.contains(query)) {
                        creditors.add(creditor!!)
                        creditorKeyList.add(creditorKey!!)
                        //creditorKey_to_balance = creditorKey;
                    }
                }


                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("CreditorListActivity", "Failed to read creditors", error.toException())
            }
        })

        adapter = CreditorAdapter(this,this, creditors, creditorKeyList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }

    private fun calculeTotalAmount() {

    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}





