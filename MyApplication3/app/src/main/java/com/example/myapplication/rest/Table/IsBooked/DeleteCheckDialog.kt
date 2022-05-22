package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

class DeleteCheckDialog(context: Context, val deleteFloor:Int, val deleteNum:Int, val migrateFloor:Int, val migrateNum:Int, val deleteTableDialog: DeleteTableDialog): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)
        var text:TextView = findViewById(R.id.checkingText)
        text.setText(deleteFloor.toString()+"층 "+deleteNum.toString()+" 번 테이블을 삭제하고\n"+
                migrateFloor.toString()+" 층"+migrateNum+" 번 테이블로 메뉴를 옮기시겠습니까?")

        var checkButton:Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            deleteTableDialog.deleteTable()
            this.dismiss()
        }

        var notCheckButton:Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener { this.dismiss() }

    }


}