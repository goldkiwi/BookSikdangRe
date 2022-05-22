package com.example.myapplication.rest.Resmain

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class OrderMessageTableRVAdapter(var context: Context, var orderNum:Int, var sikdangmainRes: SikdangMain_res): RecyclerView.Adapter<OrderMessageTableRVAdapter.Holder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmenu_eachtable, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return sikdangmainRes.msgBookInfoDataAL[orderNum].size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        public fun bind(pos:Int){
            var count = pos
            var tableNumTV:TextView = itemView.findViewById(R.id.tableNumTV)
            tableNumTV.setText(sikdangmainRes.msgBookInfoDataAL[orderNum][pos].floor + " " + sikdangmainRes.msgBookInfoDataAL[orderNum][pos].table)

            var tableMenuRV:RecyclerView = itemView.findViewById(R.id.tableMenuRV)
            var rvAdapter = InnerRVAdaper(context, count)
            tableMenuRV.adapter = rvAdapter

            var layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            tableMenuRV.layoutManager=layoutManager
            tableMenuRV.setHasFixedSize(true)

        }

    }

    inner class InnerRVAdaper(var context: Context, var tableNum:Int): RecyclerView.Adapter<InnerRVAdaper.InnerHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.bookmenu_selectedline, parent, false)
            return InnerHolder(view)
        }

        override fun onBindViewHolder(holder: InnerHolder, position: Int) {
            holder.innerBind(position)
        }

        override fun getItemCount(): Int {
            return 1
        }

        inner class InnerHolder(val innerView: View): RecyclerView.ViewHolder(innerView){
            public fun innerBind(pos:Int){
                var count = pos
                var menuNameLineTV = innerView.findViewById<TextView>(R.id.menuNameLineTV)
                menuNameLineTV.setText(sikdangmainRes.msgBookInfoDataAL[orderNum][pos].menu)

                var menuNumLineTV = innerView.findViewById<TextView>(R.id.menuNumLineTV)
                menuNumLineTV.setText(sikdangmainRes.msgBookInfoDataAL[orderNum][pos].cnt.toString())
            }
        }
    }


}