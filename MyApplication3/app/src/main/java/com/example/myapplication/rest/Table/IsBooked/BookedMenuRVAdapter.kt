package com.example.myapplication.rest.Table.IsBooked

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

//bookInfoDialog에서 사용
class BookedMenuRVAdapter(var context: Context, val userBookData: UserBookData): RecyclerView.Adapter<BookedMenuRVAdapter.Holder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmenu_eachtable, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        //예약한 테이블 수
        //Log.d("확인 bookedMenuRVAdapter 바인드 수 ", userBookData.tableAL.size.toString())
        return userBookData.tableAL.size

    }


    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            var tableNumTV = itemView.findViewById<TextView>(R.id.tableNumTV)
            tableNumTV.setText(userBookData.tableAL[pos].tableFloor.toString()+" 층"+
                    userBookData.tableAL[pos].tableNum.toString()+" 번 테이블")

            //리사이클러뷰에 메뉴 넣음
            var menuRV: RecyclerView = itemView.findViewById(R.id.tableMenuRV)
            var menuRVAdapter = innerRVAdapter(context, pos)
            menuRV.adapter = menuRVAdapter

            var RVLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            menuRV.layoutManager=RVLayoutManager
            menuRV.setHasFixedSize(true)


        }

    }
    //각 테이블마다 리사이클러뷰에 메뉴 넣음
    inner class innerRVAdapter(var innerContext: Context, val tablePos:Int): RecyclerView.Adapter<innerRVAdapter.InnerHolder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
            val view = LayoutInflater.from(innerContext).inflate(R.layout.bookmenu_selectedline, parent, false)
            return InnerHolder(view)
        }

        override fun onBindViewHolder(holder: InnerHolder, position: Int) {
            holder.innerBind(position)
        }

        override fun getItemCount(): Int {
            return userBookData.tableAL[tablePos].menuAL.size
        }

        inner class InnerHolder(val innerView:View): RecyclerView.ViewHolder(innerView){
            public fun innerBind(pos:Int){
                Log.d("확인 bookedMenuRVAdapter 테이블 정보", tablePos.toString()+" "+pos.toString())
                var menuNameLineTV = innerView.findViewById<TextView>(R.id.menuNameLineTV)
                menuNameLineTV.setText(userBookData.tableAL[tablePos].menuAL[pos].menuName)

                var menuNumLineTV = innerView.findViewById<TextView>(R.id.menuNumLineTV)
                menuNumLineTV.setText(userBookData.tableAL[tablePos].menuAL[pos].num.toString())
            }

        }
    }
}