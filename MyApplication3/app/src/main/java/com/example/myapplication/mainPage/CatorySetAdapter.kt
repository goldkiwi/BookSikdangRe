package com.example.myapplication.mainPage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.mainPage.CatorySet.Companion.CatoryDataSet

class CatorySetAdapter(
        val context: Context,
        var dialogListner : CatorySelectDialog
) : RecyclerView.Adapter<CatorySetAdapter.Holder>() {
    lateinit var max_options : IntArray
    val MAX = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.catory_item, parent, false)
        max_options = IntArray(CatoryDataSet.size, {MAX})
        for (data in max_options) {
            Log.d("CatorySetAdapter", "data : ${max_options.size}")
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var data = CatoryDataSet[position]
        holder.catory_name.text = data.catory_name
        holder.catory_theme.adapter = CatoryThemeAdapter(context, data.catory_name, data.catory_list)
        holder.catory_theme.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    }

    override fun getItemCount(): Int {
        return CatoryDataSet.size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var catory_name : TextView
        var catory_theme : RecyclerView

        init {
            catory_name = itemView.findViewById(R.id.catory_name)
            catory_theme = itemView.findViewById(R.id.catory_theme)
        }
    }
    inner class CatoryThemeAdapter(
            val context: Context,
            val catory_name : String,
            val catory_theme_list : ArrayList<String>

    ): RecyclerView.Adapter<CatoryThemeAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(context).inflate(R.layout.catory_theme_item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val data = catory_theme_list[position]
            holder.catory_theme.text = data
            holder.catory_theme.textOn = data
            holder.catory_theme.textOff = data
            holder.catory_theme.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    if(max_options[getIndex(catory_name)] - 1 < 0 ) {
                        Toast.makeText(context, "테마는 최대 3개 까지입니다.", Toast.LENGTH_SHORT).show()
                        holder.catory_theme.isChecked = false
                        return@setOnCheckedChangeListener
                    }
                    dialogListner.add_theme(catory_name, data)
                    --max_options[getIndex(catory_name)]
                } else {
                    if(max_options[getIndex(catory_name)] + 1 > MAX) return@setOnCheckedChangeListener
                    ++max_options[getIndex(catory_name)]
                    dialogListner.remove_theme(catory_name, data)
                }
            }
        }

        override fun getItemCount(): Int {
            return catory_theme_list.size
        }

        inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
            var catory_theme : ToggleButton

            init {
                catory_theme = itemView.findViewById(R.id.catory_theme)
            }
        }
    }

    private fun getIndex(name : String) : Int {
        for(i in 0 until CatoryDataSet.size) {
            if(CatoryDataSet[i].catory_name == name) return i
        }
        return -1
    }

}