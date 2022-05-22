package com.example.myapplication.bookActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.mainPage.MainActivity
import com.example.myapplication.start.StartActivity

class PayCompleteSuccess : AppCompatActivity() {
    lateinit var request : TextView
    lateinit var requst_string : String

    lateinit var rv_content : RecyclerView
    lateinit var tableDirectoryAdapter: TableDirectoryAdapter
    lateinit var stocks : HashMap<String, ChoiceItem>

    lateinit var btn_back : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_complete_success)

        request = findViewById(R.id.request)
        rv_content = findViewById(R.id.rv_content)
        btn_back = findViewById(R.id.btn_back)
        btn_back.setOnClickListener {
            complete()
        }

        stocks = intent.getSerializableExtra("stocks") as HashMap<String, ChoiceItem>
        requst_string = intent.getStringExtra("request")!!

        tableDirectoryAdapter = TableDirectoryAdapter(this, stocks)
        val linearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_content.adapter = tableDirectoryAdapter
        rv_content.layoutManager = linearLayout
    }

    override fun onBackPressed() {
        complete()
    }

    private fun complete() {
        val _intent = Intent(this, MainActivity::class.java)
        _intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(_intent)
        finish()
    }
}