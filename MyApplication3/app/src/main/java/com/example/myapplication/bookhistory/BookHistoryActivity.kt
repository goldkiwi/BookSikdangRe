package com.example.myapplication.bookhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookHistoryActivity : AppCompatActivity() {
    lateinit var user_id : String

    lateinit var rv_book_history : RecyclerView
    var bookHistoryList : ArrayList<BookHistory> = arrayListOf()
    lateinit var bookHistoryAdapter : BookHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookhist)

        user_id = intent.getStringExtra("user_id")!!
        init_Ui()
        getFromDB()
    }

    private fun init_Ui() {
        rv_book_history = findViewById(R.id.bookHistRV)
        bookHistoryAdapter = BookHistoryAdapter(this, bookHistoryList)
        val bookHistoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_book_history.adapter = bookHistoryAdapter
        rv_book_history.layoutManager = bookHistoryLayoutManager
    }
    private fun getFromDB() {
        FirebaseDatabase.getInstance().getReference("Reservations")
                .child(user_id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        bookHistoryList.clear()
                        for (Date in snapshot.children) {
                            for (reservation in Date.children) {
                                Log.d("getFromDB", "${reservation.key}")
                                val reservationData = reservation.getValue(ReservationData::class.java)

                                var tableMap = hashMapOf<String, ArrayList<Pair<String, Int>>>()
                                for (table in reservation.child("tables").children) {
                                    val content = table.getValue(String::class.java)
                                    tableMap.put(table.key.toString(), splitContent(content!!))
                                }
                                val data = BookHistory(
                                        Date.key.toString(),
                                        reservationData!!.bookTime!!,
                                        reservationData!!.payTime!!,
                                        reservationData!!.sikdangId!!,
                                        reservationData!!.totalPay!!,
                                        reservationData.store_type!!,
                                        tableMap
                                )

                                Log.d("getFromDB", "${user_id} : ${data}")
                                bookHistoryList.add(data)
                            }
                            bookHistoryList.sortBy { it.date }
                            bookHistoryList.reverse()
                            bookHistoryAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
    }

    fun splitContent(content : String): ArrayList<Pair<String, Int>> {
        var result = arrayListOf<Pair<String, Int>>()
        var tokens = content.split(',').map { it.trim() }
        for (token in tokens) {
            val pair = token.split(":").map { it.trim() }
            result.add(Pair(pair[0], pair[1].toInt()))
        }
        return result
    }
}