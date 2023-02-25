package com.example.app_konachi_kotlin.Classes


    import android.annotation.SuppressLint
    import android.app.Activity
    import android.app.AlertDialog
    import android.content.Context
    import android.content.Intent
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.EditText
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.app_konachi_kotlin.Calcule_Activity
    import com.example.app_konachi_kotlin.R
    import com.example.app_konachi_kotlin.SecendMain
    import com.example.app_konachi_kotlin.Update
    import com.google.firebase.database.FirebaseDatabase
    import kotlin.math.absoluteValue

class GaveGotAdapter(private val activity: Activity, private val context: Context, private val gavegots: List<GaveGot>, private val gavegotKeyList: List<String>, private val creditorKey :String) : RecyclerView.Adapter<GaveGotAdapter.GaveGotViewHolder>(){


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.name)
            val time: TextView = itemView.findViewById(R.id.time)

        }
        inner class GaveGotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val typegave = itemView.findViewById<TextView>(R.id.typegave)
            val typegot = itemView.findViewById<TextView>(R.id.typegot)
            val time = itemView.findViewById<TextView>(R.id.time)

            val topgot = itemView.findViewById<View>(R.id.topgot)
            val downgave = itemView.findViewById<View>(R.id.downgave)

            val amountgave = itemView.findViewById<TextView>(R.id.amountgave)
            val amountgot = itemView.findViewById<TextView>(R.id.amountgot)


        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GaveGotViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.secend_main_gave_got, parent, false)
            return GaveGotViewHolder(view)
        }

        override fun onBindViewHolder(holder: GaveGotViewHolder, position: Int) {
            val gavegot = gavegots[position]
            val gavegotKey = gavegotKeyList[position]

            if (gavegot.type == "got"){
                holder.typegot.text = gavegot.type;
                holder.typegave.text = "";

                holder.amountgave.text = ""
                holder.amountgot.text = gavegot.amount.toString()

                holder.topgot.visibility = View.VISIBLE;
                holder.downgave.visibility = View.GONE
            }else{
                holder.typegave.text = gavegot.type;
                holder.typegot.text = "";

                holder.amountgot.text = ""
                holder.amountgave.text = gavegot.amount.absoluteValue.toString()

                holder.topgot.visibility = View.GONE;
                holder.downgave.visibility = View.VISIBLE
            }

            holder.time.text = gavegot.time;


            holder.itemView.setOnClickListener {
                val intent = Intent(context, Update::class.java)
                intent.putExtra("gavegotKey", gavegotKey)
                intent.putExtra("creditorKey", creditorKey)
                context.startActivity(intent)
                activity.finish()

            }
        }
        override fun getItemCount(): Int {
            return gavegots.size
        }



    }

