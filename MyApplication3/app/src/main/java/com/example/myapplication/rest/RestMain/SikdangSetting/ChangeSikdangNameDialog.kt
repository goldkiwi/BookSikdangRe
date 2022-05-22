package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


//SikdangSetingDialog에서 사용
//식당 이름 변경하는 다이얼로그
class ChangeSikdangNameDialog(context: Context, val sikdangNum:String, var sikdangmainRes: SikdangMain_res): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_changesikdangname_dialog)


        var csndSikdangName:TextView = findViewById(R.id.csndSikdangName)
        csndSikdangName.setText(getSikdangName())

        var afterSikdangNameRT:EditText = findViewById(R.id.afterSikdangNameRT)

        var csndChangeBtn:Button = findViewById(R.id.csndChangeBtn)
        csndChangeBtn.setOnClickListener {
            changeSikdangName(sikdangNum, afterSikdangNameRT.text.toString())

        }

        var csndcancelBtn: Button = findViewById(R.id.csndcancelBtn)
        csndcancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    private fun getSikdangName():String{
        return sikdangmainRes.sikdangName
    }

    private fun changeSikdangName(sikdangNum_:String, newSikdangName:String){
        //데이터베이스 접속하여 식당 이름 변경
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("info").child("store_name")
        ref.setValue(newSikdangName)

        this.dismiss()
    }
}