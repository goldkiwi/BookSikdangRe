package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

class EditCheckDialog(context: Context, val notFloor:Int, val notNum:Int, val isFloor:Int, val isNum:Int, val editTableDialog: EditTableDialog): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)

        var checkingText = findViewById<TextView>(R.id.checkingText)
        checkingText.setText("변경전 : "+isFloor.toString()+"층" + isNum.toString()+" 번 테이블"+
                "\n변경 후 : " + notFloor.toString()+"층" + notNum.toString()+" 번 테이블"+"\n변경하시겠습니까?")

        var checkButton: Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            editTableDialog.editTable(isNum, notNum)
            this.dismiss()
        }

        var notCheckButton:Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener {
            this.dismiss()
        }

    }




}