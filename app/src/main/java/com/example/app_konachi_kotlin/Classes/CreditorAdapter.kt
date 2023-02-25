package com.example.app_konachi_kotlin.Classes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_konachi_kotlin.Calcule_Activity
import com.example.app_konachi_kotlin.R
import com.example.app_konachi_kotlin.SecendMain
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.absoluteValue


class CreditorAdapter(private val activity: Activity, private val context: Context, private val creditors: List<Creditor>, private val creditorKeyList: List<String>) : RecyclerView.Adapter<CreditorAdapter.CreditorViewHolder>(){

    @SuppressLint("MissingInflatedId")
    private fun showUpdateAmountDialog(creditor: Creditor, creditorKey: String) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.edit_amount_dialog, null)

        val amountEditText = view.findViewById<EditText>(R.id.amount_edit_text)
        amountEditText.setText(creditor.amount.toString())

        builder.setView(view)
            .setTitle("Update Amount")
            .setPositiveButton("Update") { dialog, which ->
                val newAmount = amountEditText.text.toString().toInt()
                creditor.amount = newAmount
                updateAmountInFirebase(creditorKey, newAmount)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

        builder.create().show()
    }
    private fun updateAmountInFirebase(creditorKey: String, newAmount: Int) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("creditors")
        myRef.child(creditorKey).child("amount").setValue(newAmount)
    }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val name: TextView = itemView.findViewById(R.id.name)
            val address: TextView = itemView.findViewById(R.id.address)

            val amountgot: TextView = itemView.findViewById(R.id.amountgot)
            val amountgave: TextView = itemView.findViewById(R.id.amountgave)

            val gave: TextView = itemView.findViewById(R.id.gavegot)
            val got: TextView = itemView.findViewById(R.id.got)


        }

    inner class CreditorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.name)

        val amount = itemView.findViewById<TextView>(R.id.amount)

        val dh = itemView.findViewById<TextView>(R.id.dh)

        val  firstChar = itemView.findViewById<Button>(R.id.firstChar);

        var igavegot = itemView.findViewById<TextView>(R.id.igavegot)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditorViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_laout, parent, false)
        return CreditorViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreditorViewHolder, position: Int) {
        val creditor = creditors[position]
        val creditorKey = creditorKeyList[position]



            val myRef = FirebaseDatabase.getInstance().reference.child("GaveGot")
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //gavegots.clear()
                    //val creditorKey1 = intent.getStringExtra("creditorKey")
                    //gavegotKeyList.clear()
                    var amounttotal = 0
                    var amount_total_got = 0;
                    var amount_total_gave = 0;
                    for (snapshot in dataSnapshot.children) {
                        val gavegotKey = snapshot.key
                        val gavegot = snapshot.getValue(GaveGot::class.java)
                        if (gavegot != null && gavegot.creditorkey == creditorKey) {
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
                        holder.igavegot.text ="i got ";
                        holder.igavegot.setTextColor(ContextCompat.getColor(context, R.color.verre))

                        holder.amount.text =amounttotal.toString();
                        holder.amount.setTextColor(ContextCompat.getColor(context, R.color.verre))
                        holder.dh.setTextColor(ContextCompat.getColor(context, R.color.verre))


                    }else{
                        holder.igavegot.text ="i gave ";
                        holder.igavegot.setTextColor(ContextCompat.getColor(context, R.color.red))

                        holder.amount.text =amounttotal.absoluteValue.toString();
                        holder.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
                        holder.dh.setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    val database = FirebaseDatabase.getInstance();
                    val myRef = database.getReference("creditors")

                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("CreditorListActivity", "Failed to read creditors", error.toException())
                }
            })



        holder.firstChar.text = creditor.name.trim().first().toString()

        holder.name.text = creditor.name.trim()

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SecendMain::class.java)
            intent.putExtra("creditorKey", creditorKey)
            context.startActivity(intent)
            //activity.finish()

        }
    }
    override fun getItemCount(): Int {
        return creditors.size
    }

}

