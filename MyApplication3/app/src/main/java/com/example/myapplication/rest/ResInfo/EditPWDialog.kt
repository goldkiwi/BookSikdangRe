package com.example.myapplication.rest.ResInfo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.R

class EditPWDialog(context: Context): Dialog(context) {

    lateinit var nowPWET:EditText
    lateinit var newPWET:EditText
    lateinit var rePWET:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_editpw_dialog)

        nowPWET=findViewById(R.id.nowPWET)
        newPWET=findViewById(R.id.newPWET)
        rePWET=findViewById(R.id.rePWET)

        var ep_editPWBtn: Button = findViewById(R.id.ep_editPWBtn)
        ep_editPWBtn.setOnClickListener {
            if (checkPW()){
                this.dismiss()
            }
        }

        var ep_cancelBtn:Button = findViewById(R.id.ep_cancelBtn)
        ep_cancelBtn.setOnClickListener { this.dismiss() }

    }

    //현재 비밀번호와 맞는지 데이터베이스와 대조 맞으면 비밀번호 형식에 맞는지 체크하고 맞으면 데이터베이스에서 비밀번호 변경
    private fun checkPW() :Boolean{
        //아래것들 사용
        //순서대로 현재 비밀번호 새 비밀번호 새 비밀번호 재입력
        //nowPWET.text.toString()
        //newPWET.text.toString()
        //rePWET.text.toString()


        return true
    }

}