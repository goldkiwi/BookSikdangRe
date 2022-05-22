package com.example.myapplication.start

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.MainActivity_res
import com.example.myapplication.rest.Resmain.ResLoginActivity
import com.google.firebase.auth.FirebaseUser

class SelectLoginActivity: AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    lateinit var manufacturer_login: Button
    lateinit var user_login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //setContentView(R.layout.res_sikdangmain)

        manufacturer_login = findViewById(R.id.manufacturer_login)
        user_login = findViewById(R.id.user_login)
        Log.d("확인 셀렉트 로그인", "1")

        user_login.setOnClickListener {
            //startActivity(Intent(this@StartActivity, CustomerLogInActivity::class.java))
            //finish()
            Log.d("확인 셀렉트 로그인", "2")
            if(Build.VERSION.SDK_INT<26){
                Toast.makeText(this, "안드로이드 버전이 낮습니다.", Toast.LENGTH_SHORT).show()
            }
            else{
                //startActivity(Intent(this@StartActivity, Sikdang_main::class.java))
                startActivity(Intent(this, CustomerLogInActivity::class.java))
                finish()
            }
        }
        Log.d("확인 셀렉트 로그인", "3")

        manufacturer_login.setOnClickListener {
            Log.d("확인 셀렉트 로그인", "4")
            //Toast.makeText(this, "미구현", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ResLoginActivity::class.java))
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("확인 셀렉트 로그인", "5")

    }


}