package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

//예약을 취소하는 다이얼로그
//예약번호 받아와서 예약 취소할거냐고 묻고 확인 누르면 환불 화면으로 넘어감

class CheckCancelBookDialog(context: Context, val bookNum: String, val tablestateisbookeddialogRes: TableStateIsBookedDialog_res): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)

        var text: TextView = findViewById(R.id.checkingText)
        text.setText("예약번호 : "+bookNum.toString()+"\n예약을 취소하시겠습니까?")

        var checkButton: Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            tablestateisbookeddialogRes.cancelBook()
            this.dismiss()
        }

        var notCheckButton: Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener { this.dismiss() }
    }
}