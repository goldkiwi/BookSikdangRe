package com.example.myapplication.rest.Resmain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.example.myapplication.R


class MainActivity_res : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_main)


        var startButton = findViewById<Button>(R.id.button_start)
        startButton.setOnClickListener {
            val intent = Intent(this, ChoiceSikdangPage_res::class.java)
            startActivity(intent)


        }
    }
}