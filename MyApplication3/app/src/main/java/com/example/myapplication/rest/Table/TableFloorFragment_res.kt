package com.example.sikdangbook_rest.Table

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.rest.Table.IsBooked.TableStateIsBookedDialog_res
import com.example.myapplication.rest.Resmain.SikdangMain_res

//한 층의 정보 세팅
//TableFloorVPAdapter 에서 사용
class TableFloorFragment_res(var floorNum:Int, val sikdangmainRes: SikdangMain_res, val timeNum:String):Fragment() {


    lateinit var floorImage:ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.res_table_floor_fragment, container, false)


        bind(view)

        return view
    }

    public fun setFloorImage(thisUrl:String){
        Glide.with(this)
                .load(thisUrl)
                .apply(RequestOptions())
                .into(floorImage)
    }

    //여기세 각 층 정보를 넣는다.
    public fun bind(view:View){
        var thisUrl = ""
        floorImage = view.findViewById(R.id.floorImage)


        /*
        for (i in 0..sikdangmainRes.floorUrlAL.size-1){
            if (sikdangmainRes.floorList[floorNum] ==sikdangmainRes.floorUrlAL[i].floor){
                thisUrl = sikdangmainRes.floorUrlAL[i].url
            }
        }
        if (thisUrl ==""){

        }else{
            Glide.with(this)
                    .load(thisUrl)
                    .apply(RequestOptions())
                    .into(floorImage)
        }*/


        Log.d("확인 TableFloorFragment floorNum", floorNum.toString())
        //var tableData = TableData_res()
        var tableLayout:ConstraintLayout = view.findViewById(R.id.tableCL)

        var floorNumTV = view.findViewById<TextView>(R.id.floorNumTV)
        //floorNumTV.setText(tableData.floorList[floorNum])
        floorNumTV.setText(sikdangmainRes.tableData.floorList[floorNum].toString()+"층")
        var i = 0
        while(i<sikdangmainRes.tableData.tableNumList[floorNum]){
            //Log.d("확인 TableFloorFragment i", i.toString())
            var count = i
            if (floorNum == 0){
                count = i
            }
            else{
                count = i+sikdangmainRes.tableData.accumTableNumList[floorNum-1]//테이블리스트의 몇번째인가
            }

            //Log.d("확인 TableFloorFragment 카운트", count.toString())
            var button= Button(getContext())
            if (sikdangmainRes.tableData.tableList[count].isCircle == true)//원형테이블
            {

                var roundDrawable = resources.getDrawable(R.drawable.button_round_gray, null)
                if (sikdangmainRes.tableIsBookedAL[count] == 0) {//예약이 돼있으면
                    roundDrawable = resources.getDrawable(R.drawable.button_round_red, null)
                    button.setOnClickListener {
                        showDialogIsBooked(sikdangmainRes.tableData.tableList[count].maxP, count)
                        //val myToast = Toast.makeText(context, "테이블이 이미 예약되어있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }else {//예약 안되어있으면
                    roundDrawable = resources.getDrawable(R.drawable.button_round_gray, null)
                    button.setOnClickListener {
                        showDialogNotBooked(floorNum, count)
                    }
                }
                button.background = roundDrawable
            } else {//사각 테이블
                if (sikdangmainRes.tableData.tableList[count].isBooked == true) {//이미 예약 된 경우
                    button.setBackgroundColor(Color.parseColor("#CC5555"))
                    button.setOnClickListener {
                        //val myToast = Toast.makeText(context, "테이블이 이미 예약되어있습니다.", Toast.LENGTH_SHORT).show()
                        showDialogIsBooked(sikdangmainRes.tableData.tableList[count].maxP, count)
                    }
                } else {//dialogFragment 검색 or kotlin popup
                    button.setBackgroundColor(Color.parseColor("#CCCCCC"))
                    button.setOnClickListener {
                        showDialogNotBooked(sikdangmainRes.tableData.tableList[count].maxP, count)
                    }
                }


            }
            var layoutParams = ConstraintLayout.LayoutParams(dpToPx(sikdangmainRes.tableData.tableList[i].lengX), dpToPx(sikdangmainRes.tableData.tableList[i].lengY))
            //with와 height에 픽셀값 들어감 => dp를 픽셀값으로 변환한 값 들어가야 한다.

            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.horizontalBias = sikdangmainRes.tableData.tableList[count].locX//0.5가 중앙
            layoutParams.verticalBias = sikdangmainRes.tableData.tableList[count].locy

            button.setLayoutParams(layoutParams)
            tableLayout.addView(button)

            i++

        }

    }

    public fun dpToPx(dp:Int):Int {
        var density:Float = getResources().getDisplayMetrics().density;
        return Math.round(dp.toFloat() * density.toFloat()).toInt()
    }

    public fun showDialogNotBooked(floor:Int, tableNum: Int){
        var customDialog = TableStateNotBookedDialog(this!!.requireContext(), sikdangmainRes, tableNum)
        customDialog!!.show()
    }

    public fun showDialogIsBooked(tablePerson:Int, tableNum: Int){
        var customDialog = TableStateIsBookedDialog_res(this!!.requireContext(), tableNum, sikdangmainRes)
        customDialog!!.show()
    }
}