package com.example.myapplication.mainPage

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class CatorySelectDialog (
        context: Context,
        val sikdangMainListner : Sikdang_main
        ) : Dialog(context)
{
        lateinit var catory_set : RecyclerView
        lateinit var complete : Button
        var result : ArrayList<CatorySet> = arrayListOf()

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.catory_dialog)

                catory_set = findViewById(R.id.catory_set)
                complete = findViewById(R.id.complete_btn)
                complete.setOnClickListener { complete() }

                catory_set.adapter = CatorySetAdapter(context, this)
                catory_set.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        fun add_theme(name : String, theme : String) {
                for(exist in result) {
                        if(exist.catory_name == name) {
                                if(exist.catory_list.contains(theme) == false) exist.catory_list.add(theme)
                                return
                        }
                }
                result.add(CatorySet(name, arrayListOf(theme)))
        }

        fun remove_theme(name : String, theme : String) {
                for (exist in result) {
                        if(exist.catory_name == name) {
                                if(exist.catory_list.contains(theme) == false) return
                                exist.catory_list.remove(theme)
                        }
                }
        }

        private fun complete() {
                for (data in result) {
                        for (elem in data.catory_list) {
                                Log.d("CatorySetAdapter", "${data.catory_name}, ${elem}")
                        }
                }
        }
}