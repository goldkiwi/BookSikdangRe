package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

class TableCompleteDialog(context: Context, val bookNum:String, val tablestateisbookeddialogRes: TableStateIsBookedDialog_res): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)

        var checkingText = findViewById<TextView>(R.id.checkingText)
        checkingText.setText(bookNum+"\n테이블 정리가 모두 완료되었습니까?")

        var checkButton: Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            tablestateisbookeddialogRes.tableComplete()
            this.dismiss()
        }

        var notCheckButton: Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener {
            this.dismiss()
        }
    }


}