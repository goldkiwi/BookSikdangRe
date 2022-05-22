package com.example.myapplication.bookActivity

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class CouponDialog
    (
        context: Context,
        var coupons : ArrayList<Coupon>,
        var listener : PayActivity
    ) : Dialog(context)
{
    lateinit var recyclerView: RecyclerView
    lateinit var complete : Button
    lateinit var coupon_layout : LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paypage_coupondialog)

        recyclerView = findViewById(R.id.couponListRV)
        complete = findViewById(R.id.couponSelectButton)
        recyclerView.adapter = CouponAdapter(context, coupons, this)
        coupon_layout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = coupon_layout

        complete.setOnClickListener {
            this.dismiss()
        }
    }

    fun itemCheck(coupon : Coupon) {
        if(!is_exceed_min(coupon)) {
            update_coupon()
            Toast.makeText(context, "매장 가격이 부족하여 이 쿠폰을 못씁니다.", Toast.LENGTH_SHORT).show()
            return
        }
        when(coupon.type) {
            Coupon.DUPL->{duplicateOK(coupon)}
            Coupon.SINGLE->{duplicateForbid(coupon)}
        }
    }

    private fun duplicateOK(coupon : Coupon) {
        coupon.used = coupon.used.not()
        update_coupon()
        listener.updatePrice()
    }

    private fun duplicateForbid(coupon: Coupon) {
        if(listener.duplicate == coupon) {
            listener.duplicate!!.toggle()
            listener.duplicate = null
        }
        else if(listener.duplicate == null) {
            listener.duplicate = coupon
            listener.duplicate!!.toggle()
        }
        else if (listener.duplicate != coupon) {
            listener.duplicate!!.toggle()
            listener.duplicate = coupon
            listener.duplicate!!.toggle()
        }
        update_coupon()
        listener.updatePrice()
    }

    private fun update_coupon() {
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun is_exceed_min(coupon: Coupon) : Boolean{
        return listener.price >= coupon.min_price
    }
}