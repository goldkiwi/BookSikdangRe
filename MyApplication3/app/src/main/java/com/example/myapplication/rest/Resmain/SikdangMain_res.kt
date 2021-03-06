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
    var sikdangName = "????????????????????????"
    var sikdangId = "10987654321"
    var sikdangType=""

    var showTime="09:00 ??????"

    var messages = ArrayList<MessageData>()

    var tableData = TableData_res()

    var floorList = ArrayList<String>()  //????????? ?????? ????????? ?????????
    var intFloorIist = ArrayList<Int>()
    var tableFromDBDataAL=ArrayList<TableFromDBData>() // ????????? ?????? ??????
    var tableNumAL=ArrayList<Int>() // ????????? ????????? ?????? ????????????
    var accumTableNumList = ArrayList<Int>()//????????? ?????? ??????
    var tableIsBookedAL = ArrayList<Int>()

    var timeAL = ArrayList<String>()
    var timeSwitch = false

    var newSikdangImgUri : Uri? = null
    public var newMenuImgUri : Uri? = null
    public var newFloorImageUri : Uri? = null

    var getTableDataLineNum : Int = 3

    public var msgKeyAL = ArrayList<String>()
    public var msgAL = ArrayList<MsgData>()
    public var msgTableDataAL = ArrayList<ArrayList<MsgTableData>>() //????????? ???/????????????
    public var msgBookInfoDataAL = ArrayList<ArrayList<MsgBookInfo>>()
    public var userDataAL = ArrayList<OtherUserData>()


    public var msgLineNum = 4




    //?????? ???????????? ????????? ???????????? ?????????
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
        //Log.d("?????? ?????? ?????? ??????", curTime.toString())
        //val myToast = Toast.makeText(this, curTime.toString(), Toast.LENGTH_SHORT).show()

        Log.d("?????? CSikdangMain_res", "4")
        timeNum = timeNum + curTime[0] + curTime[1] + curTime[3] + curTime[4]

        Log.d("?????? CSikdangMain_res", "5")

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

        Log.d("?????? CSikdangMain_res", "6")
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
        Log.d("?????? CSikdangMain_res", "7")
        var sm_infoBtn: Button = findViewById(R.id.sm_infoBtn)
        sm_infoBtn.setOnClickListener {
            val intent = Intent(this, ResInfoActivity::class.java)
            startActivity(intent)
        }

        //?????? ?????? ??????
        var sikdangSettingbtn: Button = findViewById(R.id.sikdangSettingbtn)
        sikdangSettingbtn.setOnClickListener {
            showSikdangSettingDialog()
        }

        //?????? ?????? ??????
        var sm_choiceSikdangBtn: Button = findViewById(R.id.sm_choiceSikdangBtn)
        sm_choiceSikdangBtn.setOnClickListener {
            this.finish()
        }

        var sm_messageTV: Button = findViewById(R.id.sm_messageTV)
        sm_messageTV.setOnClickListener {
            sm_drawerLayout.openDrawer(GravityCompat.END)
        }

        sm_beforeCheckBtn = findViewById(R.id.sm_beforeCheckBtn)
        sm_beforeCheckBtn.text = "????????? ??????"
        sm_beforeCheckBtn.textOn = "????????? ??????"
        sm_beforeCheckBtn.textOff = "????????? ??????"

        sm_afterCheckBtn = findViewById(R.id.sm_afterCheckBtn)
        sm_afterCheckBtn.text = "?????? ??????"
        sm_afterCheckBtn.textOn = "?????? ??????"
        sm_afterCheckBtn.textOff = "?????? ??????"


        sm_messageRV = findViewById(R.id.sm_messageRV)
        messageRVAdapter = ResMainMessageRVAdapter(this, this)
        sm_messageRV.adapter = messageRVAdapter

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        sm_messageRV.layoutManager = layoutManager
        sm_messageRV.setHasFixedSize(true)


        Log.d("?????? CSikdangMain_res", "8")

        val resRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangId)

        //????????? ????????? ???
        resRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (tableInfo in snapshot.children) {
                    //Log.d("??????  getTableDataFromDB()", "getFromDB : "+tableInfo.key.toString())
                    //floorList.add(tableInfo.key.toString())
                }
                getMsgKeyFromDB()
                Toast.makeText(this@SikdangMain_res, " ????????? ?????????????????????.", Toast.LENGTH_LONG).show();

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
                Toast.makeText(this@SikdangMain_res, " ?????????????????? ???????????? ????????????", Toast.LENGTH_LONG).show();
            }
        })



    }

    var backPressed = false
    var pressedTime = System.currentTimeMillis()

    override fun onBackPressed() {
        //super.onBackPressed()
        if (backPressed == false) {
            Toast.makeText(this, " ??? ??? ??? ????????? ???????????????.", Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis()
            backPressed = true
        } else {
            var seconds = (System.currentTimeMillis() - pressedTime).toInt();
            if (seconds > 10000) {
                Log.d("?????? ?????? ??????", seconds.toString())
                Toast.makeText(this, " ??? ??? ??? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                backPressed = true;
            } else {

                //Log.d("?????? ??????@@@@@@@@@@@@@@@@@@@", "1")
                //Toast.makeText(this, " ??????@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@." , Toast.LENGTH_LONG).show();
                super.onBackPressed()
                //finish(); // app ?????? ?????????
            }
        }

    }

    inner class FloorImage(var floor:String, var url:String)
    //??? ?????????

    public var floorUrlAL = ArrayList<FloorImage>()


    //??? ????????? ???????????? ??????

    var imageSwitch = 1
    public fun getFloorImageFromDB(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("floorUrl")
        imageSwitch =0

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (imageSwitch== 0){
                    for (tableInfo in snapshot.children) {
                        Log.d("?????? getMsgKeyFromDB()", tableInfo.key.toString())
                        floorUrlAL.add(FloorImage(tableInfo.key.toString(), tableInfo.value.toString()))
                    }
                    setFloorImage()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
            }
        })
    }

    public fun setFloorImage(){
        //vpAdapter.setImageByUrl()
    }









    //???????????????????????? ????????? ??????
    inner class MsgTableData(var floorTable:String, var menuNameNum:String)
    inner class MsgBookInfo (var floor:String, var table:String, var menu:String, var cnt:Int)

    inner class MsgData(var bookTime:String, var payTime:String, var totalPay:Int, var userId:String)
    inner class OtherUserData(var username:String, var phone_number:String)


    //?????? ????????? ???


    //????????? ??? ?????????

    public fun getMsgKeyFromDB(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangId)

        msgKeyAL.clear()
        msgLineNum = 0

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if ( msgLineNum == 0){
                    for (tableInfo in snapshot.children) {
                        Log.d("?????? getMsgKeyFromDB()", tableInfo.key.toString())
                        msgKeyAL.add(tableInfo.key.toString())
                    }
                    msgLineNum=1
                    getMsgByKey()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
            }
        })
    }


    //??? ?????? ?????? ?????? ?????????
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
                            Log.d("?????? getMsgByKey()", tableInfo.key.toString())
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
                    Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })
        }
    }



    //??? ?????? ???????????? ?????? ?????? ?????????

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
                            Log.d("?????? getMsgTableByKey()", tableInfo.key.toString())
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
                    Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })

        }

    }

    //?????? ??? ?????? ?????? ?????? ?????????

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
                            Log.d("?????? getOtherDataFromUid()", tableInfo.key.toString())
                            Log.d("?????? getOtherDataFromUid()", tableInfo.value.toString())
                            //Log.d("?????? getMsgKeyFromDB()", tableInfo.key.toString())
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
                    Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })
        }
    }


    public fun setMsgTableInfo(){
        for (i in 0..msgTableDataAL.size-1){
            var tempAL = ArrayList<MsgBookInfo>()
            for (j in 0..msgTableDataAL[i].size-1){
                var floor = msgTableDataAL[i][j].floorTable.slice(IntRange(0, 6))
                Log.d("?????? setMsgTableInfo()", floor)
                var table = msgTableDataAL[i][j].floorTable.slice(IntRange(8, 13))
                Log.d("?????? setMsgTableInfo()", table)
                var menuName =msgTableDataAL[i][j].menuNameNum.slice(IntRange(0, msgTableDataAL[i][j].menuNameNum.length-5))
                Log.d("?????? setMsgTableInfo()", menuName)
                var menucnt =msgTableDataAL[i][j].menuNameNum.slice(IntRange(msgTableDataAL[i][j].menuNameNum.length-1, msgTableDataAL[i][j].menuNameNum.length-1)).toInt()
                Log.d("?????? setMsgTableInfo()", menucnt.toString())
                var tempMsgBookInfo = MsgBookInfo(floor, table, menuName, menucnt)
                tempAL.add(tempMsgBookInfo)
            }
            msgBookInfoDataAL.add(tempAL)

        }

    }




    //?????????????????? ????????? ????????? ??????


    //???????????????????????? ?????? ??????
    public fun getTableDataFromDB(){
        Log.d("??????  getTableDataFromDB()", "??????")
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("TableInfo")

        //if(getTableDataLineNum==0)
        floorList.clear()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if ( getTableDataLineNum == 0){
                    for (tableInfo in snapshot.children) {
                        //Log.d("??????  getTableDataFromDB()", "getFromDB : "+tableInfo.key.toString())
                        floorList.add(tableInfo.key.toString())
                    }
                    getTableDataLineNum=1
                    getTableOnFloor()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
            }
        })

    }

    //???????????????????????? ?????? ??? ????????? ????????? ????????????
    public fun getTableOnFloor(){
        Log.d("?????? getTableOnFloor()", "?????? : "+getTableDataLineNum.toString())

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

        Log.d("??????  getTableOnFloor()", "??????"+floorList.size)
        for (i in 0..floorList.size-1){
            Log.d("??????  getTableOnFloor()", "????????????"+ floorList[i])
            ref.child(floorList[i]).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (getTableDataLineNum == 1){
                        var tableNum = 0
                        //Log.d("??????  getTableOnFloor()", " 1")
                        for (tableInfo in snapshot.children) {
                            //Log.d("??????  getTableOnFloor()", "getFromDB : "+snapshot.key.toString())
                            //floorList.add(tableInfo.key.toString())
                            val newsikdangInfo = tableInfo.getValue(TableFromDBData::class.java)
                            //Log.d("??????  getTableOnFloor()", "????????? ?????????")
                            if(newsikdangInfo!! == null) continue
                            Log.d("?????? getTableOnFloor()", "getFromDB ????????? ?????? : ${newsikdangInfo}")
                            tableFromDBDataAL.add(newsikdangInfo)
                            tableNum +=1
                        }
                        tableNumAL.add(tableNum)
                        Log.d("??????  getTableOnFloor()", "getFromDB : ${tableFromDBDataAL}")
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
                    Log.d("?????? setSikdangListInfo()", "5 getFromDB : ${error}")
                }
            })

        }


    }

    public fun setAccum(){
        Log.d("?????? setAccum()", "?????? : "+getTableDataLineNum.toString())
        accumTableNumList.clear()
        accumTableNumList.add(tableNumAL[0])
        //Log.d("?????? tableAccum", accumTableNumList[i].toString())
        for (i in 0..tableNumAL.size-2){
            accumTableNumList.add(accumTableNumList[i]+tableNumAL[i+1])
            //Log.d("?????? tableAccum", accumTableNumList[i].toString())
        }
        Log.d("?????? setAccum()", "??? : "+"${accumTableNumList}")

    }

    //?????? ?????? ?????????
    public fun getTableBookedInfo(){
        Log.d("?????? getTableBookedInfo()", "?????? : "+getTableDataLineNum.toString())
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangId).child("Booked")

        //if (getTableDataLineNum == 2) tableIsBookedAL.clear()
        tableIsBookedAL.clear()
        var floorIt = 0
        //var calTableNum = tableNumAL[0]
        var tempTableNum = 0
        for (i in 0..tableFromDBDataAL.size-1){
            //Log.d("??????  getTableBookedInfo()", "for??? ??????"+" table"+(i+1).toString() + floorList[floorIt].toString() + " " +floorIt )
            //ref.child(floorList[floorIt]).child(showTime).child("BookInfo").child(("table"+(i+1).toString())).addValueEventListener(object : ValueEventListener {

            Log.d("??????  getTableBookedInfo() for??? ??????", getTableDataLineNum.toString()+"get tableIsBookedAL FromDB : "+floorList[floorIt].toString()+" "+showTime + ("table"+(tempTableNum+1).toString()))
            //Log.d("??????  getTableBookedInfo()", "get tableIsBookedAL FromDB : "+tableBooked.key.toString()+" "+tableBooked.value.toString())
            ref.child(floorList[floorIt]).child(showTime).child(("table"+(tempTableNum+1).toString())).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var tempNum = i
                    Log.d("?????? getTableBookedInfo() ???????????? ??????",i.toString() )
                    if (getTableDataLineNum==2){
                        Log.d("?????? getTableBookedInfo() ???????????? ???",i.toString() )
                        for (tableBooked in snapshot.children) {
                            Log.d("??????  getTableBookedInfo()", "get tableIsBookedAL FromDB : ")
                            //Log.d("??????  getTableBookedInfo()", "get tableIsBookedAL FromDB : "+floorList[floorIt].toString()+" "+showTime)
                            //Log.d("??????  getTableBookedInfo()", "get tableIsBookedAL FromDB : "+tableBooked.key.toString()+" "+tableBooked.value.toString())
                            tableIsBookedAL.add(tableBooked.value.toString().toInt())
                            if (tableBooked == null) {
                                Log.d("?????? getTableBookedInfo() ???????????? ","????????? 1??????")
                                tableIsBookedAL.add(1)
                            }
                        }
                        Log.d("?????? getTableBookedInfo() ???????????? ???",i.toString()+" / " +tableFromDBDataAL.size )

                        if(tempNum == tableFromDBDataAL.size-1) {
                            Log.d("?????? getTableBookedInfo() ???","${tableIsBookedAL}" )
                            getTableDataLineNum=3
                            setTableData()
                            setTimeAL()
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    //Log.d("?????? getTableBookedInfo()", "5 getFromDB : ${error}")
                }
            })
            Log.d("?????? getTableBookedInfo() ???????????? ??? for??? ???",i.toString()+" / " +tableFromDBDataAL.size )
            //ref.child(floorList[floorIt]).child(showTime).child(("table"+(i+1).toString())).child("mutex")


            tempTableNum+=1

            if (i>=accumTableNumList[floorIt]-1) {
                floorIt+=1
                tempTableNum=0
            }
            Log.d("?????? getTableBookedInfo() ???????????? ??? for??? ??????"," " )

        }
    }



    public fun setTableData(){
        Log.d("?????? setTableData()", "?????? : "+getTableDataLineNum.toString())


        tableData.tableList=ArrayList()//??? ????????? ?????? ?????? ?????????

        //tableData.floorList = ArrayList<Int>()//?????? ??? ???????????? 1?????? 3?????? ????????? 1, 3 ??? ?????? ?????????.
        tableData.tableNumList = tableNumAL//??? ?????? ????????? ????????????
        //Log.d("??????  setTableData()", "tableNumList : ${tableData.tableNumList}")

        tableData.accumTableNumList = accumTableNumList//????????? ?????? ??????
        //Log.d("??????  setTableData()", "accum : ${tableData.accumTableNumList}")


        intFloorIist.clear()
        for (i in 0..floorList.size-1){
            //Log.d("??????  setTableData()", " 1for??? "+floorList[i]+"  "+i.toString())
            intFloorIist.add(floorList[i].slice(IntRange(6, 6)).toInt())
        }

        tableData.floorList=intFloorIist
        //Log.d("??????  setTableData()", "floorList : ${tableData.floorList}")

        //Log.d("??????  setTableData()", "2")

        tableData.tableList.clear()
        for (i in 0..tableFromDBDataAL.size-1){
            var calNum = 0
            var floorIt = 0
            var tempFloor = intFloorIist[floorIt]

            //Log.d("??????  setTableData()", "3")
            for (j in 0..tableNumAL.size-1){
                calNum+=tableNumAL[j]
                if (i<calNum) break
            }
            //Log.d("??????  setTableData()", "3.1")
            var tempIsCircle = true
            if (tableFromDBDataAL[i].shape == "circle") tempIsCircle = true

            //Log.d("??????  setTableData()", "3.2")
            var tempIsBooked = true
            if (tableIsBookedAL[i] == 1) tempIsBooked = true

            //Log.d("??????  setTableData()", "4")

            tableData.tableList.add(Table_res(tableFromDBDataAL[i].x!!, tableFromDBDataAL[i].y!!, tableFromDBDataAL[i].width!!, tableFromDBDataAL[i].height!!,
                    tableFromDBDataAL[i].capacity!!, tempFloor, tempIsBooked, tempIsCircle))

            if (i>=accumTableNumList[floorIt]-1) floorIt+=1
        }
        Log.d("??????  setTableData()", "tableList : ${tableData.tableList}")

        //setTable()
        renewalTable()



    }




    public fun setTable() {
        Log.d("?????? setTable()", "?????? : "+getTableDataLineNum.toString())
        Log.d("?????? setTable()", "????????? ????????? ??? ?????? : "+tableFromDBDataAL.size)
        //??? ??? ???????????? ????????????
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
        Log.d("?????? renewalTable()", "showTime : "+showTime)
        Log.d("?????? renewalTable()", "tableData.tableList : ${tableData.tableList}")
        Log.d("?????? renewalTable()", "tableIsBookedAL : ${tableIsBookedAL}")
        getFloorImageFromDB()
        vpAdapter.notifyDataSetChanged()
    }

    public fun renewalOrder() {
        messageRVAdapter.notifyDataSetChanged()
    }


    private fun showSikdangSettingDialog() {
        Log.d("?????? showSikdangSettingDialog()", "??????")
        var customDialog = SikdangSettingDialog(this, sikdangId, this)
        customDialog!!.show()
    }

    //???????????????????????? ?????? ?????? ?????? ????????? ????????????.
    public fun setMessage() {
        messages = ArrayList<MessageData>()
        var tempMenu = MessageMenuData("??????", 3)
        var tempMenu2 = MessageMenuData("??????????????????", 5)
        var tempMenu3 = MessageMenuData("??????????????????", 1)
        var tempMenuAL1 = ArrayList<MessageMenuData>()
        tempMenuAL1.add(tempMenu)
        tempMenuAL1.add(tempMenu2)
        tempMenuAL1.add(tempMenu3)

        var tempMenu4 = MessageMenuData("??????", 3)
        var tempMenu5 = MessageMenuData("??????????????????", 5)
        var tempMenuAL2 = ArrayList<MessageMenuData>()
        tempMenuAL2.add(tempMenu4)
        tempMenuAL2.add(tempMenu5)

        var tempMenu6 = MessageMenuData("??????", 3)
        var tempMenu7 = MessageMenuData("??????????????????", 3)
        var tempMenu8 = MessageMenuData("????????????", 1)
        var tempMenu9 = MessageMenuData("????????????", 12)
        var tempMenuAL3 = ArrayList<MessageMenuData>()
        tempMenuAL3.add(tempMenu6)
        tempMenuAL3.add(tempMenu7)
        tempMenuAL3.add(tempMenu8)
        tempMenuAL3.add(tempMenu9)

        var tempTableAL1 = ArrayList<MessageTableData>()
        tempTableAL1.add(MessageTableData(2, 8, tempMenuAL1))
        tempTableAL1.add(MessageTableData(2, 9, tempMenuAL2))
        tempTableAL1.add(MessageTableData(3, 11, tempMenuAL3))


        messages.add(MessageData("?????????", "010-1234-5678", 80000, 2021, 5, 21, 9, 34, 10, 9, 30, 10, 30, tempTableAL1, "987654321"))

        var temp2Menu = MessageMenuData("??????", 3)
        var temp2Menu2 = MessageMenuData("??????????????????", 5)
        var temp2Menu3 = MessageMenuData("??????????????????", 1)
        var temp2MenuAL1 = ArrayList<MessageMenuData>()
        temp2MenuAL1.add(temp2Menu)
        temp2MenuAL1.add(temp2Menu2)
        temp2MenuAL1.add(temp2Menu3)

        var temp2Menu4 = MessageMenuData("??????", 3)
        var temp2Menu5 = MessageMenuData("??????????????????", 5)
        var temp2MenuAL2 = ArrayList<MessageMenuData>()
        temp2MenuAL2.add(temp2Menu4)
        temp2MenuAL2.add(temp2Menu5)

        var temp2Menu6 = MessageMenuData("??????", 3)
        var temp2Menu7 = MessageMenuData("??????????????????", 3)
        var temp2Menu8 = MessageMenuData("????????????", 1)
        var temp2Menu9 = MessageMenuData("????????????", 12)
        var temp2MenuAL3 = ArrayList<MessageMenuData>()
        temp2MenuAL3.add(temp2Menu6)
        temp2MenuAL3.add(temp2Menu7)
        temp2MenuAL3.add(temp2Menu8)
        temp2MenuAL3.add(temp2Menu9)

        var temp2TableAL1 = ArrayList<MessageTableData>()
        temp2TableAL1.add(MessageTableData(1, 1, temp2MenuAL1))
        temp2TableAL1.add(MessageTableData(1, 3, temp2MenuAL2))
        temp2TableAL1.add(MessageTableData(1, 4, temp2MenuAL3))


        messages.add(MessageData("?????????", "010-1234-5678", 90000, 2021, 5, 21, 9, 34, 10, 9, 30, 10, 30, temp2TableAL1, "123456789"))


    }

    //????????? ?????? ???????????? ?????? ????????? ????????? ??????
    //conName ????????? ?????? pn ????????? ?????? price ?????? y, m, day, h, min, sec, ?????? ?????? or ?????? ?????? ?????? sh, sm ?????? ???????????? eh em ?????? ??? ??????
    //orderId ??? ?????? ????????????
    inner class MessageData(
            var conName: String, var pn: String, var price: Int,
            var y: Int, var m: Int, var day: Int, var h: Int, var min: Int, var sec: Int,
            var sh: Int, var sm: Int, var eh: Int, var em: Int, var tables: ArrayList<MessageTableData>, var orderId: String
    )


    //???????????? ?????? ??????
    inner class MessageTableData(var tableFloor: Int, var tableNum: Int, var menus: ArrayList<MessageMenuData>)

    inner class MessageMenuData(var menuName: String, var menuNum: Int)



    //????????? ?????????
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == 1&& resultCode == Activity.RESULT_OK) { // ????????? ????????? ??????
            if (data == null) return
            sikdangimgCheckNum=1
            newMenuImgUri = data.data
            menuEditDialog.setNewImg()


        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            var result = CropImage.getActivityResult(data)
            newMenuImgUri = result.uri
            Log.d("?????? sikdangMainRes", "???????????? ??? uri : "+newMenuImgUri.toString())
            if(newMenuImgUri == null) return
            menuEditDialog.setNewImg()

            sikdangimgCheckNum=1
        }
        else{

            sikdangimgCheckNum=2
        }/*
        if (requestCode == 2){//????????? ????????? ??????
            if (resultCode == RESULT_OK) {
                try {
                    val ins: InputStream? = contentResolver.openInputStream(data?.data!!)
                    sikdangimg = BitmapFactory.decodeStream(ins)
                    ins?.close()
                    Log.d("?????? onActivityResult2", sikdangimg.toString())
                    sikdangimgCheckNum=1
                    //saveBitmap(img)
                    //imageView4.setImageBitmap(sikdangimg)
                    changeFloorImageDialog.setNewImg()

                } catch (e: Exception) {
                    Log.d("?????? onActivityResult2.e", sikdangimg.toString())
                    sikdangimgCheckNum=1
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show()
                sikdangimgCheckNum=2
            }
        }*/

        if(requestCode == 2 && resultCode == Activity.RESULT_OK) {//??? ?????????
            Log.d("?????? sikdangMainRes", "??? ????????? ???")
            if (data == null) return
            sikdangimgCheckNum=1
            newFloorImageUri = data.data
            changeFloorImageDialog.setNewImage()
            Log.d("?????? sikdangMainRes", "??? ????????? uri : "+newFloorImageUri.toString())
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            sikdangimgCheckNum==1
            var result = CropImage.getActivityResult(data)
            newFloorImageUri = result.uri
            Log.d("?????? sikdangMainRes", "??? ????????? uri : "+newFloorImageUri.toString())
            if(newFloorImageUri == null) return
            changeFloorImageDialog.setNewImage()
        } else  {
            sikdangimgCheckNum==2
            Toast.makeText(this, "???????????????. ?????? ??????????????????", Toast.LENGTH_SHORT).show()
        }

        if(requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Log.d("?????? sikdangMainRes", "???????????? ???")
            if (data == null) return
            sikdangimgCheckNum=1
            newSikdangImgUri = data.data
            editSikdangImageDialog.setNewImg()
            Log.d("?????? sikdangMainRes", "???????????? ??? uri : "+newSikdangImgUri.toString())
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            var result = CropImage.getActivityResult(data)
            sikdangimgCheckNum=1
            newSikdangImgUri = result.uri
            Log.d("?????? sikdangMainRes", "???????????? ??? uri : "+newSikdangImgUri.toString())
            if(newSikdangImgUri == null) return
            editSikdangImageDialog.setNewImg()
        } else  {
            sikdangimgCheckNum=2
            Toast.makeText(this, "???????????????. ?????? ??????????????????", Toast.LENGTH_SHORT).show()
        }



        /*
        else if (requestCode == 3){
            if (resultCode == RESULT_OK) {
                try {
                    val ins: InputStream? = contentResolver.openInputStream(data?.data!!)
                    sikdangimg = BitmapFactory.decodeStream(ins)
                    ins?.close()
                    Log.d("?????? onActivityResult3", sikdangimg.toString())
                    sikdangimgCheckNum=1
                    //saveBitmap(img)
                    //imageView4.setImageBitmap(sikdangimg)
                    editSikdangImageDialog.setNewImg()

                } catch (e: Exception) {
                    Log.d("?????? onActivityResult3.e", sikdangimg.toString())
                    sikdangimgCheckNum=1
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show()
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
                    //Log.d("??????  getTableDataFromDB()", "getFromDB : "+tableInfo.key.toString())
                }
                Log.d("?????? setTimeAL()", "5 getFromDB : ${timeAL}")
                timeSwitch = false
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("?????? setTimeAL()", "5 getFromDB : ${error}")
            }
        })
    }




    private fun saveBitmap(bitmap: Bitmap): String
    {
        var folderPath = Environment.getExternalStorageDirectory().absolutePath + "/path/"
        Log.d("?????? onActivityResult", Environment.getExternalStorageDirectory().absolutePath.toString() + "/path/")
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