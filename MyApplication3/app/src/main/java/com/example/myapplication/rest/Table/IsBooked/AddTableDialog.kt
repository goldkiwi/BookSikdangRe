package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

class AddTableDialog(context: Context,val userBookData: UserBookData): Dialog(context) {

    lateinit var notBookedTableRV:RecyclerView
    lateinit var notBookedTableRVAdapter:NotBookedTableRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_addtable_dialog)


        notBookedTableRV= findViewById(R.id.notBookedTableRV)
        notBookedTableRVAdapter = NotBookedTableRVAdapter(context, userBookData, this)
        notBookedTableRV.adapter = notBookedTableRVAdapter

        var RVLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        notBookedTableRV.layoutManager=RVLayoutManager
        notBookedTableRV.setHasFixedSize(true)

        var addTable_addBtn:Button = findViewById(R.id.addTable_completeBtn)
        addTable_addBtn.setOnClickListener {

        }

        var addTable_completeBtn: Button = findViewById(R.id.addTable_completeBtn)
        addTable_completeBtn.setOnClickListener {
            this.dismiss()
        }



    }



    public fun renewalTable(){
        notBookedTableRVAdapter.notifyDataSetChanged()
    }
}