package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

//좌측에 변경전 테이블 우측에 변경 후 테이블 놓고 변경할 테이블 선택하는 다이얼로그
//BookEditDialog에서 사용
class EditTableDialog(context: Context, val userBookData: UserBookData): Dialog(context) {
    private var isBookedToggleButtonAL = ArrayList<ToggleButton>()
    private var notBookedToggleButtonAL = ArrayList<ToggleButton>()

    var notBookedFloorAL = ArrayList<Int>()
    var notBookedTableNumAL = ArrayList<Int>()

    lateinit var tableIsBookedRV: RecyclerView
    lateinit var tableIsBookedRVAdapter:TableIsBookedRVAdapter

    lateinit var tableNotBookedRV: RecyclerView
    lateinit var tableNotBookedRVAdapter: TableNotBookedRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_edittable_dialog)

        checkBlankTable()


        tableIsBookedRV = findViewById(R.id.tableIsBookedRV)
        tableIsBookedRVAdapter = TableIsBookedRVAdapter(context, userBookData, this)
        tableIsBookedRV.adapter = tableIsBookedRVAdapter

        var RVLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tableIsBookedRV.layoutManager=RVLayoutManager
        tableIsBookedRV.setHasFixedSize(true)


        tableNotBookedRV = findViewById(R.id.tableNotBookedRV)
        tableNotBookedRVAdapter = TableNotBookedRVAdapter(context, notBookedFloorAL, notBookedTableNumAL, this)
        tableNotBookedRV.adapter = tableNotBookedRVAdapter

        var RVLayoutManager2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tableNotBookedRV.layoutManager=RVLayoutManager2
        tableNotBookedRV.setHasFixedSize(true)

        var editTableEditBtn: Button = findViewById(R.id.editTableEditBtn)
        editTableEditBtn.setOnClickListener {
            checkBookedTB()
        }

        var editTableCompleteButton:Button = findViewById(R.id.editTableCompleteButton)
        editTableCompleteButton.setOnClickListener {
            this.dismiss()
        }
    }




    public fun isBookedAddTB(toggleButton: ToggleButton){
        isBookedToggleButtonAL.add(toggleButton)
    }
    public fun notBookedAddTB(toggleButton: ToggleButton){
        notBookedToggleButtonAL.add(toggleButton)
    }

    public fun checkIsBookedTB(pos:Int){
        for (i in 0..userBookData.tableAL.size-1){
            if(i != pos) {
                //Log.d("확인 EditTableDialog 변경전테이블 위치 확인", i.toString()+isBookedToggleButtonAL.size.toString())
                if (isBookedToggleButtonAL[i].isChecked) isBookedToggleButtonAL[i].isChecked = false
            }
        }
    }
    public fun checkNotBookedTB(pos:Int){
        for (i in 0..notBookedToggleButtonAL.size-1){
            if(i != pos) {
                //Log.d("확인 EditTableDialog 변경후테이블 위치 확인", i.toString()+notBookedToggleButtonAL.size.toString())
                notBookedToggleButtonAL[i].isChecked = false
            }
        }
    }

    private fun checkBookedTB(){
        var bookedTableNum:Int = 0
        var bookedTableFloor = 0
        var bookedIsChecked = false

        var notBookedTableNum:Int = 0
        var notBookedTableFloor = 0
        var notBookedIsChecked = false


        for (i in 0..userBookData.tableAL.size-1){
            if (isBookedToggleButtonAL[i].isChecked){
                bookedTableNum = userBookData.tableAL[i].tableNum
                bookedTableFloor = userBookData.tableAL[i].tableFloor
                bookedIsChecked =true
                break
            }
        }
        for (i in 0..notBookedTableNumAL.size-1){
            if (notBookedToggleButtonAL[i].isChecked){
                notBookedTableNum = notBookedTableNumAL[i]
                notBookedTableFloor=notBookedFloorAL[i]
                notBookedIsChecked =true
                break
            }
        }
        if (bookedIsChecked == true && notBookedIsChecked == true){
            Log.d("확인 EditTableDialog", "두개 변경")
            showCheckDialog(notBookedTableFloor, notBookedTableNum, bookedTableFloor, bookedTableNum)
        }
        else{
            Log.d("확인 EditTableDialog", "변경 안함")
            val myToast = Toast.makeText(context, "변경 전 후 테이블을 모두 선택해 주십시오", Toast.LENGTH_SHORT).show()
        }
    }

    public fun checkBlankTable(){
        val startTime:String = userBookData.bookStartTime
        val endTime:String = userBookData.bookEndTime
        //NotBookedTableRVAdapter 의 checkBlankTable() 와 비슷하게 만듦
        //테이블 리스트를 만든다
        //데이터 베이스에서 이 예약시간대와 겹치는 시간대에 비어있는 테이블 찾아서 리스트 만든다.
        //예약 시작 시간과 끝나는 시간이 필요함
        /*
        tableAL.add(BlankTable(1, 3))
        tableAL.add(BlankTable(1, 4))
        tableAL.add(BlankTable(3, 7))
        tableAL.add(BlankTable(3, 9))*/

        notBookedFloorAL.add(1)
        notBookedFloorAL.add(1)
        notBookedFloorAL.add(3)
        notBookedFloorAL.add(3)
        notBookedTableNumAL.add(3)
        notBookedTableNumAL.add(4)
        notBookedTableNumAL.add(7)
        notBookedTableNumAL.add(9)

    }

    public fun renewalTable(){
        //tableIsBookedRVAdapter.notifyDataSetChanged()
        //tableNotBookedRVAdapter.notifyDataSetChanged()
        this.dismiss()
    }
    //변경여부 확인 다이얼로그
    private fun showCheckDialog(notFloor:Int, notNum:Int, isFloor:Int, isNum:Int){
        var customDialog = EditCheckDialog(context, notFloor, notNum, isFloor, isNum, this)
        customDialog!!.show()
    }

    public fun editTable(beforeTable:Int, afterTable:Int){
        //변경 전 테이블 번호와 변경 후 테이블 번호를 데이터베이스에 전송 후 데이터베이스에서 예약 정보 변경
        //이후 테이블 새로 고침
        Log.d("확인 EditTableDialog.editTable", "변경전 테이블번호:"+beforeTable.toString()+" 변경 후 : "+ afterTable.toString())
       renewalTable()
    }
}