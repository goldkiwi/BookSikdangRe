package com.example.myapplication.rest.ResInfo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

//ResInfoActivity 에서 사용
class SellInfoActivity: AppCompatActivity() {
    var nowPage = 1
    var nowPageSet = 1

    var sellDataAL=ArrayList<SellData>()

    lateinit var si_nBtn1:TextView
    lateinit var si_nBtn2:TextView
    lateinit var si_nBtn3:TextView
    lateinit var si_nBtn4:TextView
    lateinit var si_nBtn5:TextView

    lateinit var si_sellRV:RecyclerView
    lateinit var rvAdapter:SellRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_selliffo_activity)

        setSellData()



        si_sellRV  = findViewById(R.id.si_sellRV)
        rvAdapter = SellRVAdapter(this, this)
        si_sellRV.adapter = rvAdapter

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        si_sellRV.layoutManager=layoutManager
        si_sellRV.setHasFixedSize(true)




        var si_numBefBtn: Button = findViewById(R.id.si_numBefBtn)
        si_numBefBtn.setOnClickListener {
            if(nowPageSet>1){
                nowPageSet-=1
            }
            pageSet()

        }

        var si_numNextBtn:Button = findViewById(R.id.si_numNextBtn)
        si_numNextBtn.setOnClickListener {
            nowPageSet+=1
            pageSet()
        }

        si_nBtn1=findViewById(R.id.si_nBtn1)
        si_nBtn1.setOnClickListener {
            nowPage = nowPageSet*5-4
            setNowPage()
        }
        si_nBtn2=findViewById(R.id.si_nBtn2)
        si_nBtn2.setOnClickListener {
            nowPage = nowPageSet*5-3
            setNowPage()
        }
        si_nBtn3=findViewById(R.id.si_nBtn3)
        si_nBtn3.setOnClickListener {
            nowPage = nowPageSet*5-2
            setNowPage()
        }
        si_nBtn4=findViewById(R.id.si_nBtn4)
        si_nBtn4.setOnClickListener {
            nowPage = nowPageSet*5-1
            setNowPage()
        }
        si_nBtn5=findViewById(R.id.si_nBtn5)
        si_nBtn5.setOnClickListener {
            nowPage = nowPageSet*5
            setNowPage()
        }

    }

    //nowPageSet 바꿈
    private fun pageSet(){
        si_nBtn1.setText((nowPageSet*5-4).toString())
        si_nBtn2.setText((nowPageSet*5-3).toString())
        si_nBtn3.setText((nowPageSet*5-2).toString())
        si_nBtn4.setText((nowPageSet*5-1).toString())
        si_nBtn5.setText((nowPageSet*5).toString())
    }

    private fun setNowPage(){
        setSellData()
        renewalRV()
    }
    public fun renewalRV(){
        Log.d("확인 리뉴얼", "되나?")
        //bookTimeSettingRVAdapter.ButtonALReset()
        //bookTimeSettingRVAdapter.vartimePoint=0
        rvAdapter.notifyDataSetChanged()
        //bookTimeSettingRVAdapter.setButtonText()
    }


    //데이터베이스 접속해서 SellData 불러옴
    public fun setSellData(){
        //nowPage 는 현재 페이지 나타냄
        //한 페이지에 10개 들어있음
        //페이지는 1페이지부터 시작
        sellDataAL=ArrayList<SellData>()
        //ArrayList 한번 비우고 아래부터 데이터베이스에서 10개 가져와 채움
        //만약 데이터가 15개 있고 페이지를 200페이지 같은데 가서 데이터 넘어선ㅁ곳 접근하려할 시 String은 정보 없음 으로, Int는 0으로 채움
        sellDataAL.add(SellData("임꺽정", "010-1234-5678", 80000, 2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("장길산", "010-1234-5678", 90000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("로빈훗", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("철수", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("영희", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("바둑이", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("임꺽정", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("임꺽정", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("임꺽정", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))
        sellDataAL.add(SellData("임꺽정", "010-1234-5678", 80000,2021, 5, 21, 9, 34, 10, 9, 30, 10, 30))

    }

    //conName 소비자 이름 pn 소비자 번호 price 가격 y, m, day, h, min, sec, 예약 수락 or 예약 신청 시간 sh, sm 예약 시작시간 eh em 예약 끝 시간
    inner class SellData(var conName:String, var pn:String, var price:Int,
                         var y:Int, var m:Int, var day:Int, var h:Int, var min:Int, var sec:Int,
                         var sh:Int, var sm:Int, var eh:Int, var em:Int)

    inner class InnerTime(y:Int, m:Int, d:Int, h:Int, min:Int, sec:Int)


}