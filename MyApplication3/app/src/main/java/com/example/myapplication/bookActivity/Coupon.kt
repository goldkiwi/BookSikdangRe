package com.example.myapplication.bookActivity

import android.view.inspector.InspectionCompanion
import com.google.firebase.database.DataSnapshot
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Coupon(
    val user_id : String,
    val coupon_id: String,
    val min_price: Int,
    val discount: String,
    val coupon_exp: String,
    val type : Int,
    val expire: String,
    var used : Boolean
)
{
    fun toggle() {
        used = used.not()
    }

    companion object {
        val DUPL = 1
        val SINGLE = 2
        var CURRENT_TIME : String? = null
        fun IsValidCoupon(coupon: _coupon) : Boolean{
            Init_TIME()
            if(CURRENT_TIME!!.compareTo(coupon.expire!!) > 0) return false
            return true
        }

        fun Init_TIME() {
            if(CURRENT_TIME == null) {
                CURRENT_TIME = LocalDate.now().toString()
            }
        }
    }
}

data class _coupon (
    val user_id : String? = null,
    val coupon_id : String? = null,
    val discount : String? = null,
    val min_price : Int? = null,
    val coupon_exp : String? = null,
    val type : Int? = null,
    val expire : String? = null
)