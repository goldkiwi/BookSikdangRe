package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

//TableStateIsBookedDialog_res 에서 사용

class bookInfoDialog(context: Context, val userBookData: UserBookData): Dialog(context) {
    var totalPrice = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_bookinfo_dialog)
        setTotalPrice()

        var bookTimeTV = findViewById<TextView>(R.id.bookTimeTV)
        bookTimeTV.setText(userBookData.bookStartTime+" ~ "+userBookData.bookEndTime)

        var bookInfo_bookNumberTV = findViewById<TextView>(R.id.bookInfo_bookNumberTV)
        bookInfo_bookNumberTV.setText("예약번호 : "+userBookData.bookNumber)

        var userNameTv = findViewById<TextView>(R.id.userNameTV)
        userNameTv.setText(userBookData.userName)

        var userPhone = findViewById<TextView>(R.id.userPhone)
        userPhone.setText(userBookData.phonNum)

        var bookInfoPriceTV:TextView = findViewById(R.id.bookInfoPriceTV)
        bookInfoPriceTV.setText("총 "+totalPrice.toString()+" 원")


        var xTV_bookInfoDialog = findViewById<TextView>(R.id.XTV_bookInfoDialog)
        xTV_bookInfoDialog.setOnClickListener {
            this.dismiss()
        }

        var menuRV: RecyclerView = findViewById(R.id.bookedMenuRV)
        var menuRVAdapter = BookedMenuRVAdapter(context, userBookData)
        menuRV.adapter = menuRVAdapter

        var RVLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        menuRV.layoutManager=RVLayoutManager
        menuRV.setHasFixedSize(true)
    }

    private fun setTotalPrice(){
        totalPrice = 0
        for (i in 0..userBookData.tableAL.size-1){
            for (j in 0.. userBookData.tableAL[i].menuAL.size-1){
                totalPrice += userBookData.tableAL[i].menuAL[j].price
            }
        }
    }
}