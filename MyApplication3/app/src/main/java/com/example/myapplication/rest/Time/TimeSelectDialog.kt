package com.example.sikdangbook_rest.Time

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res

class TimeSelectDialog(context: Context, var sikdangMain_res: SikdangMain_res): Dialog(context)  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_timeselect_dialog)



        var timeRV : RecyclerView = findViewById(R.id.timeRV)
        var timeRVAdapter = TimeLineAdapter(context, this, sikdangMain_res)
        timeRV.adapter = timeRVAdapter



        var timeLM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        timeRV.layoutManager=timeLM
        timeRV.setHasFixedSize(true)
        //timeRV.setIsRecyclable(false)
    }

    //시간 텍스트 바꿔줌
    //TimeLineAdapter 에서 호출
    public fun setTimeNum(timeNum:String){
        sikdangMain_res.setTimeNum(timeNum)

        sikdangMain_res.showTime = timeNum
        sikdangMain_res.getTableDataLineNum=0
        sikdangMain_res.getTableDataFromDB()
        this.dismiss()
    }



}