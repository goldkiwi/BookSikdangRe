package com.example.sikdangbook_rest.Time

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.example.myapplication.rest.Time.TempTimeClass
import java.text.SimpleDateFormat
import java.util.*

class TimeLineAdapter(var context: Context, val timeSelectDialog: TimeSelectDialog, var sikdangmainRes: SikdangMain_res): RecyclerView.Adapter<TimeLineAdapter.Holder>() {
    //var tempTimeClass = TempTimeClass(1111)

    //var timeNumMax = tempTimeClass.timeArrayList.size
    var timeNumMax = sikdangmainRes.timeAL.size
    var timePoint = timeSet()
    var vartimePoint=timePoint

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view = LayoutInflater.from(context).inflate(R.layout.res_timeline, parent, false)
        return Holder(view)
    }

    public fun timeSet():Int{
        //Log.d("확인 timeSet() ", sikdangmainRes.timeAL.toString())
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("kk:mm")
        val curTime = dateFormat.format(Date(time))
        var timeString:String=""
        timeString=timeString+curTime[0]+curTime[1]+curTime[3]+curTime[4]
        var tempTimeInt = timeString.toInt()
        //Log.d("확인 timeSet() 변형 ", timeString+" "+tempTimeInt.toString())
        if (tempTimeInt > 1200) {
            tempTimeInt -= 1200
            if(tempTimeInt < 10) timeString="00:0"+tempTimeInt.toString().slice(IntRange(0, 0))+" 오후"
            else if(tempTimeInt < 100)timeString="00:"+tempTimeInt.toString().slice(IntRange(0, 1))+" 오후"
            else if(tempTimeInt < 1000){
                timeString="0"+tempTimeInt.toString().slice(IntRange(0, 0)) +":"+tempTimeInt.toString().slice(IntRange(1, 2))+" 오후"
            }
            else{
                timeString=tempTimeInt.toString().slice(IntRange(0, 1)) +":"+tempTimeInt.toString().slice(IntRange(2, 3))+" 오후"
            }
        }
        else{
            if(tempTimeInt < 10) timeString="00:0"+tempTimeInt.toString().slice(IntRange(0, 0))+" 오후"
            else if(tempTimeInt < 100)timeString="00:"+tempTimeInt.toString().slice(IntRange(0, 1))+" 오후"
            else if(tempTimeInt < 1000){
                timeString="0"
                timeString+=tempTimeInt.toString().slice(IntRange(0, 0))
                timeString+=":"
                timeString+=tempTimeInt.toString().slice(IntRange(1, 2))
                timeString+=" 오전"
            }
            else{
                timeString=tempTimeInt.toString().slice(IntRange(0, 1)) +":"+tempTimeInt.toString().slice(IntRange(2, 3))+" 오전"
            }
            //Log.d("확인 timeSet() 변형3 ", timeString+" "+tempTimeInt.toString())
        }


        var i = 0
        while (i<sikdangmainRes.timeAL.size){
            if(timeString <= sikdangmainRes.timeAL[i]){
                break
            }
            //Log.d("확인 time 현재 다음 ", timeString+" "+sikdangmainRes.timeAL[i])
            i++
        }
        //Log.d("확인 time 현재 i ", i.toString())
        if(i==sikdangmainRes.timeAL.size){
            //이경우는 하루 영업이 끝남
            val myToast = Toast.makeText(context, "영업끝", Toast.LENGTH_SHORT).show()
            //Log.d("확인 time 현재 다음 ", timeString+"영업끝")
        }
        //Log.d("확인 time 현재 다음 ", timeString+" "+bookTimeData.getTimeArrayList()[i])


        //Log.d("확인 time", timeString+" "+bookTimeData.getTimeArrayList()[0])

        return i
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        var i =timeNumMax - timePoint
        //Log.d("확인 getItemCount ", i.toString()+" "+(i/2).toString())
        return (i+3)/4
    }

    inner class Holder(itemView: View):RecyclerView.ViewHolder(itemView){
        var callTimePoint =vartimePoint
        public fun bind(){
            //this.setIsRecyclable(false)

            var button1: Button = itemView.findViewById(R.id.timebtn1)
            val timeText = sikdangmainRes.timeAL[vartimePoint]
            button1.setText(timeText)


            button1.setOnClickListener {
                if (button1.text.toString()!=""){
                    timeSelectDialog.setTimeNum(button1.text.toString())
                }
            }

            //Log.d("확인 왼쪽 버튼 바인드 ", timeText+vartimePoint.toString())

            //오른쪽 버튼 바인드
            var button2: Button = itemView.findViewById(R.id.timebtn2)
            vartimePoint+=1
            if (vartimePoint<timeNumMax){
                button2.setText(sikdangmainRes.timeAL[vartimePoint])

            }
            else{
                button2.setText("")
            }
            //Log.d("확인 가운데 버튼 바인드 ", timeText+vartimePoint.toString())

            button2.setOnClickListener {
                if (button2.text.toString()!=""){
                    timeSelectDialog.setTimeNum(button2.text.toString())
                }
            }



            var button3: Button = itemView.findViewById(R.id.timebtn3)
            vartimePoint+=1
            if (vartimePoint<timeNumMax){
                button3.setText(sikdangmainRes.timeAL[vartimePoint])
            }
            else{
                button3.setText("")
            }
            //Log.d("확인 오른쪽 버튼 바인드 ", timeText+vartimePoint.toString())

            button3.setOnClickListener {
                if (button3.text.toString()!=""){
                    timeSelectDialog.setTimeNum(button3.text.toString())
                }
            }

            var button4: Button = itemView.findViewById(R.id.timebtn4)
            vartimePoint+=1
            if (vartimePoint<timeNumMax){
                button4.setText(sikdangmainRes.timeAL[vartimePoint])
                vartimePoint+=1
            }
            else{
                button4.setText("")
            }

            button4.setOnClickListener {
                if (button4.text.toString()!=""){
                    timeSelectDialog.setTimeNum(button4.text.toString())
                }
            }



        }



    }


}