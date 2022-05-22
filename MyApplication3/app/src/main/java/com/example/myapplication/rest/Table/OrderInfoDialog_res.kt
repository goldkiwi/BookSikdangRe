package com.example.myapplication.rest.Table

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.sikdangbook_rest.Time.TimeLineAdapter

class OrderInfoDialog_res(context: Context): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_orderinfo_dialog)

        var orderRV : RecyclerView = findViewById(R.id.orderRV)
        var RVAdapter = OrderInfoRVAdapter(context)
        orderRV.adapter = RVAdapter


        var LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        orderRV.layoutManager=LM
        orderRV.setHasFixedSize(true)

    }
}