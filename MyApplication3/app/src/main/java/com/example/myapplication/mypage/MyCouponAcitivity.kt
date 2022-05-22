package com.example.myapplication.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myapplication.R

class MyCouponAcitivity : AppCompatActivity() {
    lateinit var fragment_name : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_acitivity)

        fragment_name = findViewById(R.id.fragment_name)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ViewMyCouponFragment())
            .commit()
    }
}