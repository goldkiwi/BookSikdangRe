package com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.example.sikdangbook_rest.Table.TableData_res
import com.example.sikdangbook_rest.Table.TableFloorFragment_res
import com.example.sikdangbook_rest.Table.Table_res
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_notification.*
import retrofit2.http.Url

//TableFloorSettingDialog 에서 사용
//층을 받아와 그 층의 테이블 정보 띄우고 테이블 정보 수정
class TableSettingDialog(context: Context, val sikdangNum: String, val floorNum: Int, var sikdangmainRes: SikdangMain_res): Dialog(context) {
    lateinit var tableLayout:ConstraintLayout
    //lateinit var tableData:TableData_res
    lateinit var ts_floorTV:TextView
    lateinit var ts_floorImg:ImageView
    var TPC = 0
    var moveNum = 0
    var nowFloor=0

    var buttonAL=ArrayList<Button>()

    var changedTableAL = ArrayList<Table_res>()

    var timeAL = ArrayList<String>()

    var switch = false





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_tablesetting_dialog)


        tableLayout = findViewById(R.id.ts_tableSettingLayout)
        //tableData = TableData_res("09:30")


        ts_floorTV = findViewById<TextView>(R.id.ts_floorTV)
        ts_floorTV.setText(sikdangmainRes.tableData.floorList[floorNum].toString()+"층")
        ts_floorImg = findViewById(R.id.ts_floorImg)
        Log.d("확인 TableSettingDialog", "1")

        setFloorImg()
        Log.d("확인 TableSettingDialog", "2")

        //initalTableSet()

        setTableAL()
        Log.d("확인 TableSettingDialog", "3")
        setTable()
        Log.d("확인 TableSettingDialog", "4")

        nowFloor=sikdangmainRes.tableData.floorList[floorNum]

        //변경버튼 클릭시
        //테이블 정보 새로 저장
        var ts_changeBtn:Button = findViewById(R.id.ts_changeBtn)
        ts_changeBtn.setOnClickListener {


            setNowLoc()
            //setTable()

            //Log.d("확인 변경좌표", "\n\n\n")

            /*
            for (i in 0..changedTableAL.size-1){
                tableLayout.removeView(buttonAL[i])
            }
            buttonAL=ArrayList<Button>()*/

            tableDataSet()

        }

        //사각형 테이블 추가버튼
        var  ts_addRectTableBtn:Button = findViewById(R.id.ts_addRectTableBtn)
        ts_addRectTableBtn.setOnClickListener {
            addTable(false)
        }
        //원형테이블 추가버튼
        var ts_addCircleTableBtn:Button = findViewById(R.id.ts_addCircleTableBtn)
        ts_addCircleTableBtn.setOnClickListener { addTable(true) }

        //층 변경 버튼
        var ts_changeFloorBtn:Button = findViewById(R.id.ts_changeFloorBtn)
        ts_changeFloorBtn.setOnClickListener {
            setNowLoc()
            showChangeFloorDialog()
        }

        //층 단면도 변경
        var ts_changeImage =findViewById<Button>(R.id.ts_changeImage)
        ts_changeImage.setOnClickListener {
            showChangeFloorImageDialog()
        }

        //층 삭제 버튼
        var ts_deleteFloorBtn = findViewById<Button>(R.id.ts_deleteFloorBtn)
        ts_deleteFloorBtn.setOnClickListener {
            showCheckingDeleteFloorDialog()
        }


        var ts_cancelBtn:Button = findViewById(R.id.ts_cancelBtn)
        ts_cancelBtn.setOnClickListener { this.dismiss() }




    }




    public fun dpToPx(dp: Int):Int {
        var density:Float = context.getResources().getDisplayMetrics().density;
        return Math.round(dp.toFloat() * density.toFloat())
    }

    public fun pxTodp(px: Float):Float {
        val metrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        val dp: Float = px / (metrics.densityDpi / 160f)
        //Log.d("확인 pxTodp", px.toString()+" "+Math.round(dp).toFloat().toString())
        return Math.round(dp).toFloat()


    }

    //데이터베이스에 접속해서 이미지 가져온다.
    private fun setFloorImg(){
        //ts_floorImg 에 이미지 넣는다.
        //아래는 임시데이터
        ts_floorImg.setImageResource(R.drawable.sikdangstr)

    }
    public fun setNowShape(){

    }

    public fun setTableAL(){
        //변경 테이블리스트 비우고 새로 집어넣음
        //Log.d("확인 setTable", "0")
        changedTableAL = ArrayList<Table_res>()
        var i = 0
        //Log.d("확인 setTable", "1")
        while(i<sikdangmainRes.tableData.tableNumList[floorNum]) {
            //Log.d("확인 setTable", "2 "+i.toString())
            //Log.d("확인 TableFloorFragment i", i.toString())
            var count = i
            if (floorNum == 0) {
                count = i
            } else {
                count = i + sikdangmainRes.tableData.accumTableNumList[floorNum - 1]//테이블리스트의 몇번째인가
            }
            //tableData.tableList[count]

            var tempTable=sikdangmainRes.tableData.tableList[count]

            changedTableAL.add(tempTable)

            i++

        }
        //Log.d("확인 setTable", "3")
    }

    //현재 테이블들의 위치를 저장
    fun setNowLoc(){
        for (i in 0..changedTableAL.size-1){
            var count = i
            var originWidth = pxTodp(tableLayout.width.toFloat())
            var originHeight = pxTodp(tableLayout.height.toFloat())

            var width = originWidth - changedTableAL[count].lengX
            var height = originHeight - changedTableAL[count].lengY

            changedTableAL[count].locX= pxTodp(buttonAL[count].x)/width
            changedTableAL[count].locy=pxTodp(buttonAL[count].y)/height
            changedTableAL[count].lengX= pxTodp(buttonAL[count].width.toFloat()).toInt()
            changedTableAL[count].lengY=pxTodp(buttonAL[count].height.toFloat()).toInt()

            Log.d("확인 변경좌표", i.toString()+" "+changedTableAL[count].locX.toString() + "  " + changedTableAL[count].locy.toString())
            Log.d("확인 변경크기", i.toString()+" "+changedTableAL[count].lengX.toString())
        }
    }

    public fun setTable(){
        //현재 테이블 모두 삭제하고 changedTableAL 에 따라 테이블 재배치

        Log.d("확인 setTable", "작동")
        for ( i in 0..buttonAL.size-1){
            tableLayout.removeView(buttonAL[i])
        }
        buttonAL=ArrayList<Button>()

        var i = 0
        while(i< changedTableAL.size){
            var count=i
            var button= Button(getContext())
            if (changedTableAL[count].isCircle == true)//원형테이블
            {
                var roundDrawable = context.getDrawable(R.drawable.button_round_gray)
                button.background = roundDrawable
            }
            else {
                button.setBackgroundColor(Color.parseColor("#CCCCCC"))
            }
            var moveX = 0f
            var moveY = 0f

            //var a = tableLayout.layoutParams

            var layoutParams = ConstraintLayout.LayoutParams(dpToPx(changedTableAL[count].lengX), dpToPx(changedTableAL[count].lengY))
            //with와 height에 픽셀값 들어감 => dp를 픽셀값으로 변환한 값 들어가야 한다.


            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.horizontalBias = changedTableAL[count].locX//0.5가 중앙
            layoutParams.verticalBias = changedTableAL[count].locy


            button.setLayoutParams(layoutParams)
            tableLayout.addView(button)

            button.x=changedTableAL[count].locX
            button.y=changedTableAL[count].locy

            button.setOnClickListener {
                //Log.d("확인 버튼클릭", "@@@@@@@@@@@@@@@@@@@@@@@@")
                var tNum = 0
                if (moveNum<10){
                    if (floorNum == 0) {
                        tNum = count
                    } else {
                        tNum = count + sikdangmainRes.tableData.accumTableNumList[floorNum - 1]//테이블리스트의 몇번째인가
                    }
                    //tableData.floorList[floorNum]
                    //Log.d("확인 버튼정보", changedTableAL[count].floor.toString()+changedTableAL[count].isCircle.toString()+changedTableAL[count].maxP.toString() )
                    showOneTableSettingDialog(changedTableAL[count].floor, tNum, changedTableAL[count].maxP, count)
                }
            }
            button.setOnTouchListener{ v, event ->
                when(event.action) {

                    MotionEvent.ACTION_DOWN -> {
                        moveX = v.x - event.rawX
                        moveY = v.y - event.rawY
                        var c = button.x
                        //Log.d("확인 버튼클릭", "액션다운")
                        moveNum = 0
                    }
                    MotionEvent.ACTION_MOVE -> {
                        v.animate()
                                .x(event.rawX + moveX)
                                .y(event.rawY + moveY)
                                .setDuration(0)
                                .start()
                        //Log.d("확인 버튼클릭", "액션무브")
                        moveNum+=1
                    }
                    MotionEvent.ACTION_UP -> {
                        //Log.d("확인 버튼클릭", "액션업")
                    }

                }
                false

            }


            buttonAL.add(button)


            i++

        }



    }

    public fun addTable(isCircle:Boolean){

        setNowLoc()
        var a =Table_res(0.0f, 0.0f, 30, 30, 1, sikdangmainRes.tableData.floorList[floorNum], true, isCircle)
        Log.d("확인 tableAL 크기 확인1: ", changedTableAL.size.toString())
        //changedTableAL.add(Table_res(0.0f, 0.0f, 30, 30, 1, tableData.floorList[floorNum], true, isCircle))

        changedTableAL.add(a)
        Log.d("확인 tableAL 크기 확인2: ", changedTableAL.size.toString())
        setTable()
    }




    //층수, 테이블번호, 인원수, changedTableAL 배열에서 몇 번째인지, 버튼 크기
    public fun showOneTableSettingDialog(tableFloor:Int, tableNum:Int, pNum:Int, alNum:Int){
        var customDialog = OneTableSettingDialog(context,tableFloor, tableNum, pNum, alNum, changedTableAL[alNum].lengX, changedTableAL[alNum].lengY, this)
        customDialog!!.show()
    }

    public fun showChangeFloorDialog(){
        var customDialog = ChangeFloorDialog(context, sikdangNum, nowFloor, this)
        customDialog!!.show()
    }

    public fun showChangeFloorImageDialog(){
        var customDialog = ChangeFloorImageDialog(context, sikdangNum, nowFloor, this, sikdangmainRes)
        customDialog!!.show()
    }

    public fun showCheckingDeleteFloorDialog(){
        var customDialog = CheckingDeleteFloorDialog(context, nowFloor, this)
        customDialog!!.show()
    }



    public fun deleteTable(alNum:Int){
        setNowLoc()
        changedTableAL.remove(changedTableAL[alNum])
        setTable()
    }

    public fun changeOneTableSetting(alNum:Int, pNum:Int, newLengX:Int, newLengY:Int){
        setNowLoc()
        changedTableAL[alNum].maxP=pNum
        changedTableAL[alNum].lengX = newLengX
        changedTableAL[alNum].lengY = newLengY
        setTable()
    }

    public fun setNewFloor(newFloor:Int){
        ts_floorTV.setText(newFloor.toString()+"층")
        nowFloor=newFloor
    }


    public fun setNewFloorImg(uri:String){
        Glide.with(context)
                .load(uri)
                .apply(RequestOptions())
                .into(ts_floorImg)
    }

    //데이터베이스 접속해서 이 층 삭제
    public fun deleteThisFloor(){
        //삭제하고 이 다이얼로그 닫느다.
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId)
        ref.child("Booked").child(sikdangmainRes.floorList[floorNum]).removeValue()
        ref.child("TableInfo").child(sikdangmainRes.floorList[floorNum]).removeValue()


        this.dismiss()
    }

    //데이터베이스에 층 정보 올림
    private fun tableDataSet(){
        Log.d("확인 TableSettingDialog.tableDataSet(): ", "올리기 시작")
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId).child("TableInfo").child("floor_"+sikdangmainRes.tableData.floorList[floorNum])


        var tableHashMap =hashMapOf<String, Any>()


        for(i in 0..changedTableAL.size-1){
            var tempShape = "rect"
            if (changedTableAL[i].isCircle) tempShape = "circle"

            var tempTableHashMap =hashMapOf<String, Any>(
                "capacity" to changedTableAL[i].maxP,
                "height" to changedTableAL[i].lengY,
                "width" to changedTableAL[i].lengX,
                "x" to changedTableAL[i].locX,
                "y" to changedTableAL[i].locy,
                "shape" to tempShape
            )
            //if (changedTableAL[i].isCircle) tempTableHashMap.put("shape", tempShape)

            tableHashMap.put("table"+(i+1).toString(), tempTableHashMap)
            if(i == changedTableAL.size-1) getTimeAtBooked()

        }
        ref.setValue(tableHashMap).addOnSuccessListener {
            Log.d("확인 TableSettingDialog.tableDataSet(): ", "올리기 성공")
        }.addOnFailureListener {
            Toast.makeText(context, "테이블 업데이트 실패.", Toast.LENGTH_SHORT).show()

        }
        //changedTableAL
        // changedTableAL 을 데이터베이스로 넘긴다
        // 데이터베이스에 이 층에 속해있는 데이터를 이걸로 대체
        //floorNum 사용하거나 이전 다이얼로그에서 floor 받아와서 층 정보 넘긴다.

        //이후 데이터 다시 불러옴
        //getTableData()
        //setTableAL()
        //setTable()

        //다시 불러오지 않고 그냥 다이얼로그 닫게 할 수도 있음
        this.dismiss()
    }

    public fun getTimeAtBooked(){
        Log.d("확인 getTimeAtBooked() 예약 시간 가져오기 ", "작동 하는가? 층수 : "+sikdangmainRes.tableData.floorList[floorNum].toString())
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId).child("Booked").child("floor_"+sikdangmainRes.tableData.floorList[floorNum])

        switch = true
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(switch == true){
                    for (tableBooked in snapshot.children) {
                        Log.d("확인 getTimeAtBooked() 예약 시간 가져오기 ", tableBooked.key.toString())
                        timeAL.add(tableBooked.key.toString())
                    }
                    setTableOnTime()
                    switch = false
                }


            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 getTableBookedInfo()", "5 getFromDB : ${error}")
            }
        })
    }

    public fun setTableOnTime(){
        Log.d("확인 setTableOnTime() 장동 시기 확인 ", "작동 하는가? 시간 수 : "+timeAL.toString())
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId).child("Booked").child("floor_"+sikdangmainRes.tableData.floorList[floorNum])

        var timeHashMap =hashMapOf<String, Any>()

        for (i in 0..timeAL.size-1){
            Log.d("확인 getTimeAtBooked() 예약 시간 가져오기 ", "시간수"+ timeAL.size.toString() + "테이블수"+changedTableAL.size.toString())
            for (j in 0..changedTableAL.size-1) {
                var tempTimeHashMap = hashMapOf<String, Any>(
                        "mutex" to 1
                )
                timeHashMap.put("table" + (j + 1).toString(), tempTimeHashMap)
            }
            var tempBookInfo = hashMapOf<String, Any>(
                    "current" to changedTableAL.size,
                    "max" to changedTableAL.size
            )
            Log.d("확인 getTimeAtBooked() 예약 시간 가져오기 ", "${timeHashMap}")
            timeHashMap.put("BookInfo", tempBookInfo)
            ref.child(timeAL[i]).setValue(timeHashMap).addOnCompleteListener {
                Log.d("확인 getTimeAtBooked() 예약 시간 가져오기 ", "올리기 성공")
                sikdangmainRes.getTableDataLineNum = 0
                sikdangmainRes.getTableDataFromDB()
            }

        }



    }






}