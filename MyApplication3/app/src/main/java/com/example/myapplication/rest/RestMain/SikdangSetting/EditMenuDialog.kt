package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication._Ingredient
import com.example.myapplication.bookActivity.MenuData
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.google.firebase.database.*

//SikdangSettingDialog 에서 사용
//메뉴 리스트 보여주고 거기서 하나 선택해서 그 메뉴 수정하는 다이얼로그
class EditMenuDialog (context: Context, val sikdangNum:String, var sikdangmainRes: SikdangMain_res): Dialog(context) {

    var menuDataAL = ArrayList<MenuData>()
    var menuKeyAL = ArrayList<String>()
    var ingKeyAL = ArrayList<ArrayList<String>>()
    var ingAL = ArrayList<ArrayList<_Ingredient>>()
    lateinit var menuRV : RecyclerView
    lateinit var RVAdapter: EditMenuRVAdapter

    var deleteIngAL = ArrayList<String>()

    //현재 진행중인 가져오기 무엇인지 나타냄
    var lineNum = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_editmenu_dialog)

        menuRV = findViewById(R.id.em_rv)
        RVAdapter = EditMenuRVAdapter(context, sikdangNum, sikdangmainRes, this)
        menuRV.adapter = RVAdapter


        var LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        menuRV.layoutManager=LM
        menuRV.setHasFixedSize(true)

        var em_addMenuBtn:Button = findViewById(R.id.em_addMenuBtn)
        em_addMenuBtn.setOnClickListener {
            showAddMenuDialog()
        }

        var emCloseBtn: Button = findViewById(R.id.emCloseBtn)
        emCloseBtn.setOnClickListener { this.dismiss() }

        getMenuDataOnDB()
    }

    public fun showAddMenuDialog(){
        var customDialog = AddMenuDialog(context, sikdangNum, this, sikdangmainRes )
        customDialog!!.show()
    }


    //메뉴 키 가져오고, 재료 키 가져오고, 재료 가져오고, 메뉴 가져오고 메뉴 넣는다.0 1 2 3


    //메뉴들의 키 가져온다.
    public fun getMenuDataOnDB(){
        Log.d("확인  getMenuDataOnDB() 1  menuKeyAL 사이즈 확인", menuKeyAL.size.toString())

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("menu")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (lineNum == 0){
                    Log.d("확인  getMenuDataOnDB()", "ref.addValueEventListener")
                    menuKeyAL.clear()
                    for (menuData in snapshot.children) {
                        //Log.d("확인  getMenuDataOnDB()", "3")
                        //Log.d("확인  getMenuDataOnDB()", menuData.key.toString())
                        //tableIsBookedAL.add(menuData.value.toString().toInt())
                        menuKeyAL.add(menuData.key.toString())
                    }
                    Log.d("확인  getMenuDataOnDB()", "4")
                    lineNum = 1
                    getIngKey()
                    //setTableData()
                    //getMenuDataByMenuId()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 getMenuDataOnDB()", "5 getFromDB : ${error}")
            }
        })

    }


    //가져온 메뉴 키 기반으로 메뉴 데이터 가져온다.
    /*
    public fun getMenuDataByMenuId(){

        for (i in 0..menuKeyAL.size-1){
            val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("menu").child(menuKeyAL[i])
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("확인  getMenuDataByMenuId()", "2")
                    var product:String = ""
                    var image_url : String = ""
                    var price : Int = 0
                    var product_exp: String = ""
                    var ingredients : ArrayList<_Ingredient> = ArrayList()
                    for (menuData in snapshot.children) {
                        if(menuData.key.toString() == "product") product=menuData.value.toString()
                        if(menuData.key.toString() == "image_url") image_url=menuData.value.toString()
                        if(menuData.key.toString() == "price") price=menuData.value.toString().toInt()
                        if(menuData.key.toString() == "product_exp") product_exp=menuData.value.toString()
                        //if(menuData.key.toString() == "ingredients") ingredients=menuData.value.
                        //menuKeyAL.add(menuData.key.toString())
                    }
                    //Log.d("확인  getMenuDataByMenuId()", product.toString())
                    //Log.d("확인  getMenuDataByMenuId()", "4")
                    if(i == menuKeyAL.size-1){
                        ref.child("ingredients").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot2: DataSnapshot) {
                                ingKeyAL.clear()
                                for (menuData2 in snapshot2.children) {
                                    ingKeyAL.add(menuData2.key.toString())
                                    Log.d("확인 2getMenuDataByMenuId()", "내용확인 : ${ingredients}")
                                }
                                for (k in 0..ingKeyAL.size-1){
                                    //menuDataAL.clear()
                                    ref.child("ingredients").child(ingKeyAL[k]).addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot3: DataSnapshot) {
                                            var tempIng = ""
                                            var tempCon = ""
                                            for (menuData3 in snapshot3.children) {
                                                if (menuData3.key.toString() == "country") tempCon=menuData3.value.toString()
                                                if (menuData3.key.toString() == "ing") tempIng=menuData3.value.toString()
                                            }
                                            ingredients.add(_Ingredient(tempIng, tempCon))
                                            //Log.d("확인 2getMenuDataByMenuId()", "내용확인 : ${ingredients}")
                                            //여기서 한번에 MenuData 하나 만들어서 ArrayList에 넣는다.
                                            var tempMenuData = MenuData(product, image_url, price, product_exp, ingredients)
                                            if((i == menuKeyAL.size-1)&&(k == ingKeyAL.size-1)){ //재료 다 불러오고 마지막에 추가 안그러면 반복수만큼 추가
                                                menuDataAL.add(tempMenuData)
                                                Log.d("확인 getMenuDataByMenuId()", "불러오기 완료")
                                                renewalMenus()
                                            }

                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            Log.d("확인 2getMenuDataByMenuId()", "5 getFromDB : ${error}")
                                        }
                                    })

                                }




                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.d("확인 2getMenuDataByMenuId()", "5 getFromDB : ${error}")
                            }
                        })
                    }



                    //setTableData()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 getMenuDataByMenuId()", "5 getFromDB : ${error}")
                }
            })
        }

    }*/





    //재료 키 가져옴
    public fun getIngKey(){
        Log.d("확인  getIngKey() 1  menuKeyAL 사이즈 확인", menuKeyAL.size.toString())
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("menu")
        if(lineNum==1) ingKeyAL.clear()
        for (i in 0..menuKeyAL.size-1){
            ref.child(menuKeyAL[i]).child("ingredients").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot2: DataSnapshot) {
                    if (lineNum == 1){
                        var tempingKeyAL=ArrayList<String>()
                        for (menuData2 in snapshot2.children) {
                            Log.d("확인 EditMenuDialog.getIngKey", "재료 키 추가")
                            tempingKeyAL.add(menuData2.key.toString())
                            //Log.d("확인 2getMenuDataByMenuId()", "내용확인 : ${ingredients}")
                        }
                        Log.d("확인 EditMenuDialog.getIngKey", "재료 키 배열 추가 : "+ingKeyAL.size.toString()+ " " + tempingKeyAL.size.toString())
                        ingKeyAL.add(tempingKeyAL)
                        Log.d("확인 EditMenuDialog.getIngKey", "재료 키 배열 추가 : "+ingKeyAL.size.toString()+ " " + tempingKeyAL.size.toString())

                        if(i == menuKeyAL.size-1){
                            lineNum = 2
                            getIngByKey()
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 getIngKey()", "5 getFromDB : ${error}")
                }
            })
        }

    }

    //키 기반으로 재료 가져온다
    public fun getIngByKey(){
        Log.d("확인  getIngByKey() 1  menuKeyAL 사이즈 확인", menuKeyAL.size.toString()+"lineNun:"+lineNum.toString())
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("menu")
        for (i in 0..ingKeyAL.size-1){
            var tempIngAL = ArrayList<_Ingredient>()
            //Log.d("확인  getIngByKey() 내부수행", "isSetted = false")
            var isSetted = false
            if(lineNum == 2) ingAL.clear()
            for (j in 0..ingKeyAL[i].size-1){
                //Log.d("확인  getIngByKey() 내부수행", "안쪽 for문")
                ref.child(menuKeyAL[i]).child("ingredients").child(ingKeyAL[i][j]).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot3: DataSnapshot) {
                        //Log.d("확인  getIngByKey() 내부수행", "lineNum : "+lineNum.toString())
                        if (lineNum == 2){
                            //Log.d("확인  getIngByKey() 내부수행", j.toString())
                            isSetted = true
                            var tempIng = ""
                            var tempCon = ""

                            for (menuData3 in snapshot3.children) {
                                //Log.d("확인  getIngByKey() 내부수행 삽입 ", j.toString())
                                if (menuData3.key.toString() == "country") tempCon=menuData3.value.toString()
                                if (menuData3.key.toString() == "ing") tempIng=menuData3.value.toString()
                            }
                            tempIngAL.add(_Ingredient(tempIng, tempCon))
                            //Log.d("확인  getIngByKey() 내부수행 2 ", j.toString())
                            if(j==ingKeyAL[i].size-1) ingAL.add(tempIngAL)

                            if((lineNum==2)&&(i == ingKeyAL.size-1)&&(j==ingKeyAL[i].size-1)) {
                                //Log.d("확인  getIngByKey() 내부수행 3 ", j.toString())
                                lineNum = 3
                                getMenuByMenuKey()
                            }

                        }

                        //Log.d("확인 2getMenuDataByMenuId()", "내용확인 : ${ingredients}")
                        //여기서 한번에 MenuData 하나 만들어서 ArrayList에 넣는다.
                        //var tempMenuData = MenuData(product, image_url, price, product_exp, ingredients)
                        /*
                        if((i == menuKeyAL.size-1)&&(k == ingKeyAL.size-1)){ //재료 다 불러오고 마지막에 추가 안그러면 반복수만큼 추가

                            menuDataAL.add(tempMenuData)
                            Log.d("확인 getMenuDataByMenuId()", "불러오기 완료")
                            renewalMenus()
                        }*/

                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("확인 getIngByKey()", "5 getFromDB : ${error}")
                    }
                })
            }
            Log.d("확인  getIngByKey() 내부수행", "for문 아래")




        }

    }

    //메뉴 가져옴
    public fun getMenuByMenuKey(){
        Log.d("확인  getMenuByMenuKey() 1  menuKeyAL 사이즈 확인", menuKeyAL.size.toString())
        if (lineNum == 3) menuDataAL.clear()
        for (i in 0..menuKeyAL.size-1){
            val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("menu").child(menuKeyAL[i])
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(lineNum ==3){

                        Log.d("확인  getMenuByMenuKey()", "2")
                        var product:String = ""
                        var image_url : String = ""
                        var price : Int = 0
                        var product_exp: String = ""
                        //var ingredients : ArrayList<_Ingredient> = ArrayList()
                        Log.d("확인  getMenuByMenuKey()", "3")
                        for (menuData in snapshot.children) {
                            //Log.d("확인  getMenuByMenuKey()", "4")
                            if(menuData.key.toString() == "product") product=menuData.value.toString()
                            if(menuData.key.toString() == "image") image_url=menuData.value.toString()
                            if(menuData.key.toString() == "price") price=menuData.value.toString().toInt()
                            if(menuData.key.toString() == "product_exp") product_exp=menuData.value.toString()
                            //if(menuData.key.toString() == "ingredients") ingredients=menuData.value.
                            //menuKeyAL.add(menuData.key.toString())
                        }
                        Log.d("확인  getMenuByMenuKey()", "5"+i.toString()+"/"+ingAL.size.toString() + "ingKeyAL 크기 : "+ingKeyAL.size.toString())

                        var tempMenuData = MenuData(product, image_url, price, product_exp, ingAL[i])
                        Log.d("확인  getMenuByMenuKey()", "메뉴 추가")
                        menuDataAL.add(tempMenuData)
                        if(i==menuKeyAL.size-1) {
                            //Log.d("확인  getMenuByMenuKey()", "7")
                            lineNum = 0
                            renewalMenus()
                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 getMenuByMenuKey()", "5 getFromDB : ${error}")
                }
            })
        }
    }



    public fun renewalMenus() {
        RVAdapter.notifyDataSetChanged()
    }


}