package com.example.myapplication.bookhistory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.internal.readFieldOrNull
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookHistoryAdapter(
    var context: Context,
    var bookHistoryList : ArrayList<BookHistory>
) :RecyclerView.Adapter<BookHistoryAdapter.Holder>()
{
    var store_map = hashMapOf<String, StoreInfo>()
    lateinit var bookMenuAdapter : BookMenuAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookhist_line, parent, false)
        for (store in bookHistoryList) {
            getStoreInfo(store.sikdangId, store.store_type)
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val bookHistory = bookHistoryList[position]
        val sikdangId = bookHistory.sikdangId
        if(store_map.containsKey(sikdangId)) {
            holder.bookHistSikdangNameTV.text = store_map[sikdangId]!!.store_name
            holder.bookHistSikdangLocTV.text = store_map[sikdangId]!!.phone_number
            holder.bookDate.text = bookHistory.date
            holder.bookTime.text = bookHistory.book_time
        }
        Log.d("BookHistoryAdapter", "${bookHistory}")
        bookMenuAdapter = BookMenuAdapter(context, bookHistory)
        val bookMenuLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.bookMenuHistRV.adapter = bookMenuAdapter
        holder.bookMenuHistRV.layoutManager = bookMenuLayout
    }

    override fun getItemCount(): Int {
        return bookHistoryList.size
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookHistSikdangNameTV : TextView
        var bookHistSikdangLocTV : TextView
        var bookDate : TextView
        var bookTime : TextView
        var bookMenuHistRV : RecyclerView

        init {
            bookHistSikdangNameTV = itemView.findViewById(R.id.bookHistSikdangNameTV)
            bookHistSikdangLocTV = itemView.findViewById(R.id.bookHistSikdangLocTV)
            bookDate = itemView.findViewById(R.id.bookDate)
            bookTime = itemView.findViewById(R.id.bookTime)
            bookMenuHistRV = itemView.findViewById(R.id.bookMenuHistRV)
        }
    }


    inner class BookMenuAdapter(
        var context: Context,
        var bookHistory : BookHistory
    ) :RecyclerView.Adapter<BookMenuAdapter.InnerHolder>()
    {
        var table_names : ArrayList<String> = ArrayList(bookHistory.hashMap.keys)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
            var view =  LayoutInflater.from(context).inflate(R.layout.bookhistmenu_line, parent, false)
            Log.d("InnerAdapter", "${table_names}")
            return InnerHolder(view)
        }

        override fun onBindViewHolder(holder: InnerHolder, position: Int) {
            val sb = StringBuilder()
            val table = table_names[position]
            Log.d("InnerAdapter", "${table}")
            for (content in bookHistory.hashMap[table]!!) {
                sb.append("%40s : %2d".format(content.first, content.second))
                sb.append(System.getProperty("line.separator"))
            }
            holder.bookHist_table.text = table
            holder.bookHist_item.text = sb.toString()
        }

        override fun getItemCount(): Int {
            return table_names.size
        }

        inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var bookHist_table : TextView
            var bookHist_item : TextView

            init {
                bookHist_table = itemView.findViewById(R.id.bookHist_table)
                bookHist_item = itemView.findViewById(R.id.bookHist_item)
                bookHist_item
            }
        }
    }

    private fun getStoreInfo(sikdangId : String, catory : String) {
        store_map.clear()
        FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(catory)
                .child(sikdangId)
                .child("info")
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val store_info = snapshot.getValue(StoreInfo::class.java)
                        if(store_info == null) return
                        store_map.put(sikdangId, store_info)
                        this@BookHistoryAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
    }
}