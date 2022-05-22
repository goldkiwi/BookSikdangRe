package com.example.myapplication.rest.RestMain.SikdangSetting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bookActivity.MenuData

//MenuEditDialog 에서 사용
//변경 후의 재료와 원산지 표기

class AfterIngRVAdapter(val context: Context, var menuEditDialog: MenuEditDialog): RecyclerView.Adapter<AfterIngRVAdapter.Holder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.generalitem_textview, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuEditDialog.afterIngAL.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        public fun bind(pos: Int) {
            //Log.d("확인 BeforeIngRVAdapter","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
            var gItemTV:TextView=itemView.findViewById(R.id.gItemTV)
            var tempText=""
            tempText+=menuEditDialog.afterIngAL[pos].ing
            tempText+= " : "
            tempText+= menuEditDialog.afterIngAL[pos].country
            gItemTV.setText(tempText)
        }
    }
}