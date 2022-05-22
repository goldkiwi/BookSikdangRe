package com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.IsBooked.DeleteTableDialog

//TableSettingDialog에서 사용
//층 삭제하는 다이얼로그
class CheckingDeleteFloorDialog(context: Context, val nowFloor:Int, var tableSettingDialog: TableSettingDialog): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)
        var text: TextView = findViewById(R.id.checkingText)
        text.setText(nowFloor.toString()+" 층을 삭제하시겠습니까?")

        var checkButton: Button = findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            tableSettingDialog.deleteThisFloor()
            this.dismiss()
        }

        var notCheckButton: Button = findViewById(R.id.notCheckButton)
        notCheckButton.setOnClickListener { this.dismiss() }

    }


}