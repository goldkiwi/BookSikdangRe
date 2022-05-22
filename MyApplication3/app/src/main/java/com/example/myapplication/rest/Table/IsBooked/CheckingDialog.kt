package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

class CheckingDialog(context: Context, val string: String, val tableNum:Int, val notBookedTableRVAdapter: NotBookedTableRVAdapter): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)

        var checkingText:TextView = findViewById(R.id.checkingText)
        checkingText.setText(string+"\n 예약을 추가하겠습니까?")

        var checkButton: Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            notBookedTableRVAdapter.addBook(tableNum)
            this.dismiss()

        }

        var notCheckButton: Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener {
            this.dismiss()
        }
    }


}