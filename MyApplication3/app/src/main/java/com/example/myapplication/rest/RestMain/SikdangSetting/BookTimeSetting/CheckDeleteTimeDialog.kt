package com.example.myapplication.rest.RestMain.SikdangSetting.BookTimeSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

//BookTimeSettingRVAdapter 에서 사용
//시간 버튼 클릭시 띄우는 삭제할것인지 묻는 다이얼로그

class CheckDeleteTimeDialog(context: Context, val deleteTime:String, var bookTimeSettingDialog: BookTimeSettingDialog): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)
        var text: TextView = findViewById(R.id.checkingText)
        text.setText(deleteTime+" 삭제하시겠습니까?")

        var checkButton: Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            bookTimeSettingDialog.deleteTime(deleteTime)
            this.dismiss()
        }

        var notCheckButton: Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener { this.dismiss() }

    }
}