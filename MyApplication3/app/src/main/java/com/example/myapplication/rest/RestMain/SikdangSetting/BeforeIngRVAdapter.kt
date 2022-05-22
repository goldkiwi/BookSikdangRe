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
//아직 변경되지 않은 메뉴의 재료 리사이클러뷰

class BeforeIngRVAdapter(val context: Context, val menuData: MenuData): RecyclerView.Adapter<BeforeIngRVAdapter.Holder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.generalitem_textview, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        //Log.d("확인 BeforeIngRVAdapter", menuData.ingredients.size.toString())
        return menuData.ingredients.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        public fun bind(pos:Int){
            //Log.d("확인 BeforeIngRVAdapter","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
            var gItemTV:TextView=itemView.findViewById(R.id.gItemTV)
            var tempText=""
            tempText+=menuData.ingredients[pos].ing
            tempText+= " : "
            tempText+= menuData.ingredients[pos].country
            gItemTV.setText(tempText)

        }

    }


}