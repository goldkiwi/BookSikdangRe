package com.example.myapplication.mypage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.Store
import com.example.myapplication.bookhistory.BookHistory
import com.example.myapplication.dataclass.StoreInfo
import com.example.myapplication.storeActivity.EditReviewActivity
import com.example.myapplication.storeActivity.StoreActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PaymentAdapter (
    var context: Context,
    var payment_list : ArrayList<BookHistory>,
    var store_list : ArrayList<StoreInfo?>
) : RecyclerView.Adapter<PaymentAdapter.Holder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.payment_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val history = payment_list[position]
        val store = store_list[position]

        if (store == null) {
            FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(history.store_type)
                .child(history.sikdangId)
                .child("info")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d("onBindViewHolder", "${snapshot.key},")
                        val store = snapshot.getValue(StoreInfo::class.java)
                        if (store == null) return
                        store_list[position] = store
                        notifyItemChanged(position)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
        if(store == null) {
            holder.payment_layout.visibility = View.GONE
        } else {
            holder.payment_layout.visibility = View.VISIBLE
            holder.store_type_image
            holder.date.text = history.date
            holder.store_name.text = store.store_name
            holder.payment_content.text = compactcontentToString(history.hashMap)
            holder.total_pay.text = "${history.total_pay} 원"

            holder.write_review.setOnClickListener {
                var _intent = Intent(context, EditReviewActivity::class.java)
                _intent.putExtra("store_info", store!!)
                context.startActivity(_intent)
            }

            holder.view_store.setOnClickListener {
                var _intent = Intent(context, StoreActivity::class.java)
                _intent.putExtra("store_info", store!!)
                context.startActivity(_intent)
            }

            holder.view_detail_payment.setOnClickListener {
                Toast.makeText(context, "미구현", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return payment_list.size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var payment_layout : LinearLayout
        var store_type_image : ImageView
        var date : TextView
        var store_name : TextView
        var payment_content : TextView
        var total_pay : TextView
        var write_review : ImageView
        var view_store : ImageView
        var view_detail_payment : ImageView

        init {
            payment_layout = itemView.findViewById(R.id.payment_layout)
            store_type_image = itemView.findViewById(R.id.store_type_image)
            date = itemView.findViewById(R.id.date)
            store_name = itemView.findViewById(R.id.store_name)
            payment_content = itemView.findViewById(R.id.payment_content)
            total_pay = itemView.findViewById(R.id.total_pay)
            write_review = itemView.findViewById(R.id.write_review)
            view_store = itemView.findViewById(R.id.view_store)
            view_detail_payment = itemView.findViewById(R.id.view_detail_payment)
        }
    }

    private fun compactcontentToString(
        content : HashMap<String, ArrayList<Pair<String, Int>>>)
    : String{
        val first_item = content[content.keys.first()]!!.first().first
        var result = "${first_item}"
        var total = 0
        for (key in content.keys) {
            total += content[key].count()
        }
        val count = maxOf(content.count(), total)
        if(count > 1)
            result += " 외 ${count}"

        return result
    }
}