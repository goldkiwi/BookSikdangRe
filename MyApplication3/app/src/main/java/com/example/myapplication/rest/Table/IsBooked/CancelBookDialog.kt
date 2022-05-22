package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

class CancelBookDialog(context: Context, val bookNum: String, val tablestateisbookeddialogRes: TableStateIsBookedDialog_res): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_cancelbook_dialog)

        var cancelInfoTV = findViewById<TextView>(R.id.cancelInfoTV)
        cancelInfoTV.setText(bookNum+" 예약이 취소되었습니다.\n환불을 진행하시겠습니까?")

        var refundbtn: Button = findViewById(R.id.refundbtn)
        refundbtn.setOnClickListener {
            refund()
            this.dismiss()
        }

        var closebtn:Button = findViewById(R.id.closebtn)
        closebtn.setOnClickListener { this.dismiss() }


    }
    //예약번호의 예약 환불 진행
    private fun refund(){



        //마지막줄에 이전 다이얼로그 닫는 아랫줄
        tablestateisbookeddialogRes.closeThis()
    }
}