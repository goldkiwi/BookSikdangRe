package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

//테이블 삭제하는 다이얼로그
//위아래로 리사이클러뷰
//res_edittable_dialog 재사용
class DeleteTableDialog(context: Context, val userBookData: UserBookData): Dialog(context) {
    lateinit var beforeDeleteRV:RecyclerView
    lateinit var beforeDeleteRVAdapter:BeforeDeleteRVAdapter

    lateinit var afterDeleteRV:RecyclerView
    lateinit var afterDeleteRVAdapter:AfterDeleteRVAdapter

    var beforeButtonAl=ArrayList<ToggleButton>()
    var afterButtonAL=ArrayList<ToggleButton>()

    var floorAL = ArrayList<Int>()
    var tableNumAL = ArrayList<Int>()

    var dFloor =999
    var dNum = 999
    var mFloor = 999
    var mNum =999

    //삭제할 테이블이 몇 번째인지
    var sPos:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.res_deletetable_dialog)
        setContentView(R.layout.res_edittable_dialog)

        var beforeChangeTV:TextView = findViewById(R.id.beforeChangeTV)
        beforeChangeTV.setText("삭제 전")
        var afterChangeTV:TextView=findViewById(R.id.afterChangeTV)
        afterChangeTV.setText("메뉴 이동할 테이블")


        beforeDeleteRV = findViewById(R.id.tableIsBookedRV)
        //beforeDeleteRV = findViewById(R.id.beforeDeleteRV)
        beforeDeleteRVAdapter = BeforeDeleteRVAdapter(context, userBookData, this)
        beforeDeleteRV.adapter = beforeDeleteRVAdapter

        var RVLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        beforeDeleteRV.layoutManager=RVLayoutManager
        beforeDeleteRV.setHasFixedSize(true)


        afterDeleteRV = findViewById(R.id.tableNotBookedRV)
        //beforeDeleteRV = findViewById(R.id.beforeDeleteRV)
        afterDeleteRVAdapter = AfterDeleteRVAdapter(context, userBookData,this)
        afterDeleteRV.adapter = afterDeleteRVAdapter

        var RVLayoutManager2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        afterDeleteRV.layoutManager=RVLayoutManager2
        afterDeleteRV.setHasFixedSize(true)







        //삭제 버튼
        var editTableEditBtn: Button = findViewById(R.id.editTableEditBtn)
        editTableEditBtn.setText("삭제")
        editTableEditBtn.setOnClickListener {
            if (dFloor == 999 || dNum == 999 || mFloor == 999 || mNum == 999){
                val myToast = Toast.makeText(context, "삭제할 테이블과 메뉴를 이동할 테이블을 선택하십시오", Toast.LENGTH_SHORT).show()
            }
            else showCheckDialog(dFloor, dNum, mFloor, mNum)
        }

        var editTableCompleteButton:Button = findViewById(R.id.editTableCompleteButton)
        editTableCompleteButton.setOnClickListener {
            this.dismiss()
        }



    }


    public fun beforeButtonClick(pos:Int){
        sPos = pos
        for (i in 0..beforeButtonAl.size-1){
            if (i != pos){
                beforeButtonAl[i].isChecked = false
            }
        }
        dFloor = floorAL[pos]
        dNum = tableNumAL[pos]
        renewalAfterRV()
    }

    public fun afterButtonClick(pos:Int){
        for (i in 0..afterButtonAL.size-1){
            if (i != pos){
                afterButtonAL[i].isChecked = false
                Log.d("확인 DeleteTableDialog.afterButtonClick", " i: "+i.toString()+" pos: "+pos.toString())
            }
        }
        mFloor = floorAL[pos]
        mNum = tableNumAL[pos]
    }

    public fun renewalAfterRV(){
        afterButtonAL.clear()
        afterDeleteRVAdapter.notifyDataSetChanged()
    }

    public fun showCheckDialog(deleteFloor:Int, deleteNum:Int, migrateFloor:Int, migrateNum:Int){
        var customDialog = DeleteCheckDialog(context,deleteFloor,deleteNum,migrateFloor,migrateNum, this)
        customDialog!!.show()
    }

    public fun deleteTable(){
        //삭제할 테이블 정보와 메뉴를 이동시킬 테이블 번호를 데이터베이스에 전송
        this.dismiss()
    }
}