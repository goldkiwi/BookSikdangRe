package com.example.myapplication.rest.Table.IsBooked

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

//AddTableDialog에서 사용
class NotBookedTableRVAdapter(var context: Context, val userBookData: UserBookData, var addTableDialog: AddTableDialog): RecyclerView.Adapter<NotBookedTableRVAdapter.Holder>() {
    val tableAL = ArrayList<BlankTable>()
    init{
        checkBlankTable()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.generalitem_button, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        Log.d("확인 NotBookedTableRVAdapter 바인드 수", tableAL.size.toString())
        return tableAL.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            var gButton: Button = itemView.findViewById(R.id.gButton)
            gButton.setText(tableAL[pos].floor.toString()+" 층 "+tableAL[pos].tableNum.toString()+" 번 테이블")
            gButton.setOnClickListener {
                var customDialog = CheckingDialog(context,tableAL[pos].floor.toString()+" 층 "+tableAL[pos].tableNum.toString()+" 번 테이블",
                    tableAL[pos].tableNum, this@NotBookedTableRVAdapter)
                customDialog!!.show()

            }
            //addTableDialog.addTG(gButton)


        }
    }

    public fun checkBlankTable(){
        val startTime:String = userBookData.bookStartTime
        val endTime:String = userBookData.bookEndTime
        //테이블 리스트를 만든다
        //데이터 베이스에서 이 예약시간대와 겹치는 시간대에 비어있는 테이블 찾아서 리스트 만든다.
        //예약 시작 시간과 끝나는 시간이 필요함
        tableAL.add(BlankTable(1, 3))
        tableAL.add(BlankTable(1, 4))
        tableAL.add(BlankTable(3, 7))
        tableAL.add(BlankTable(3, 9))

    }

    public fun addBook(tableNum: Int){
        //tableAL[pos].tableNum 을 데이터 베이스로 보낸다.
        addTableDialog.renewalTable()//예약을 데이터베이스로 올리고 비어있는 테이블 목록 새로고침 한다.
    }

    inner class BlankTable(val floor:Int, val tableNum:Int)



}