package com.example.myapplication.rest.RestMain.SikdangSetting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

//AddMenuDialog 에서 사용
//메뉴의 재료 텍스트뷰 한줄에 재료와 원산지 붙여서 바인드

class AddMenuIngRVAdapter(var context: Context, var addMenuDialog: AddMenuDialog): RecyclerView.Adapter<AddMenuIngRVAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.generalitem_textview, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        //Log.d("확인 AddMenuIngRVAdapter","어레이리스트 사이즈"+addMenuDialog.addIngAL.size.toString())
        return addMenuDialog.addIngAL.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            var gItemTV: TextView =itemView.findViewById(R.id.gItemTV)
            var tempText=""
            tempText+=addMenuDialog.addIngAL[pos].ing
            tempText+= " : "
            tempText+= addMenuDialog.addIngAL[pos].country
            gItemTV.setText(tempText)
        }

    }
}