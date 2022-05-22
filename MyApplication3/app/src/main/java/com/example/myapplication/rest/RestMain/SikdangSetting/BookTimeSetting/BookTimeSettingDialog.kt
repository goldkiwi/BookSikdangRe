package com.example.myapplication.rest.RestMain.SikdangSetting.BookTimeSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.example.myapplication.rest.Time.TempTimeClass
import com.example.sikdangbook_rest.Time.TimeLineAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//SikdangSettingDialog에서 사용
//예약 가능 시간 목록 띄우고
//여기서 시간 하나 선택하면 삭제 가능
//추가버튼으로 추가 가능
//텀 변경 버튼으로 예약 텀 변경 가능
//저장버튼 클릭시 변경사항 저장
class BookTimeSettingDialog(context: Context, val sikdangNum:String, var sikdangmainRes: SikdangMain_res): Dialog(context) {

    lateinit var bookTimeSettingRV : RecyclerView
    lateinit var bookTimeSettingRVAdapter:BookTimeSettingRVAdapter


    //lateinit var tempTimeClass:TempTimeClass

    public var nowTerm:String ="0000"

    var tempTimeAL = sikdangmainRes.timeAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_booktimesetting_dialog)

        getTime()
        getTerm()

        //sikdangmainRes.timeAL



        bookTimeSettingRV  = findViewById(R.id.bookTimeSettingRV)
        bookTimeSettingRVAdapter = BookTimeSettingRVAdapter(context, this)
        bookTimeSettingRV.adapter = bookTimeSettingRVAdapter



        var timeLM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        bookTimeSettingRV.layoutManager=timeLM
        bookTimeSettingRV.setHasFixedSize(true)


        //bookTimeSettingRV.setis
        //bookTimeSettingRVAdapter.setis


        var bts_addTimeBtn: Button = findViewById(R.id.bts_addTimeBtn)
        bts_addTimeBtn.setOnClickListener {
            showAddTimeDialog()
        }
        var bts_editTermBtn:Button = findViewById(R.id.bts_editTermBtn)
        bts_editTermBtn.setOnClickListener {
            showEditTimeTermDialog()
        }

        var bts_saveBtn:Button = findViewById(R.id.bts_saveBtn)
        bts_saveBtn.setOnClickListener {
            saveTimeInfo()
        }




        var bts_cancelBtn:Button = findViewById(R.id.bts_cancelBtn)
        bts_cancelBtn.setOnClickListener { this.dismiss() }
    }

    public fun deleteTime(slectedTime:String){
        tempTimeAL.remove(slectedTime)
        renewalRV()
    }
    public fun renewalRV(){
        Log.d("확인 리뉴얼", "되나?")
        bookTimeSettingRVAdapter.ButtonALReset()
        bookTimeSettingRVAdapter.vartimePoint=0
        bookTimeSettingRVAdapter.notifyDataSetChanged()
        //bookTimeSettingRVAdapter.setButtonText()
    }

    //데이터베이스에서 시간 정보 가져온다.
    public fun getTime(){
        //tempTimeClass = TempTimeClass(1111)
    }

    public fun addTime(newTime:String):Boolean{
        for (i in 0..tempTimeAL.size-2){
            var nowTime = tempTimeAL[i]
            var nextTime = tempTimeAL[i+1]

            var nowTimeToInt:Int = 0
            var newTimeToInt = 0
            var nextTimeToInt = 0

            if(nowTime.slice(IntRange(6, 7))=="오전"){
                nowTimeToInt =(nowTime.slice(IntRange(0, 1))+nowTime.slice(IntRange(3, 4))).toInt()
            }
            else{
                nowTimeToInt =(nowTime.slice(IntRange(0, 1))+nowTime.slice(IntRange(3, 4))).toInt() + 1200
            }

            if(newTime.slice(IntRange(6, 7))=="오전"){
                newTimeToInt =(newTime.slice(IntRange(0, 1))+newTime.slice(IntRange(3, 4))).toInt()
            }
            else{
                newTimeToInt =(newTime.slice(IntRange(0, 1))+newTime.slice(IntRange(3, 4))).toInt() + 1200
            }


            if(nextTime.slice(IntRange(6, 7))=="오전"){
                nextTimeToInt =(nextTime.slice(IntRange(0, 1))+nextTime.slice(IntRange(3, 4))).toInt()
            }
            else{
                nextTimeToInt =(nextTime.slice(IntRange(0, 1))+nextTime.slice(IntRange(3, 4))).toInt() + 1200
            }

            Log.d("확인 AddTime", "now : " + nowTimeToInt.toString() + " new : "+newTimeToInt.toString())



            if (newTimeToInt == nowTimeToInt.toInt()){
                val myToast = Toast.makeText(context, "이미 설정되어있는 시간입니다.", Toast.LENGTH_SHORT).show()
                return false
            }
            else if (newTimeToInt.toInt() < nextTimeToInt){
                Log.d("확인 AddTime", "중간에 추가"+i.toString())
                var newTimeAL = ArrayList<String>()
                if(newTimeToInt.toInt() < nowTimeToInt.toInt()){
                    Log.d("확인 AddTime", newTime+" / "+nextTime+" 처음에 추가"+i.toString())
                    newTimeAL.add(newTime)
                    for (j in i..tempTimeAL.size-1){
                        newTimeAL.add(tempTimeAL[j])
                    }
                }
                else{
                    Log.d("확인 AddTime", "다음으로 넘어감")
                    for (j in 0..i){
                        newTimeAL.add(tempTimeAL[j])
                    }
                    newTimeAL.add(newTime)
                    for (j in i+1..tempTimeAL.size-1){
                        newTimeAL.add(tempTimeAL[j])
                    }
                }
                tempTimeAL=newTimeAL
                Log.d("확인 AddTime", tempTimeAL.size.toString())
                renewalRV()
                return true
            }

        }
        Log.d("확인 AddTime", "마지막 추가")
        renewalRV()
        tempTimeAL.add(newTime)

        return true

    }

    public fun showAddTimeDialog(){
        var customDialog = AddTimeDialog(context, this)
        customDialog!!.show()
    }

    public fun showEditTimeTermDialog(){
        var customDialog = EditTimeTermDialog(context, nowTerm, this)
        customDialog!!.show()
    }

    public fun getTerm(){
        nowTerm="0100"
    }
    public fun editTerm(newTerm:String){
        nowTerm=newTerm
    }

    //데이터베이스에 접속해 현재 시간정보 저장
    public fun saveTimeInfo(){
        //아래 두개 저장
        //tempTimeClass.timeArrayList
        //nowTerm


        /*
        for (i in 0..sikdangmainRes.floorList.size-1){
            var tempBookInfo = hashMapOf<String, Any>(
                    "cerrent" to sikdangmainRes.tableData.tableNumList[i],
                    "max" to sikdangmainRes.tableData.tableNumList[i]
            )
            var temptableMutex = hashMapOf<String, Any>(
                    "mutex" to 1
            )
            var timeHashMap =hashMapOf<String, Any>(
                    "BookInfo" to tempBookInfo,
                    "table1" to temptableMutex
            )
            var floorHashMap = hashMapOf<String, Any>()
            for (j in 0..tempTimeAL.size-1){
                floorHashMap.put(tempTimeAL[i], timeHashMap)
            }

            val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Tables").child(sikdangmainRes.sikdangId).child("Booked").child(sikdangmainRes.floorList[i]).child(tempTimeAL)

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



        }*/




        var temptableMutex = hashMapOf<String, Any>(
                "mutex" to 1
        )
        var floorHashMap = hashMapOf<String, Any>()
        for (i in 0..sikdangmainRes.floorList.size-1){
            var tempBookInfo = hashMapOf<String, Any>(
                    "cerrent" to sikdangmainRes.tableData.tableNumList[i],
                    "max" to sikdangmainRes.tableData.tableNumList[i]
            )
            var timeHashMap = hashMapOf<String, Any>()

            var tableHashMap = hashMapOf<String, Any>()
            tableHashMap.put("BookInfo", tempBookInfo)
            for (k in 0..sikdangmainRes.tableNumAL[i]-1){
                tableHashMap.put("table"+(k+1).toString(), temptableMutex)
            }

            for (j in 0..tempTimeAL.size - 1){
                timeHashMap.put(tempTimeAL[j], tableHashMap)
            }
            floorHashMap.put(sikdangmainRes.floorList[i], timeHashMap)

        }

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId).child("Booked")

        ref.setValue(floorHashMap)
                .addOnSuccessListener {
                    //finish()
                    //upSikdangOnUser(postId)
                    //setTimeAl(newFloor)
                    Log.d("확인 BookTimeSettingDialog.saveTimeInfo()", "시간대 추가 성공")
                    sikdangmainRes.getTableDataLineNum=0
                    sikdangmainRes.getTableDataFromDB()
                    this.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(context, "TableFloorSettingDialog().addFloor", Toast.LENGTH_SHORT).show()
                }




        this.dismiss()
    }


}
