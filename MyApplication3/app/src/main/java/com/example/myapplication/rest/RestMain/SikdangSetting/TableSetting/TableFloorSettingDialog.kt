package com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.example.myapplication.rest.RestMain.SikdangSetting.AddMenuIngRVAdapter
import com.example.sikdangbook_rest.Table.TableData_res
import com.example.sikdangbook_rest.Table.Table_res
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_notification.*

//SikdangSettingDialog 에서 사용
//테이블 층수 선택 / 테이블 층 추가 가능
class TableFloorSettingDialog(context: Context, val sikdangNum: String, var sikdangmainRes: SikdangMain_res): Dialog(context) {


    var timeAL = ArrayList<String>()
    var switch = false

    lateinit var floorListRV:RecyclerView
    lateinit var RVAdapter:FloorListRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_tablefloorsetting_dialog)
        getTableData()


        floorListRV = findViewById(R.id.floorListRV)


        RVAdapter = FloorListRVAdapter(context, this, sikdangmainRes)
        floorListRV.adapter = RVAdapter

        var LM2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        floorListRV.layoutManager=LM2
        floorListRV.setHasFixedSize(true)

        var tfs_addFloorBtn: Button = findViewById(R.id.tfs_addFloorBtn)
        tfs_addFloorBtn.setOnClickListener {
            addFloor()
        }

        var tfs_closeBtn:Button = findViewById(R.id.tfs_closeBtn)
        tfs_closeBtn.setOnClickListener { this.dismiss() }

    }
    //데이터베이스 접속
    private fun getTableData(){
        //테이블정보만 가져오면 됨
    }


    //floor은 몇 층인지 직접적으로 알려줌
    //floorNum은 이 층이 신당이 존재하는 층들 중 몇 번째인지
    //식당이 5, 6, 7, 8층이면
    //5층일 때 floor 은 5 floorNum 은 0
    //6층일 때 floor 은 6 floorNum은 1
    //일단 TableData_res의 구조때문에 둘다 받는데 층만으로 데이터 받아올 수 있으면 굳이 FloorNum 사용할 필요 없음

    public fun showTableSettingDialog(floorNum:Int, floor:Int){
        Log.d("TableFloorSettingDialog", "showTableSettingDialog")
        var customDialog = TableSettingDialog(context,sikdangNum, floorNum, sikdangmainRes)
        customDialog!!.show()
    }

    private fun addFloor(){
        //새 층은 현재 최대 층보다 1층 추가
        var newFloor = sikdangmainRes.tableData.floorList[sikdangmainRes.tableData.floorList.size-1]+1
        //데이터베이스로 층 전송하고 그 층에 임의의 테이블데이터 하나 추가
        //굳이 이 클래스 형태가 되지 않아도 됨 그냥 데이터베이스에 전송만 가능하면 된다.
        var newTable= Table_res(0.1F, 0.1F, 50, 50, 2, newFloor, false, false)

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId)


        var tempTable = hashMapOf<String, Any>(
                "capacity" to 2,
                "height" to 30,
                "width" to 30,
                "x" to 0.5,
                "y" to 0.5,
                "shape" to "circle"
        )



        ref.child("TableInfo").child("floor_"+newFloor.toString()).child("table1").setValue(tempTable)
                .addOnSuccessListener {
                    //finish()
                    //upSikdangOnUser(postId)
                    setTimeAl(newFloor)
                }.addOnFailureListener {
                    Toast.makeText(context, "TableFloorSettingDialog().addFloor", Toast.LENGTH_SHORT).show()
                }






        //데이터 전송후 다시 데이터 불러온다.
        getTableData()
        //리사이클러뷰 갱신
        renewalRV()
    }


    public fun setTimeAl(newFloor:Int){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId).child("Booked").child(sikdangmainRes.floorList[0])
        switch = true
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(switch == true){
                    for (tableBooked in snapshot.children) {
                        Log.d("확인 getTimeAtBooked() 예약 시간 가져오기 ", tableBooked.key.toString())
                        timeAL.add(tableBooked.key.toString())
                    }
                    setTableOnTime(newFloor)
                    switch = false
                }


            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 getTableBookedInfo()", "5 getFromDB : ${error}")
            }
        })





    }



    public fun setTableOnTime(newFloor:Int){

        var tempBookInfo = hashMapOf<String, Any>(
                "cerrent" to 1,
                "max" to 1
        )
        var temptableMutex = hashMapOf<String, Any>(
                "mutex" to 1
        )
        var timeHashMap =hashMapOf<String, Any>(
                "BookInfo" to tempBookInfo,
                "table1" to temptableMutex
        )
        var floorHashMap = hashMapOf<String, Any>()
        for (i in 0..timeAL.size-1){
            floorHashMap.put(timeAL[i], timeHashMap)
        }

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId).child("Booked").child("floor_"+newFloor.toString())


        ref.setValue(floorHashMap)
                .addOnSuccessListener {
                    //finish()
                    //upSikdangOnUser(postId)
                    //setTimeAl(newFloor)
                    Log.d("확인 TableFloorFragment.setTableOnTime", "시간에 층 추가 성공")
                    sikdangmainRes.getTableDataLineNum=0
                    sikdangmainRes.getTableDataFromDB()
                    this.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(context, "TableFloorSettingDialog().addFloor", Toast.LENGTH_SHORT).show()
                }


    }



    public fun renewalRV(){
        RVAdapter.notifyDataSetChanged()

    }

}