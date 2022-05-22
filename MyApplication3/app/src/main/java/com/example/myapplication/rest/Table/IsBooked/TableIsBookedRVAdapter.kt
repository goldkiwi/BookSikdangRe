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
class TableIsBookedRVAdapter(var context: Context, val userBookData: UserBookData, var editTableDialog: EditTableDialog): RecyclerView.Adapter<TableIsBookedRVAdapter.Holder>() {

    init{
        setTable()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.generalitem_togglebutton, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return userBookData.tableAL.size
    }

    private fun setTable(){

    }


    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            var pos_=pos
            var toggleButton:ToggleButton = itemView.findViewById(R.id.gToggleButton)
            toggleButton.setText(userBookData.tableAL[pos].tableFloor.toString()+" 층"+userBookData.tableAL[pos_].tableNum.toString()+" 번 테이블")
            toggleButton.textOn = userBookData.tableAL[pos].tableFloor.toString()+" 층"+userBookData.tableAL[pos_].tableNum.toString()+" 번 테이블"
            toggleButton.textOff = userBookData.tableAL[pos].tableFloor.toString()+" 층"+userBookData.tableAL[pos_].tableNum.toString()+" 번 테이블"
            toggleButton.setOnClickListener {
                editTableDialog.checkIsBookedTB(pos_)
            }
            Log.d("확인 TableIsBookedRVAdapter 테이블 추가", pos_.toString())
            editTableDialog.isBookedAddTB(toggleButton)

        }
    }
}