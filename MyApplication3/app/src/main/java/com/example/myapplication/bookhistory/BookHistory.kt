package com.example.myapplication.bookhistory

import com.google.firebase.database.IgnoreExtraProperties
import java.io.StringReader

@IgnoreExtraProperties
data class ReservationData(
    val bookTime : String? = null,
    val payTime : String? = null,
    val totalPay : Int? = null,
    val sikdangId : String? = null,
    val store_type : String? = null
)

data class BookHistory(
    val date : String,
    val book_time : String,
    val pay_time : String,
    val sikdangId : String,
    val total_pay : Int,
    val store_type: String,
    val hashMap: HashMap<String, ArrayList<Pair<String, Int>>>
)

