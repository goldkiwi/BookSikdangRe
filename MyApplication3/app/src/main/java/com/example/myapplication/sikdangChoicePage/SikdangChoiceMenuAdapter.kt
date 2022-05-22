package com.example.myapplication.sikdangChoicePage

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.example.myapplication.storeActivity.StoreActivity
import com.example.myapplication.storeActivity.StoreMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SikdangChoiceMenuAdapter (
    var context: Context,
    var sikdangList : ArrayList<SikdangStoreMenu> = arrayListOf()
) : RecyclerView.Adapter<SikdangChoiceMenuAdapter.Holder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.sikdangchoice_menuline, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val store = sikdangList[position]

        holder.sikdangName.text = store.name
        Glide.with(context).load(store.store_image).into(holder.sikdangImage)
        holder.sikdangChoice_menuLine_dist.text = store.dist.toString()
        holder.itemView.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Restaurants")
                    .child(store.store_type)
                    .child(store.id)
                    .child("info")
                    .addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val store_info = snapshot.getValue(StoreInfo::class.java)
                            val _intent = Intent(context, StoreActivity::class.java)
                            _intent.putExtra("store_info", store_info)
                            Log.d("store_info", "${store_info}")
                            context.startActivity(_intent)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
        }
    }

    override fun getItemCount(): Int {
        return sikdangList.size
    }

    inner class Holder(itemView : View) :RecyclerView.ViewHolder(itemView) {
        var sikdangChoice_menuLine : LinearLayout

        var sikdangImage : ImageView
        var sikdangName : TextView

        var sikdangChoice_menuLine_dist : TextView
        var sikdangChoice_menuLine_park : TextView
        var sikdangChoice_menuLine_event : TextView

        var reqMenu1 : TextView
        var reqMenu2 : TextView
        var reqMenu3 : TextView
        var reqMenu4 : TextView

        init {
            sikdangChoice_menuLine = itemView.findViewById(R.id.sikdangChoice_menuLine)

            sikdangImage = itemView.findViewById(R.id.sikdangImage)
            sikdangName = itemView.findViewById(R.id.sikdangName)

            sikdangChoice_menuLine_dist = itemView.findViewById(R.id.sikdangChoice_menuLine_dist)
            sikdangChoice_menuLine_park = itemView.findViewById(R.id.sikdangChoice_menuLine_park)
            sikdangChoice_menuLine_event = itemView.findViewById(R.id.sikdangChoice_menuLine_event)

            reqMenu1 = itemView.findViewById(R.id.reqMenu1)
            reqMenu1.visibility = View.INVISIBLE
            reqMenu2 = itemView.findViewById(R.id.reqMenu2)
            reqMenu2.visibility = View.INVISIBLE
            reqMenu3 = itemView.findViewById(R.id.reqMenu3)
            reqMenu3.visibility = View.INVISIBLE
            reqMenu4 = itemView.findViewById(R.id.reqMenu4)
            reqMenu4.visibility = View.INVISIBLE
        }
    }
}