package com.example.myapplication.bookhistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.internal.notify
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookHistoryFragment : Fragment() {
    lateinit var userid : String
    lateinit var bookHistRV : RecyclerView
    lateinit var bookHistoryAdapter: BookHistoryAdapter

    var bookHistoryList : ArrayList<BookHistory> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view  = inflater.inflate(R.layout.bookhist, container, false)
        userid = FirebaseAuth.getInstance().uid!!
        initUi(view)
        getFromDB()

        return view
    }

    private fun initUi(view: View) {
        bookHistRV = view.findViewById(R.id.bookHistRV)
        bookHistoryAdapter = BookHistoryAdapter(requireContext(), bookHistoryList)
        var bookHistoryLayout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bookHistRV.adapter = bookHistoryAdapter
        bookHistRV.layoutManager = bookHistoryLayout

    }

    fun getFromDB() {
        val current = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

        FirebaseDatabase.getInstance().getReference("Reservations")
            .child(userid)
            .orderByKey()
            .startAt(current)
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

                            Log.d("getFromDB", "${userid} : ${data}")
                            bookHistoryList.add(data)
                        }
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