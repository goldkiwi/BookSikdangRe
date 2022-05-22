package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//EditSikdangDialog 에서 사용
//식당 설명 수정하는 다이얼로그
class EditSikdangExpDialog(context: Context, val sikdangNum:String, var sikdangmainRes: SikdangMain_res): Dialog(context) {

    lateinit var sikdangExpET:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_editsikdangexp_dialog)
        sikdangExpET=findViewById(R.id.sikdangExpET)

        setSikdangExp()


        var eseCloseBtn: Button =findViewById(R.id.eseCloseBtn)
        eseCloseBtn.setOnClickListener {
            this.dismiss()
        }

        var eseChangeBtn:Button = findViewById(R.id.eseChangeBtn)
        eseChangeBtn.setOnClickListener {
            changeSikdangExp(sikdangExpET.text.toString())
        }
    }


    //데이터베이스 접속하여 식당 설명 불러온다.
    private fun setSikdangExp(){
        sikdangExpET.setText("")

    }


    private fun changeSikdangExp(newSikdangExp:String){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("info").child("store_exp")
        ref.setValue(newSikdangExp)
        //데이터베이스로 식당설명 보낸다.
        this.dismiss()
    }
}