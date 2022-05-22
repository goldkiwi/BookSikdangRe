package com.example.myapplication.addPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class Add_page: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getExtras 로 배너의 pos 받아서 어느 광고페이지 띄울지 결정
        var intent = getIntent()
        var pos = intent.getExtras()?.getInt("pos")
        when (pos){
            0-> setContentView(R.layout.add_page_0)
            1-> setContentView(R.layout.add_page_1)
            2-> setContentView(R.layout.add_page_2)
            3-> setContentView(R.layout.add_page_3)
            4-> setContentView(R.layout.add_page_4)
            5-> setContentView(R.layout.add_page_5)
            else -> setContentView(R.layout.add_page_0)
        }
        //setContentView(R.layout.add_page_0)


    }
}