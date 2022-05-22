package com.example.myapplication.rest.AddRest

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.ChoiceSikdangPage_res
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_notification.*


//ChoiceSikdangPage에서 사용
class AddRestDialog(context: Context, var choicesikdangpageRes: ChoiceSikdangPage_res): Dialog(context) {
    lateinit var ar_sikdangNameRT:EditText
    lateinit var ar_addressET:EditText
    lateinit var ar_detailAddressET:EditText
    lateinit var ar_pnET:EditText
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = auth.currentUser
    val userid = firebaseUser!!.uid
    val reference = FirebaseDatabase.getInstance().getReference()
    .child("Users")
    .child(userid)

    var newRes = hashMapOf<String, Any>()
    var newResLoc = hashMapOf<String, Any>()

    var catAL:ArrayList<String>  = arrayListOf("돼지고기", "닭고기", "한식", "중식", "일식*회", "아시안*양식", "면", "분식", "포차", "디저트", "프랜차이즈")

    var toggleBtnAL = ArrayList<ToggleButton>()

    init{
        //catAL.add("돼지고기")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_addrest_dialog)

        choicesikdangpageRes.addRestDialog=this

        ar_sikdangNameRT=findViewById(R.id.ar_sikdangNameRT)
        ar_addressET=findViewById(R.id.ar_addressET)
        ar_detailAddressET=findViewById(R.id.ar_detailAddressET)
        ar_pnET=findViewById(R.id.ar_pnET)

        var ar_AddSikdangBtn: Button = findViewById(R.id.ar_AddSikdangBtn)
        ar_AddSikdangBtn.setOnClickListener {
            addRest()
            this.dismiss()
        }

        var ar_catRV : RecyclerView = findViewById(R.id.ar_catRV)
        var RVAdapter = RVAdapter(context)
        ar_catRV.adapter = RVAdapter


        var LM = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        ar_catRV.layoutManager=LM
        ar_catRV.setHasFixedSize(true)

        var ar_cancelBtn:Button = findViewById(R.id.ar_cancelBtn)
        ar_cancelBtn.setOnClickListener { this.dismiss() }
    }

    //데이터 베이스 접속
    public fun addRest(){
        //아래 세개 데이터 베이스로 넘김
        //ar_sikdangNameRT.text.toString()
        //ar_addressET.text.toString()
        //ar_detailAddressET.text.toString()

        var cat =getCat()
        if (cat == 999){

        }
        else{
            /*
            val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Restaurants")

            val pushedPostRef: DatabaseReference = ref.push()
            val postId = pushedPostRef.key*/

                /*
            newRes = hashMapOf<String, Any>(
                    "phone_number" to ar_pnET.text.toString(),
                    "store_id" to postId!!,
                    "store_name" to ar_sikdangNameRT.text.toString(),
                    "Store_image" to "Null",
                    "store_type" to catAL[cat]
            )

            newResLoc = hashMapOf<String, Any>(
                    "Lat" to 37.535879,
                    "Lang" to 126.824997,
                    "id" to postId!!,
                    "Name" to ar_sikdangNameRT.text.toString()
            )*/






            choicesikdangpageRes.setNewSikdang()
        }





        //식당에 이름과 주소만 주고 나머지 비운 채로 생성
        //상세 정보는 식당 수정 페이지에서 직접 등록하는 방향으로 진행
        //0층 00 위치에 크기 30테이블 생성한다거나 하는 식으로 심시데이터 하나씩만 넣음
    }

    public fun getCat():Int{
        var result = 999
        for (i in 0..toggleBtnAL.size-1){
            if (toggleBtnAL[i].isChecked) result = i
        }
        return result
    }

    inner class RVAdapter(var context: Context): RecyclerView.Adapter<RVAdapter.Holder>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(context).inflate(R.layout.generalitem_smalltogglebutton, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int {
            return 11
        }

        inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
            public fun bind(pos: Int) {
                var button = itemView.findViewById<ToggleButton>(R.id.gSmallToggleBtn)
                if(pos == 0) {
                    button.setText("돼지고기")
                    button.setTextOn("돼지고기")
                    button.setTextOff("돼지고기")
                }
                if(pos == 1) {
                    button.setText("닭고기")
                    button.setTextOn("닭고기")
                    button.setTextOff("닭고기")
                }
                if(pos == 2) {
                    button.setText("한식")
                    button.setTextOn("한식")
                    button.setTextOff("한식")
                }
                if(pos == 3) {
                    button.setText("중식")
                    button.setTextOn("중식")
                    button.setTextOff("중식")
                }
                if(pos == 4) {
                    button.setText("일식*회")
                    button.setTextOn("일식*회")
                    button.setTextOff("일식*회")
                }
                if(pos == 5) {
                    button.setText("아시안*양식")
                    button.setTextOn("아시안*양식")
                    button.setTextOff("아시안*양식")
                }
                if(pos == 6) {
                    button.setText("면")
                    button.setTextOn("면")
                    button.setTextOff("면")
                }
                if(pos == 7) {
                    button.setText("분식")
                    button.setTextOn("분식")
                    button.setTextOff("분식")
                }
                if(pos == 8) {
                    button.setText("포차")
                    button.setTextOn("포차")
                    button.setTextOff("포차")
                }
                if(pos == 9) {
                    button.setText("디저트")
                    button.setTextOn("디저트")
                    button.setTextOff("디저트")
                }
                if(pos == 10) {
                    button.setText("프랜차이즈")
                    button.setTextOn("프랜차이즈")
                    button.setTextOff("프랜차이즈")
                }

                toggleBtnAL.add(button)

                button.setOnCheckedChangeListener { buttonView, isChecked ->
                    for (i in 0..toggleBtnAL.size - 1){
                        Log.d("확인 for문 확인", i.toString())
                        if (pos != i){
                            toggleBtnAL[i].isChecked = false
                        }
                    }
                    //toggleBtnAL[pos].isChecked = true
                }

            }


        }


    }


}