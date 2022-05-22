package com.example.myapplication.rest.ResInfo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

//sellInfoActivity 에서 사용
//리사이클러뷰에 판매 정보 바인드
class SellRVAdapter(var context: Context, var sellInfoActivity: SellInfoActivity): RecyclerView.Adapter<SellRVAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.res_sellinfo_line, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            Log.d("확인 SellRVAdapter", pos.toString())
            var si_nameTV:TextView=itemView.findViewById(R.id.si_nameTV)
            si_nameTV.setText(sellInfoActivity.sellDataAL[pos].conName)
            Log.d("확인 SellRVAdapter", pos.toString()+" 2")

            var si_pnTV:TextView=itemView.findViewById(R.id.si_pnTV)
            si_pnTV.setText(sellInfoActivity.sellDataAL[pos].pn)

            var si_priceTV:TextView=itemView.findViewById(R.id.si_priceTV)
            si_priceTV.setText(sellInfoActivity.sellDataAL[pos].price.toString()+"원")

            Log.d("확인 SellRVAdapter", pos.toString()+" 3")
            var si_TimeTV:TextView=itemView.findViewById(R.id.si_TimeTV)
            si_TimeTV.setText(sellInfoActivity.sellDataAL[pos].sh.toString()+" 시"+sellInfoActivity.sellDataAL[pos].sm.toString()+" 분 ~ "+
                    sellInfoActivity.sellDataAL[pos].eh.toString()+" 시"+sellInfoActivity.sellDataAL[pos].em.toString()+" 분 / 예약시간:"+
                    sellInfoActivity.sellDataAL[pos].y.toString()+"."+sellInfoActivity.sellDataAL[pos].m.toString()+"."+sellInfoActivity.sellDataAL[pos].day.toString()+"."+
                    sellInfoActivity.sellDataAL[pos].h.toString()+"."+sellInfoActivity.sellDataAL[pos].min.toString()+"."+sellInfoActivity.sellDataAL[pos].sec.toString())
            Log.d("확인 SellRVAdapter", pos.toString()+"끝")

            var sellInfoLayout:LinearLayout = itemView.findViewById(R.id.sellInfoLayout)
            sellInfoLayout.setOnClickListener {

            }
        }

    }
}