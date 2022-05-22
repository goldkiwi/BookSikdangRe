package com.example.myapplication.rest.Table.IsBooked

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

//EditTableDialog에서 사용
class TableNotBookedRVAdapter(var context: Context, val floorAL:ArrayList<Int>, val tableNumAL:ArrayList<Int>, val editTableDialog: EditTableDialog): RecyclerView.Adapter<TableNotBookedRVAdapter.Holder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.generalitem_togglebutton, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return tableNumAL.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            var pos_ = pos
            var toggleButton: ToggleButton = itemView.findViewById(R.id.gToggleButton)
            toggleButton.setText(floorAL[pos_].toString()+" 층"+tableNumAL[pos_].toString()+" 번 테이블")
            toggleButton.textOn = floorAL[pos_].toString()+" 층"+tableNumAL[pos_].toString()+" 번 테이블"
            toggleButton.textOff = floorAL[pos_].toString()+" 층"+tableNumAL[pos_].toString()+" 번 테이블"
            toggleButton.setOnClickListener {
                editTableDialog.checkNotBookedTB(pos_)
            }
            Log.d("확인 TableNotBookedRVAdapter 테이블 추가", pos_.toString())
            editTableDialog.notBookedAddTB(toggleButton)

        }
    }
}