package com.example.myapplication.rest.Table.IsBooked

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData


//DeleteTableDialog에서 사용
class BeforeDeleteRVAdapter(var context: Context, val userBookData: UserBookData, val deleteTableDialog: DeleteTableDialog): RecyclerView.Adapter<BeforeDeleteRVAdapter.Holder>() {


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



    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            var gButton: ToggleButton = itemView.findViewById(R.id.gToggleButton)
            gButton.setText(userBookData.tableAL[pos].tableFloor.toString()+" 층"+userBookData.tableAL[pos].tableNum.toString()+" 번 테이블")
            gButton.textOff = userBookData.tableAL[pos].tableFloor.toString()+" 층"+userBookData.tableAL[pos].tableNum.toString()+" 번 테이블"
            gButton.textOn = userBookData.tableAL[pos].tableFloor.toString()+" 층"+userBookData.tableAL[pos].tableNum.toString()+" 번 테이블"
            deleteTableDialog.beforeButtonAl.add(gButton)
            deleteTableDialog.floorAL.add(userBookData.tableAL[pos].tableFloor)
            deleteTableDialog.tableNumAL.add(userBookData.tableAL[pos].tableNum)
            gButton.setOnClickListener {
                //여기서 리사이클러뷰 설정
                deleteTableDialog.beforeButtonClick(pos)
            }
        }
    }

}