package com.example.myapplication.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import android.content.Intent
import android.os.Build
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.mainPage.MainActivity
import com.example.myapplication.rest.Resmain.ChoiceSikdangPage_res
import com.example.myapplication.rest.Resmain.MainActivity_res
import com.example.myapplication.rest.Resmain.ResLoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    lateinit var manufacturer_login : Button
    lateinit var user_login : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        manufacturer_login = findViewById(R.id.manufacturer_login)
        user_login = findViewById(R.id.user_login)

        user_login.setOnClickListener {
            //startActivity(Intent(this@StartActivity, CustomerLogInActivity::class.java))
            //finish()
            if(Build.VERSION.SDK_INT<26){
                Toast.makeText(this, "안드로이드 버전이 낮습니다.", Toast.LENGTH_SHORT).show()
            }
            else{
                //startActivity(Intent(this@StartActivity, Sikdang_main::class.java))
                startActivity(Intent(this@StartActivity, CustomerLogInActivity::class.java))
                finish()
            }
        }

        manufacturer_login.setOnClickListener {
            //Toast.makeText(this, "미구현", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@StartActivity, ResLoginActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        // 여기서 firebaseUser와
        // firebaseDatabase로 사용자와 판매자 구별해서 UI변경
        // FirebaseDatabase.getInstance().getReference(/* 사용자 */)
        // reference.childExists(firebaseUser) -> Main

        if(firebaseUser != null) {
            //startActivity(Intent(this, SelectLoginActivity::class.java))
            startActivity(Intent(this, MainActivity::class.java))
            //startActivity(Intent(this, ChoiceSikdangPage_res::class.java))
            finish()
        }

        else{
            //startActivity(Intent(this@StartActivity, SelectLoginActivity::class.java))
        }
    }
}