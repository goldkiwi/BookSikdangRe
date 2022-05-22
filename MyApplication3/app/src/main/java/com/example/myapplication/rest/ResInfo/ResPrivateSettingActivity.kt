package com.example.myapplication.rest.ResInfo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.rest.RestMain.SikdangSetting.BookTimeSetting.BookTimeSettingDialog

class ResPrivateSettingActivity: AppCompatActivity() {

    lateinit var ps_signInfoTV:TextView
    lateinit var ps_idInfoTV:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_privatesetting_activity)

        ps_signInfoTV=findViewById(R.id.ps_signInfoTV)
        ps_idInfoTV=findViewById(R.id.ps_idInfoTV)

        var ps_editPWBtn: Button = findViewById(R.id.ps_editPWBtn)
        ps_editPWBtn.setOnClickListener {
            showEditPWDialog()
        }

        setInfoTV()

    }


    private fun setInfoTV(){
        ps_signInfoTV.setText("가입 정보")
        ps_idInfoTV.setText("계정 정보")
    }

    private fun showEditPWDialog(){
        var customDialog = EditPWDialog(this)
        customDialog!!.show()
    }

}