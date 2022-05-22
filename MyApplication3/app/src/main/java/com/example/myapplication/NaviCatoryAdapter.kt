package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.mainPage.CatList
import com.example.myapplication.mainPage.CatorySet

class NaviCatoryAdapter (
    val context: Context,
    var callback : MapActivity
) : RecyclerView.Adapter<NaviCatoryAdapter.Holder>()
{
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = catories[position]

        holder.catory_name.text = data
        holder.catory_name.setOnClickListener {
            callback.catory = data
            if(callback.mNaverMap == null) return@setOnClickListener
            Log.d("Navi adapter", callback.catory)
            callback.fetchStore(callback.lastLocation, callback.range)
        }
    }

    override fun getItemCount(): Int {
        return catories.size
    }

    var catories : ArrayList<String>

    init {
        catories = arrayListOf("ALL", "돼지고기", "닭고기", "한식",
            "중식", "일식*회", "아시안*양식", "면",
            "분식", "포차", "디저트", "프랜차이즈",
            "뭐가", "문제야", "한식")

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.navi_catory_item, parent, false)

        return Holder(view)
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var catory_name : TextView
        init {
            catory_name = itemView.findViewById(R.id.catory_name)
        }
    }
}