package com.example.myapplication.bookmark

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.example.myapplication.storeActivity.StoreActivity
import com.google.firebase.database.*

class BookMarkAdapter (
    var context: Context,
    var store_list : ArrayList<StoreInfo>
) : RecyclerView.Adapter<BookMarkAdapter.Holder> ()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.bookmark_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: BookMarkAdapter.Holder, position: Int) {
        val store = store_list[position]

        FirebaseDatabase.getInstance().getReference("Review")
            .child(store.store_id!!)
            .child("info")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val rate = snapshot.getValue(UserReview::class.java)
                    if(rate == null) return
                    holder.cnt_review.text = rate.cnt_user.toString()
                    val rating = rate.rating!!.toFloat() / rate.cnt_user!!
                    holder.rating.text = "%.2f".format(rating)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("BookMarkAdapter", "${store.store_name} : ${error}")
                }
            })

        Glide.with(context).load(store.store_image).into(holder.store_image)
        holder.store_name.text = store.store_name
        holder.store_type.text = store.store_type
        holder.phone_number.text = store.phone_number
        holder.store_layout.setOnClickListener {
            val _intent = Intent(context, StoreActivity::class.java)
            _intent.putExtra("store_info", store)
            context.startActivity(_intent)
        }
    }

    override fun getItemCount(): Int {
        return store_list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var store_layout : LinearLayout
        var store_image : ImageView
        var store_name : TextView
        var store_type : TextView
        var rating : TextView
        var cnt_review : TextView
        var phone_number : TextView

        init {
            store_layout = itemView.findViewById(R.id.store_layout)
            store_image = itemView.findViewById(R.id.store_image)
            store_name = itemView.findViewById(R.id.store_name)
            store_type = itemView.findViewById(R.id.store_type)
            rating = itemView.findViewById(R.id.rating)
            cnt_review = itemView.findViewById(R.id.cnt_review)
            phone_number = itemView.findViewById(R.id.phone_number)
        }
    }

    @IgnoreExtraProperties
    data class UserReview(
        val cnt_user : Int? = null,
        val rating : Int? = null
    )
}