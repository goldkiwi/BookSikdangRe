package com.example.myapplication.mainPage

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.recommendation.MsgCat

class What_eat_today(): AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.what_eat_today)
        //msg객체를 넘겨받는다.
        var intent = getIntent()
        //var pos = intent.getExtras()?.getInt("msgCat")
        Log.d("확인 msgCat 생성? call?", "What_eat_today.onCreate")
        var msgCat = intent.getParcelableExtra<MsgCat>("msgcat")
        Log.d("종료지점확인", "What_eat_today 1")
        var orderListText: TextView = findViewById(R.id.orderListText)
        Log.d("종료지점확인", "What_eat_today 2")
        msgCat?.logOrderList()
        var orderText:String="123"
        orderText+=msgCat?.getText()
        Log.d("msgCat.getText 내용확인", orderText)
        orderListText.setText(msgCat?.getText())
        msgCat?.logTagOrderListString()
        msgCat?.logTagOrderList()

        Log.d("종료지점확인", "What_eat_today 3")

    }
}