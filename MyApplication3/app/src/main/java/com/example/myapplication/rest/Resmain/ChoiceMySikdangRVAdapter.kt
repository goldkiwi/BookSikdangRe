package com.example.myapplication.rest.Resmain

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ChoiceMySikdangRVAdapter(var context: Context, val choicesikdangpageRes: ChoiceSikdangPage_res):RecyclerView.Adapter<ChoiceMySikdangRVAdapter.Holder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.res_sikdang_line, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        Log.d("확인 ChoiceMySikdangRVAdapter", choicesikdangpageRes.sikdangInfoList.size.toString())
        return choicesikdangpageRes.sikdangInfoList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }





    //데이터베이스 접속해 식당 정보 불러온다.
    //getItempCount 에 리턴값 2로 되어있는데 저 부분은 식당 갯수로 수정 필요
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int) {
            var count = pos
            var sikdangImage:ImageView = itemView.findViewById(R.id.sikdangImage)
            var sikdangLine_NameTV:TextView = itemView.findViewById(R.id.sikdangLine_NameTV)
            var sikdangLine_addr:TextView = itemView.findViewById(R.id.sikdangLine_addr)
            var sikdangLine_number:TextView = itemView.findViewById(R.id.sikdangLine_number)

            sikdangLine_NameTV.setText(choicesikdangpageRes.sikdangInfoList[count].store_name)
            sikdangLine_number.setText(choicesikdangpageRes.sikdangInfoList[count].phone_number)

            itemView.setOnClickListener {
                Log.d("확인 ChoiceMySikdangRVAdapter", "ㅁㅁ")
                val intent = Intent(itemView.context, SikdangMain_res::class.java)
                intent.putExtra("sikdangId", choicesikdangpageRes.sikdangList[count].sikdangId)
                intent.putExtra("sikdangType", choicesikdangpageRes.sikdangInfoList[count].store_type)
                intent.putExtra("sikdangName", choicesikdangpageRes.sikdangInfoList[count].store_name)

                context.startActivity(intent)

            }
        }

    }


}