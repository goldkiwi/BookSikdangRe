package com.example.myapplication.rest.Resmain

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

class OrderPermitCheckingDialog(context: Context, var  orderMessageDialog: OrderMessageDialog): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)

        var checkingText:TextView=findViewById(R.id.checkingText)
        checkingText.setText("예약을 수락하시겠습니까?")

        var checkButton: Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            orderMessageDialog.orderPermit()
            this.dismiss()
        }

        var notCheckButton:Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener { this.dismiss() }
    }
}