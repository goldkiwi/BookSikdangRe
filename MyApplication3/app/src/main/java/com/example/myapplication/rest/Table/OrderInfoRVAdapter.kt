package com.example.myapplication.rest.Table

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.sikdangbook_rest.Time.TimeLineAdapter

class OrderInfoRVAdapter(var context: Context): RecyclerView.Adapter<OrderInfoRVAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmenu_eachtable, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class Holder(itemView: View):RecyclerView.ViewHolder(itemView){
        public fun bind(){

        }
    }
}