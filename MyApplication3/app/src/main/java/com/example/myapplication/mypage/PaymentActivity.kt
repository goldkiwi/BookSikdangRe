package com.example.myapplication.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bookhistory.BookHistory
import com.example.myapplication.bookhistory.ReservationData
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class PaymentActivity : AppCompatActivity() {
    lateinit var rv_payment : RecyclerView
    lateinit var paymentAdapter : PaymentAdapter
    var payment_list : ArrayList<BookHistory> = arrayListOf()
    var store_list : ArrayList<StoreInfo?> = arrayListOf()

    lateinit var user_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_completed)
        user_id = FirebaseAuth.getInstance().uid!!
        init_Ui()
        getFromHistory()
    }

    private fun init_Ui() {
        rv_payment = findViewById(R.id.rv_payment)
        val linearLayoutManager
        = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        paymentAdapter = PaymentAdapter(this, payment_list, store_list)
        rv_payment.adapter = paymentAdapter
        rv_payment.layoutManager = linearLayoutManager
    }

    private fun getFromHistory() {
        FirebaseDatabase.getInstance().getReference("Reservations")
            .child(user_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    payment_list.clear()
                    store_list.clear()
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

                            Log.d("getFromHistory", "${user_id} : ${data}")
                            payment_list.add(data)
                            store_list.add(null)
                        }
                    }
                    payment_list.reverse()
                    paymentAdapter.notifyDataSetChanged()
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