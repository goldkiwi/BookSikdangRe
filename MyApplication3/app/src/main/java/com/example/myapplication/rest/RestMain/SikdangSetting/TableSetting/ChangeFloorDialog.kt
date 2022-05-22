package com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R

//TableSettingDialog에서 사용
//층수 변경 버튼 클릭시 뜨는 다이얼로그
//에딧텍스트에서 숫자 입력하고 변경시킴
class ChangeFloorDialog(context: Context, val sikdangNum: String, val floor: Int, var tableSettingDialog: TableSettingDialog): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_changefloor_dialog)

        var cf_nowFloorTV =findViewById<TextView>(R.id.cf_nowFloorTV)
        cf_nowFloorTV.setText("현재층 : "+floor.toString())


        var cf_afterFloorET = findViewById<EditText>(R.id.cf_afterFloorET)
        cf_afterFloorET.setText(floor.toString())

        var cf_changeBtn: Button = findViewById(R.id.cf_changeBtn)
        cf_changeBtn.setOnClickListener {
            var tempCheck = checkFloor(cf_afterFloorET.text.toString().toInt())
            if (tempCheck){
                setNewFloor(cf_afterFloorET.text.toString().toInt())
                this.dismiss()
            }
            else{
                val myToast = Toast.makeText(context, "이미 있는 층입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        var cf_cancelBtn:Button = findViewById(R.id.cf_cancelBtn)
        cf_cancelBtn.setOnClickListener { this.dismiss() }


    }

    //데이터베이스 접속해서 newFloor 가 이미 등록되어있는 층인지 검색
    //만약 있으면 false 리턴하고 아니면 true 리턴
    private fun checkFloor(newFloor:Int):Boolean{
        if(false){//데이터베이스에서 체크
            return false
        }

        return true
    }

    //데이터베이스 접속해서 지금 층을 newFloor로 바꾼다
    private fun setNewFloor(newFloor:Int){
        //데이터베이스에 접속해서 바꾼 뒤 아래 코드로 이전 다이얼로그에서 정보 다시 불러온다.
        tableSettingDialog.setNewFloor(newFloor)
    }

}