package com.example.myapplication.bookActivity

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PersonNumRVAdapter(
        val context: Context,
        val selected_table : Table,
        var listner: BookPersonDialog.Listener
) : RecyclerView.Adapter<PersonNumRVAdapter.Holder>()
{
    val capacity_list : ArrayList<Int>

    init {
        capacity_list = arrayListOf()
        for (i in 0 until selected_table.capacity) {
            capacity_list.add(i + 1)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.general_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var item = capacity_list[position]

        holder.general_item.text = item.toString()
        holder.general_item.setOnClickListener {
            listner.holder_notify(position)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for (payload in payloads) {
                if(payload is String) {
                    if(payload.equals("select")) {
                        holder.general_item.setBackgroundColor(Color.parseColor("#55CC55"))
                    } else if(payload.equals("invalid"))
                        holder.general_item.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return capacity_list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var general_item : Button

        init {
            general_item = itemView.findViewById(R.id.general_item)
        }
    }
}