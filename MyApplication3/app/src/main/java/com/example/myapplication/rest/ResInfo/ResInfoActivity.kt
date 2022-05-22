package com.example.myapplication.rest.ResInfo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R


//SikdangMain_res 에서 사용
//회원정보 설정
class ResInfoActivity: AppCompatActivity() {
    var sikdangName=""
    var ownerName=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_infoactivity)

        setName()



        var ip_resNameTV:TextView=findViewById(R.id.ip_resNameTV)
        ip_resNameTV.setText(sikdangName)

        var ip_nameTV:TextView = findViewById(R.id.ip_nameTV)
        ip_nameTV.setText(ownerName)



        var ip_settingTV:TextView=findViewById(R.id.ip_settingTV)
        ip_settingTV.setOnClickListener {
            val intent = Intent(this, ResPrivateSettingActivity::class.java)
            startActivity(intent)
        }

        var ip_sellInfoTV:TextView=findViewById(R.id.ip_sellInfoTV)
        ip_sellInfoTV.setOnClickListener {
            val intent = Intent(this, SellInfoActivity::class.java)
            startActivity(intent)
        }

        var ip_noticeTV:TextView = findViewById(R.id.ip_noticeTV)
        ip_noticeTV.setOnClickListener {

        }

        var ip_appSettingTV:TextView=findViewById(R.id.ip_appSettingTV)
        ip_appSettingTV.setOnClickListener {

        }


    }

    //데이터베이스 접속해서 가져옴
    private fun setName(){
        sikdangName="식당이름"
        ownerName="홍길동"
    }
}