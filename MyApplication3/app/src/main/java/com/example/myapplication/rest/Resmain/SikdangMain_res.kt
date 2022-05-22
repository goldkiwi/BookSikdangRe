package com.example.myapplication.rest.Resmain

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.rest.ResInfo.ResInfoActivity
import com.example.myapplication.rest.RestMain.SikdangSetting.EditSikdangImageDialog
import com.example.myapplication.rest.RestMain.SikdangSetting.MenuEditDialog
import com.example.myapplication.rest.RestMain.SikdangSetting.SikdangSettingDialog
import com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting.ChangeFloorImageDialog
import com.example.myapplication.rest.Table.TableFromDBData
import com.example.sikdangbook_rest.Table.TableData_res
import com.example.sikdangbook_rest.Table.TableFloorVPAdapter_res
import com.example.sikdangbook_rest.Table.Table_res
import com.example.sikdangbook_rest.Time.TimeSelectDialog
import com.google.firebase.database.*
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SikdangMain_res:AppCompatActivity() {
    lateinit var sm_drawerLayout: DrawerLayout
    lateinit var tableFloorVP: ViewPager2
    lateinit var vpAdapter: TableFloorVPAdapter_res
    lateinit var nowBtn: ToggleButton
    lateinit var selectedTimeTV: TextView
    lateinit var sm_beforeCheckBtn: ToggleButton
    lateinit var sm_afterCheckBtn: ToggleButton

    lateinit var sm_messageRV: RecyclerView
    lateinit var messageRVAdapter: ResMainMessageRVAdapter

    lateinit var sikdangimg:Bitmap
    var sikdangimgCheckNum = 0
    lateinit var menuEditDialog:MenuEditDialog
    lateinit var changeFloorImageDialog:ChangeFloorImageDialog
    lateinit var editSikdangImageDialog:EditSikdangImageDialog



    private var timeNum = ""
    var sikdangName = "식다아아아앙이름"
    var sikdangId = "10987654321"
    var sikdangType=""

    var showTime="09:00 오전"

    var messages = ArrayList<MessageData>()

    var tableData = TableData_res()

    var floorList = ArrayList<String>()  //테이블 각각 몇층에 있는지
    var intFloorIist = ArrayList<Int>()
    var tableFromDBDataAL=ArrayList<TableFromDBData>() // 테이블 모두 저장
    var tableNumAL=ArrayList<Int>() // 층별로 테이블 각각 몇개인지
    var accumTableNumList = ArrayList<Int>()//테이블 개수 축적
    var tableIsBookedAL = ArrayList<Int>()

    var timeAL = ArrayList<String>()
    var timeSwitch = false

    var newSikdangImgUri : Uri? = null
    public var newMenuImgUri : Uri? = null
    public var newFloorImageUri : Uri? = null

    var getTableDataLineNum : Int = 3

    public var msgKeyAL = ArrayList<String>()
    public var msgAL = ArrayList<MsgData>()
    public var msgTableDataAL = ArrayList<ArrayList<MsgTableData>>() //예약별 층/테이블별
    public var msgBookInfoDataAL = ArrayList<ArrayList<MsgBookInfo>>()
    public var userDataAL = ArrayList<OtherUserData>()


    public var msgLineNum = 4




    //전체 알림인지 처리전 알림인지 체크크
    var isAll: Boolean = true

    lateinit var imageView4:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.res_sikdangmain)

        var myIntent = getIntent()
        sikdangId = myIntent.getStringExtra("sikdangId")!!
        sikdangName = myIntent.getStringExtra("sikdangName")!!
        sikdangType = myIntent.getStringExtra("sikdangType")!!

        getTableDataLineNum=0
        getTableDataFromDB()

        sm_drawerLayout = findViewById(R.id.sm_drawerLayout)
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("kk:mm")
        val curTime = dateFormat.format(Date(time))
        //Log.d("확인 시간 정상 확인", curTime.toString())
        //val myToast = Toast.makeText(this, curTime.toString(), Toast.LENGTH_SHORT).show()

        Log.d("확인 CSikdangMain_res", "4")
        timeNum = timeNum + curTime[0] + curTime[1] + curTime[3] + curTime[4]

        Log.d("확인 CSikdangMain_res", "5")

        //setTable()

        setMessage()
        setTable()
        getMsgKeyFromDB()


        imageView4=findViewById(R.id.imageView4)


        var timeselectBtn: Button = findViewById(R.id.timeselectBtn)
        timeselectBtn.setOnClickListener {
            var customDialog = TimeSelectDialog(this, this)
            customDialog!!.show()
        }

        Log.d("확인 CSikdangMain_res", "6")
        nowBtn = findViewById<ToggleButton>(R.id.nowBtn)
        nowBtn.isChecked = true
        selectedTimeTV = findViewById<TextView>(R.id.selectedTimeTV)
        selectedTimeTV.setText(curTime.toString())
        nowBtn.setOnClickListener {
            nowBtn.isChecked = true
            val time = System.currentTimeMillis()
            val dateFormat = SimpleDateFormat("kk:mm")
            val curTime = dateFormat.format(Date(time))
            selectedTimeTV.setText(curTime.toString())

        }
        Log.d("확인 CSikdangMain_res", "7")
        var sm_infoBtn: Button = findViewById(R.id.sm_infoBtn)
        sm_infoBtn.setOnClickListener {
            val intent = Intent(this, ResInfoActivity::class.java)
            startActivity(intent)
        }

        //식당 설정 버튼
        var sikdangSettingbtn: Button = findViewById(R.id.sikdangSettingbtn)
        sikdangSettingbtn.setOnClickListener {
            showSikdangSettingDialog()
        }

        //식당 선택 버튼
        var sm_choiceSikdangBtn: Button = findViewById(R.id.sm_choiceSikdangBtn)
        sm_choiceSikdangBtn.setOnClickListener {
            this.finish()
        }

        var sm_messageTV: Button = findViewById(R.id.sm_messageTV)
        sm_messageTV.setOnClickListener {
            sm_drawerLayout.openDrawer(GravityCompat.END)
        }

        sm_beforeCheckBtn = findViewById(R.id.sm_beforeCheckBtn)
        sm_beforeCheckBtn.text = "처리전 알림"
        sm_beforeCheckBtn.textOn = "처리전 알림"
        sm_beforeCheckBtn.textOff = "처리전 알림"

        sm_afterCheckBtn = findViewById(R.id.sm_afterCheckBtn)
        sm_afterCheckBtn.text = "전체 알림"
        sm_afterCheckBtn.textOn = "전체 알림"
        sm_afterCheckBtn.textOff = "전체 알림"


        sm_messageRV = findViewById(R.id.sm_messageRV)
        messageRVAdapter = ResMainMessageRVAdapter(this, this)
        sm_messageRV.adapter = messageRVAdapter

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        sm_messageRV.layoutManager = layoutManager
        sm_messageRV.setHasFixedSize(true)


        Log.d("확인 CSikdangMain_res", "8")

        val resRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangId)

        //예약이 추가될 때
        resRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (tableInfo in snapshot.children) {
                    //Log.d("확인  getTableDataFromDB()", "getFromDB : "+tableInfo.key.toString())
                    //floorList.add(tableInfo.key.toString())
                }
                getMsgKeyFromDB()
                Toast.makeText(this@SikdangMain_res, " 예약이 추가되었습니다.", Toast.LENGTH_LONG).show();

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
                Toast.makeText(this@SikdangMain_res, " 데이터베이스 정보변경 접근실패", Toast.LENGTH_LONG).show();
            }
        })



    }

    var backPressed = false
    var pressedTime = System.currentTimeMillis()

    override fun onBackPressed() {
        //super.onBackPressed()
        if (backPressed == false) {
            Toast.makeText(this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis()
            backPressed = true
        } else {
            var seconds = (System.currentTimeMillis() - pressedTime).toInt();
            if (seconds > 10000) {
                Log.d("확인 종료 안됨", seconds.toString())
                Toast.makeText(this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                backPressed = true;
            } else {

                //Log.d("확인 종료@@@@@@@@@@@@@@@@@@@", "1")
                //Toast.makeText(this, " 종료@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@." , Toast.LENGTH_LONG).show();
                super.onBackPressed()
                //finish(); // app 종료 시키기
            }
        }

    }

    inner class FloorImage(var floor:String, var url:String)
    //층 이미지

    public var floorUrlAL = ArrayList<FloorImage>()


    //층 이미지 가져오는 루틴

    var imageSwitch = 1
    public fun getFloorImageFromDB(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("floorUrl")
        imageSwitch =0

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (imageSwitch== 0){
                    for (tableInfo in snapshot.children) {
                        Log.d("확인 getMsgKeyFromDB()", tableInfo.key.toString())
                        floorUrlAL.add(FloorImage(tableInfo.key.toString(), tableInfo.value.toString()))
                    }
                    setFloorImage()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
            }
        })
    }

    public fun setFloorImage(){
        //vpAdapter.setImageByUrl()
    }









    //데이터베이스에서 테이블 목록
    inner class MsgTableData(var floorTable:String, var menuNameNum:String)
    inner class MsgBookInfo (var floor:String, var table:String, var menu:String, var cnt:Int)

    inner class MsgData(var bookTime:String, var payTime:String, var totalPay:Int, var userId:String)
    inner class OtherUserData(var username:String, var phone_number:String)


    //예약 추가될 때


    //예약의 키 받아옴

    public fun getMsgKeyFromDB(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangId)

        msgKeyAL.clear()
        msgLineNum = 0

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if ( msgLineNum == 0){
                    for (tableInfo in snapshot.children) {
                        Log.d("확인 getMsgKeyFromDB()", tableInfo.key.toString())
                        msgKeyAL.add(tableInfo.key.toString())
                    }
                    msgLineNum=1
                    getMsgByKey()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
            }
        })
    }


    //키 기반 예약 정보 받아옴
    public fun getMsgByKey(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangId)

        msgAL.clear()
        for (i in 0..msgKeyAL.size-1){
            ref.child(msgKeyAL[i]).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if ( msgLineNum == 1){
                        var tempMsg = MsgData("", "", 0, "")
                        for (tableInfo in snapshot.children) {
                            Log.d("확인 getMsgByKey()", tableInfo.key.toString())
                            //msgKeyAL.add(tableInfo.key.toString())
                            if (tableInfo.key.toString() == "bookTime") tempMsg.bookTime = tableInfo.value.toString()
                            if (tableInfo.key.toString() == "payTime") tempMsg.payTime = tableInfo.value.toString()
                            if (tableInfo.key.toString() == "totalPay") tempMsg.totalPay = tableInfo.value.toString().toInt()
                            if (tableInfo.key.toString() == "userId") tempMsg.userId = tableInfo.value.toString()
                        }
                        msgAL.add(tempMsg)
                        if (i == msgKeyAL.size-1){
                            msgLineNum=2
                            getMsgTableByKey()
                        }

                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })
        }
    }



    //키 기반 테이블과 메뉴 정보 받아옴

    public fun getMsgTableByKey(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangId)

        msgTableDataAL.clear()

        for (i in 0..msgKeyAL.size-1){
            ref.child(msgKeyAL[i]).child("tables").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var temptempMsgTable = ArrayList<MsgTableData>()
                    if ( msgLineNum == 2){
                        var tempMsgTable = MsgTableData("", "")
                        for (tableInfo in snapshot.children) {
                            Log.d("확인 getMsgTableByKey()", tableInfo.key.toString())
                            tempMsgTable.floorTable = tableInfo.key.toString()
                            tempMsgTable.menuNameNum = tableInfo.value.toString()
                            temptempMsgTable.add(tempMsgTable)
                        }
                        msgTableDataAL.add(temptempMsgTable)
                        if (i == msgKeyAL.size-1){
                            msgLineNum=3
                            getOtherDataFromUid()
                            setMsgTableInfo()
                            //getMsgTableByKey()
                        }

                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })

        }

    }

    //유저 키 기반 유저 정보 받아옴

    public fun getOtherDataFromUid(){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users")
        for (i in 0..msgKeyAL.size-1){
           userDataAL.clear()
            //msgLineNum = 0

            ref.child(msgAL[i].userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if ( msgLineNum == 3){
                        var tempUserData = OtherUserData("", "")
                        for (tableInfo in snapshot.children) {
                            Log.d("확인 getOtherDataFromUid()", tableInfo.key.toString())
                            Log.d("확인 getOtherDataFromUid()", tableInfo.value.toString())
                            //Log.d("확인 getMsgKeyFromDB()", tableInfo.key.toString())
                            //msgKeyAL.add(tableInfo.key.toString())
                            if(tableInfo.key.toString() == "username") tempUserData.username = tableInfo.value.toString()
                            if(tableInfo.key.toString() == "phone_number") tempUserData.phone_number = tableInfo.value.toString()
                        }
                        userDataAL.add(tempUserData)
                        if (i == msgKeyAL.size-1){
                            msgLineNum=4
                            renewalOrder()
                        }
                        //getMsgByKey()
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })
        }
    }


    public fun setMsgTableInfo(){
        for (i in 0..msgTableDataAL.size-1){
            var tempAL = ArrayList<MsgBookInfo>()
            for (j in 0..msgTableDataAL[i].size-1){
                var floor = msgTableDataAL[i][j].floorTable.slice(IntRange(0, 6))
                Log.d("확인 setMsgTableInfo()", floor)
                var table = msgTableDataAL[i][j].floorTable.slice(IntRange(8, 13))
                Log.d("확인 setMsgTableInfo()", table)
                var menuName =msgTableDataAL[i][j].menuNameNum.slice(IntRange(0, msgTableDataAL[i][j].menuNameNum.length-5))
                Log.d("확인 setMsgTableInfo()", menuName)
                var menucnt =msgTableDataAL[i][j].menuNameNum.slice(IntRange(msgTableDataAL[i][j].menuNameNum.length-1, msgTableDataAL[i][j].menuNameNum.length-1)).toInt()
                Log.d("확인 setMsgTableInfo()", menucnt.toString())
                var tempMsgBookInfo = MsgBookInfo(floor, table, menuName, menucnt)
                tempAL.add(tempMsgBookInfo)
            }
            msgBookInfoDataAL.add(tempAL)

        }

    }




    //메인페이지에 테이블 띄우는 루틴


    //데이터베이스에서 층을 받음
    public fun getTableDataFromDB(){
        Log.d("확인  getTableDataFromDB()", "시작")
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("TableInfo")

        //if(getTableDataLineNum==0)
        floorList.clear()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if ( getTableDataLineNum == 0){
                    for (tableInfo in snapshot.children) {
                        //Log.d("확인  getTableDataFromDB()", "getFromDB : "+tableInfo.key.toString())
                        floorList.add(tableInfo.key.toString())
                    }
                    getTableDataLineNum=1
                    getTableOnFloor()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
            }
        })

    }

    //데이터베이스에서 받은 층 토대로 테이블 가져옴옴
    public fun getTableOnFloor(){
        Log.d("확인 getTableOnFloor()", "시작 : "+getTableDataLineNum.toString())

        /*
        if (getTableDataLineNum == 1){
            tableFromDBDataAL.clear()
            tableNumAL.clear()
        }*/

        tableFromDBDataAL.clear()
        tableNumAL.clear()
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("TableInfo")


        /*
        var ttttt=0
        for (i in 0..10000000){
            ttttt+=1
            ttttt=ttttt*2/ttttt
        }*/

        Log.d("확인  getTableOnFloor()", "층수"+floorList.size)
        for (i in 0..floorList.size-1){
            Log.d("확인  getTableOnFloor()", "테이블별"+ floorList[i])
            ref.child(floorList[i]).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (getTableDataLineNum == 1){
                        var tableNum = 0
                        //Log.d("확인  getTableOnFloor()", " 1")
                        for (tableInfo in snapshot.children) {
                            //Log.d("확인  getTableOnFloor()", "getFromDB : "+snapshot.key.toString())
                            //floorList.add(tableInfo.key.toString())
                            val newsikdangInfo = tableInfo.getValue(TableFromDBData::class.java)
                            //Log.d("확인  getTableOnFloor()", "데이터 가져옴")
                            if(newsikdangInfo!! == null) continue
                            Log.d("확인 getTableOnFloor()", "getFromDB 테이블 추가 : ${newsikdangInfo}")
                            tableFromDBDataAL.add(newsikdangInfo)
                            tableNum +=1
                        }
                        tableNumAL.add(tableNum)
                        Log.d("확인  getTableOnFloor()", "getFromDB : ${tableFromDBDataAL}")
                        if (i ==floorList.size-1){
                            getTableDataLineNum=2
                            setAccum()
                            getTableBookedInfo()
                        }
                        //choiceMySikdangRVAdapter.notifyDataSetChanged()
                        //getTableBookedInfo()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })

        }


    }

    public fun setAccum(){
        Log.d("확인 setAccum()", "시작 : "+getTableDataLineNum.toString())
        accumTableNumList.clear()
        accumTableNumList.add(tableNumAL[0])
        //Log.d("확인 tableAccum", accumTableNumList[i].toString())
        for (i in 0..tableNumAL.size-2){
            accumTableNumList.add(accumTableNumList[i]+tableNumAL[i+1])
            //Log.d("확인 tableAccum", accumTableNumList[i].toString())
        }
        Log.d("확인 setAccum()", "끝 : "+"${accumTableNumList}")

    }

    //예약 현황 가져옴
    public fun getTableBookedInfo(){
        Log.d("확인 getTableBookedInfo()", "시작 : "+getTableDataLineNum.toString())
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("Booked")

        //if (getTableDataLineNum == 2) tableIsBookedAL.clear()
        tableIsBookedAL.clear()
        var floorIt = 0
        //var calTableNum = tableNumAL[0]
        var tempTableNum = 0
        for (i in 0..tableFromDBDataAL.size-1){
            //Log.d("확인  getTableBookedInfo()", "for문 시작"+" table"+(i+1).toString() + floorList[floorIt].toString() + " " +floorIt )
            //ref.child(floorList[floorIt]).child(showTime).child("BookInfo").child(("table"+(i+1).toString())).addValueEventListener(object : ValueEventListener {

            Log.d("확인  getTableBookedInfo() for문 시작", getTableDataLineNum.toString()+"get tableIsBookedAL FromDB : "+floorList[floorIt].toString()+" "+showTime + ("table"+(tempTableNum+1).toString()))
            //Log.d("확인  getTableBookedInfo()", "get tableIsBookedAL FromDB : "+tableBooked.key.toString()+" "+tableBooked.value.toString())
            ref.child(floorList[floorIt]).child(showTime).child(("table"+(tempTableNum+1).toString())).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var tempNum = i
                    Log.d("확인 getTableBookedInfo() 가져오기 전전",i.toString() )
                    if (getTableDataLineNum==2){
                        Log.d("확인 getTableBookedInfo() 가져오기 전",i.toString() )
                        for (tableBooked in snapshot.children) {
                            Log.d("확인  getTableBookedInfo()", "get tableIsBookedAL FromDB : ")
                            //Log.d("확인  getTableBookedInfo()", "get tableIsBookedAL FromDB : "+floorList[floorIt].toString()+" "+showTime)
                            //Log.d("확인  getTableBookedInfo()", "get tableIsBookedAL FromDB : "+tableBooked.key.toString()+" "+tableBooked.value.toString())
                            tableIsBookedAL.add(tableBooked.value.toString().toInt())
                            if (tableBooked == null) {
                                Log.d("확인 getTableBookedInfo() 가져오기 ","비어서 1넣음")
                                tableIsBookedAL.add(1)
                            }
                        }
                        Log.d("확인 getTableBookedInfo() 가져오기 후",i.toString()+" / " +tableFromDBDataAL.size )

                        if(tempNum == tableFromDBDataAL.size-1) {
                            Log.d("확인 getTableBookedInfo() 끝","${tableIsBookedAL}" )
                            getTableDataLineNum=3
                            setTableData()
                            setTimeAL()
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    //Log.d("확인 getTableBookedInfo()", "5 getFromDB : ${error}")
                }
            })
            Log.d("확인 getTableBookedInfo() 가져오기 후 for문 끝",i.toString()+" / " +tableFromDBDataAL.size )
            //ref.child(floorList[floorIt]).child(showTime).child(("table"+(i+1).toString())).child("mutex")


            tempTableNum+=1

            if (i>=accumTableNumList[floorIt]-1) {
                floorIt+=1
                tempTableNum=0
            }
            Log.d("확인 getTableBookedInfo() 가져오기 후 for문 끝후"," " )

        }
    }



    public fun setTableData(){
        Log.d("확인 setTableData()", "시작 : "+getTableDataLineNum.toString())


        tableData.tableList=ArrayList()//각 테이블 정보 담긴 리스트

        //tableData.floorList = ArrayList<Int>()//식당 각 몇층인지 1층과 3층이 있으면 1, 3 의 값을 갖는다.
        tableData.tableNumList = tableNumAL//각 층에 테이블 몇개인지
        //Log.d("확인  setTableData()", "tableNumList : ${tableData.tableNumList}")

        tableData.accumTableNumList = accumTableNumList//테이블 개수 축적
        //Log.d("확인  setTableData()", "accum : ${tableData.accumTableNumList}")


        intFloorIist.clear()
        for (i in 0..floorList.size-1){
            //Log.d("확인  setTableData()", " 1for문 "+floorList[i]+"  "+i.toString())
            intFloorIist.add(floorList[i].slice(IntRange(6, 6)).toInt())
        }

        tableData.floorList=intFloorIist
        //Log.d("확인  setTableData()", "floorList : ${tableData.floorList}")

        //Log.d("확인  setTableData()", "2")

        tableData.tableList.clear()
        for (i in 0..tableFromDBDataAL.size-1){
            var calNum = 0
            var floorIt = 0
            var tempFloor = intFloorIist[floorIt]

            //Log.d("확인  setTableData()", "3")
            for (j in 0..tableNumAL.size-1){
                calNum+=tableNumAL[j]
                if (i<calNum) break
            }
            //Log.d("확인  setTableData()", "3.1")
            var tempIsCircle = true
            if (tableFromDBDataAL[i].shape == "circle") tempIsCircle = true

            //Log.d("확인  setTableData()", "3.2")
            var tempIsBooked = true
            if (tableIsBookedAL[i] == 1) tempIsBooked = true

            //Log.d("확인  setTableData()", "4")

            tableData.tableList.add(Table_res(tableFromDBDataAL[i].x!!, tableFromDBDataAL[i].y!!, tableFromDBDataAL[i].width!!, tableFromDBDataAL[i].height!!,
                    tableFromDBDataAL[i].capacity!!, tempFloor, tempIsBooked, tempIsCircle))

            if (i>=accumTableNumList[floorIt]-1) floorIt+=1
        }
        Log.d("확인  setTableData()", "tableList : ${tableData.tableList}")

        //setTable()
        renewalTable()



    }




    public fun setTable() {
        Log.d("확인 setTable()", "시작 : "+getTableDataLineNum.toString())
        Log.d("확인 setTable()", "불러온 테이블 총 개수 : "+tableFromDBDataAL.size)
        //각 층 들어가는 뷰페이저
        tableFloorVP = findViewById(R.id.tableFloorVP)
        vpAdapter = TableFloorVPAdapter_res(this, this)
        tableFloorVP.adapter = vpAdapter
    }

    public fun setTimeNum(timeNum_: String) {
        timeNum = timeNum_
        selectedTimeTV.setText(timeNum)
        renewalTable()
        nowBtn.isChecked = false
    }

    public fun getTimeNum(): String {
        return timeNum
    }

    public fun renewalTable() {
        Log.d("확인 renewalTable()", "showTime : "+showTime)
        Log.d("확인 renewalTable()", "tableData.tableList : ${tableData.tableList}")
        Log.d("확인 renewalTable()", "tableIsBookedAL : ${tableIsBookedAL}")
        getFloorImageFromDB()
        vpAdapter.notifyDataSetChanged()
    }

    public fun renewalOrder() {
        messageRVAdapter.notifyDataSetChanged()
    }


    private fun showSikdangSettingDialog() {
        Log.d("확인 showSikdangSettingDialog()", "ㅁㅁ")
        var customDialog = SikdangSettingDialog(this, sikdangId, this)
        customDialog!!.show()
    }

    //데이터베이스에서 현재 예약 신청 목록을 불러온다.
    public fun setMessage() {
        messages = ArrayList<MessageData>()
        var tempMenu = MessageMenuData("맥콜", 3)
        var tempMenu2 = MessageMenuData("파인애플피자", 5)
        var tempMenu3 = MessageMenuData("민트초코라떼", 1)
        var tempMenuAL1 = ArrayList<MessageMenuData>()
        tempMenuAL1.add(tempMenu)
        tempMenuAL1.add(tempMenu2)
        tempMenuAL1.add(tempMenu3)

        var tempMenu4 = MessageMenuData("맥콜", 3)
        var tempMenu5 = MessageMenuData("파인애플피자", 5)
        var tempMenuAL2 = ArrayList<MessageMenuData>()
        tempMenuAL2.add(tempMenu4)
        tempMenuAL2.add(tempMenu5)

        var tempMenu6 = MessageMenuData("맥콜", 3)
        var tempMenu7 = MessageMenuData("파인애플피자", 3)
        var tempMenu8 = MessageMenuData("오이피자", 1)
        var tempMenu9 = MessageMenuData("초코치킨", 12)
        var tempMenuAL3 = ArrayList<MessageMenuData>()
        tempMenuAL3.add(tempMenu6)
        tempMenuAL3.add(tempMenu7)
        tempMenuAL3.add(tempMenu8)
        tempMenuAL3.add(tempMenu9)

        var tempTableAL1 = ArrayList<MessageTableData>()
        tempTableAL1.add(MessageTableData(2, 8, tempMenuAL1))
        tempTableAL1.add(MessageTableData(2, 9, tempMenuAL2))
        tempTableAL1.add(MessageTableData(3, 11, tempMenuAL3))


        messages.add(MessageData("임꺽정", "010-1234-5678", 80000, 2021, 5, 21, 9, 34, 10, 9, 30, 10, 30, tempTableAL1, "987654321"))

        var temp2Menu = MessageMenuData("맥콜", 3)
        var temp2Menu2 = MessageMenuData("파인애플피자", 5)
        var temp2Menu3 = MessageMenuData("민트초코라떼", 1)
        var temp2MenuAL1 = ArrayList<MessageMenuData>()
        temp2MenuAL1.add(temp2Menu)
        temp2MenuAL1.add(temp2Menu2)
        temp2MenuAL1.add(temp2Menu3)

        var temp2Menu4 = MessageMenuData("맥콜", 3)
        var temp2Menu5 = MessageMenuData("파인애플피자", 5)
        var temp2MenuAL2 = ArrayList<MessageMenuData>()
        temp2MenuAL2.add(temp2Menu4)
        temp2MenuAL2.add(temp2Menu5)

        var temp2Menu6 = MessageMenuData("맥콜", 3)
        var temp2Menu7 = MessageMenuData("파인애플피자", 3)
        var temp2Menu8 = MessageMenuData("오이피자", 1)
        var temp2Menu9 = MessageMenuData("초코치킨", 12)
        var temp2MenuAL3 = ArrayList<MessageMenuData>()
        temp2MenuAL3.add(temp2Menu6)
        temp2MenuAL3.add(temp2Menu7)
        temp2MenuAL3.add(temp2Menu8)
        temp2MenuAL3.add(temp2Menu9)

        var temp2TableAL1 = ArrayList<MessageTableData>()
        temp2TableAL1.add(MessageTableData(1, 1, temp2MenuAL1))
        temp2TableAL1.add(MessageTableData(1, 3, temp2MenuAL2))
        temp2TableAL1.add(MessageTableData(1, 4, temp2MenuAL3))


        messages.add(MessageData("장길산", "010-1234-5678", 90000, 2021, 5, 21, 9, 34, 10, 9, 30, 10, 30, temp2TableAL1, "123456789"))


    }

    //하나의 예약 신청에서 식당 주인이 확인할 정보
    //conName 소비자 이름 pn 소비자 번호 price 가격 y, m, day, h, min, sec, 예약 수락 or 예약 신청 시간 sh, sm 예약 시작시간 eh em 예약 끝 시간
    //orderId 는 주문 고유번호
    inner class MessageData(
            var conName: String, var pn: String, var price: Int,
            var y: Int, var m: Int, var day: Int, var h: Int, var min: Int, var sec: Int,
            var sh: Int, var sm: Int, var eh: Int, var em: Int, var tables: ArrayList<MessageTableData>, var orderId: String
    )


    //테이블별 메뉴 내용
    inner class MessageTableData(var tableFloor: Int, var tableNum: Int, var menus: ArrayList<MessageMenuData>)

    inner class MessageMenuData(var menuName: String, var menuNum: Int)



    //이미지 받아옴
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == 1&& resultCode == Activity.RESULT_OK) { // 메뉴의 이미지 변경
            if (data == null) return
            sikdangimgCheckNum=1
            newMenuImgUri = data.data
            menuEditDialog.setNewImg()


        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            var result = CropImage.getActivityResult(data)
            newMenuImgUri = result.uri
            Log.d("확인 sikdangMainRes", "메뉴사진 셋 uri : "+newMenuImgUri.toString())
            if(newMenuImgUri == null) return
            menuEditDialog.setNewImg()

            sikdangimgCheckNum=1
        }
        else{

            sikdangimgCheckNum=2
        }/*
        if (requestCode == 2){//테이블 이미지 세팅
            if (resultCode == RESULT_OK) {
                try {
                    val ins: InputStream? = contentResolver.openInputStream(data?.data!!)
                    sikdangimg = BitmapFactory.decodeStream(ins)
                    ins?.close()
                    Log.d("확인 onActivityResult2", sikdangimg.toString())
                    sikdangimgCheckNum=1
                    //saveBitmap(img)
                    //imageView4.setImageBitmap(sikdangimg)
                    changeFloorImageDialog.setNewImg()

                } catch (e: Exception) {
                    Log.d("확인 onActivityResult2.e", sikdangimg.toString())
                    sikdangimgCheckNum=1
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show()
                sikdangimgCheckNum=2
            }
        }*/

        if(requestCode == 2 && resultCode == Activity.RESULT_OK) {//층 평면도
            Log.d("확인 sikdangMainRes", "층 평면도 셋")
            if (data == null) return
            sikdangimgCheckNum=1
            newFloorImageUri = data.data
            changeFloorImageDialog.setNewImage()
            Log.d("확인 sikdangMainRes", "층 평면도 uri : "+newFloorImageUri.toString())
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            sikdangimgCheckNum==1
            var result = CropImage.getActivityResult(data)
            newFloorImageUri = result.uri
            Log.d("확인 sikdangMainRes", "층 평면도 uri : "+newFloorImageUri.toString())
            if(newFloorImageUri == null) return
            changeFloorImageDialog.setNewImage()
        } else  {
            sikdangimgCheckNum==2
            Toast.makeText(this, "죄송합니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }

        if(requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Log.d("확인 sikdangMainRes", "식당사진 셋")
            if (data == null) return
            sikdangimgCheckNum=1
            newSikdangImgUri = data.data
            editSikdangImageDialog.setNewImg()
            Log.d("확인 sikdangMainRes", "식당사진 셋 uri : "+newSikdangImgUri.toString())
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            var result = CropImage.getActivityResult(data)
            sikdangimgCheckNum=1
            newSikdangImgUri = result.uri
            Log.d("확인 sikdangMainRes", "식당사진 셋 uri : "+newSikdangImgUri.toString())
            if(newSikdangImgUri == null) return
            editSikdangImageDialog.setNewImg()
        } else  {
            sikdangimgCheckNum=2
            Toast.makeText(this, "죄송합니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
        }



        /*
        else if (requestCode == 3){
            if (resultCode == RESULT_OK) {
                try {
                    val ins: InputStream? = contentResolver.openInputStream(data?.data!!)
                    sikdangimg = BitmapFactory.decodeStream(ins)
                    ins?.close()
                    Log.d("확인 onActivityResult3", sikdangimg.toString())
                    sikdangimgCheckNum=1
                    //saveBitmap(img)
                    //imageView4.setImageBitmap(sikdangimg)
                    editSikdangImageDialog.setNewImg()

                } catch (e: Exception) {
                    Log.d("확인 onActivityResult3.e", sikdangimg.toString())
                    sikdangimgCheckNum=1
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show()
                sikdangimgCheckNum=2
            }
        }*/

    }





    public fun setTimeAL(){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("Booked").child(floorList[0])

        timeSwitch=true
        timeAL.clear()


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (tableInfo in snapshot.children) {
                    if(timeSwitch == true)timeAL.add(tableInfo.key.toString())
                    //Log.d("확인  getTableDataFromDB()", "getFromDB : "+tableInfo.key.toString())
                }
                Log.d("확인 setTimeAL()", "5 getFromDB : ${timeAL}")
                timeSwitch = false
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 setTimeAL()", "5 getFromDB : ${error}")
            }
        })
    }




    private fun saveBitmap(bitmap: Bitmap): String
    {
        var folderPath = Environment.getExternalStorageDirectory().absolutePath + "/path/"
        Log.d("확인 onActivityResult", Environment.getExternalStorageDirectory().absolutePath.toString() + "/path/")
        var fileName = "comment.jpeg"
        var imagePath = folderPath + fileName
        var folder = File(folderPath)
        if (!folder.isDirectory)
            folder.mkdirs()
        var out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        return imagePath
    }








}