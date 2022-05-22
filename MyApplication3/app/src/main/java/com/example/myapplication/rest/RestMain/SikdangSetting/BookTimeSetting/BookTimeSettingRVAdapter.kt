package com.example.myapplication.rest.RestMain.SikdangSetting.BookTimeSetting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.*

//BookTimeDialog에서 사용
//시간 나타내는 네개의 버튼 한 줄에 바인드
class BookTimeSettingRVAdapter(var context: Context, val bookTimeSettingDialog: BookTimeSettingDialog): RecyclerView.Adapter<BookTimeSettingRVAdapter.Holder>() {

    var timeNumMax = bookTimeSettingDialog.tempTimeAL.size
    //var timePoint = timeSet()
    //var vartimePoint=timePoint
    var vartimePoint=0
    var buttonAl=ArrayList<Button>()


    override fun getItemViewType(position: Int): Int {
        return position
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookTimeSettingRVAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.res_timeline, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //holder.setIsRecyclable(false)
        holder.bind()
    }

    override fun getItemCount(): Int {
        timeNumMax = bookTimeSettingDialog.tempTimeAL.size
        var i =timeNumMax
        //Log.d("확인 getItemCount ", i.toString()+" "+(i/2).toString())
        return (i+3)/4

    }

    inner class Holder(itemView: View):RecyclerView.ViewHolder(itemView){
        //var callTimePoint =vartimePoint
        public fun bind(){
            //this.setIsRecyclable(false)
            //Log.d("확인 바인드  ", pos.toString()+"/"+bookTimeSettingDialog.tempTimeClass.timeArrayList.size)

            var button1: Button = itemView.findViewById(R.id.timebtn1)
            val timeText = bookTimeSettingDialog.tempTimeAL[vartimePoint]
            button1.setText(timeText)
            //Log.d("확인 바인드  ", "1")


            button1.setOnClickListener {
                if (button1.text.toString()!=""){
                    showCheckDeleteTimeDialog(button1.text.toString())
                    //bookTimeSettingDialog.deleteTime(button1.text.toString())
                }
            }
            buttonAl.add(button1)

            //Log.d("확인 왼쪽 버튼 바인드 ", timeText+vartimePoint.toString())

            //오른쪽 버튼 바인드
            var button2: Button = itemView.findViewById(R.id.timebtn2)
            vartimePoint+=1
            if (vartimePoint<timeNumMax){
                button2.setText(bookTimeSettingDialog.tempTimeAL[vartimePoint])

            }
            else{
                button2.setText("")
            }
            //Log.d("확인 가운데 버튼 바인드 ", timeText+vartimePoint.toString())

            button2.setOnClickListener {
                if (button2.text.toString()!=""){
                    showCheckDeleteTimeDialog(button2.text.toString())
                    //bookTimeSettingDialog.deleteTime(button2.text.toString())
                }
            }
            buttonAl.add(button2)

            //Log.d("확인 바인드  ", "2")


            var button3: Button = itemView.findViewById(R.id.timebtn3)
            vartimePoint+=1
            if (vartimePoint<timeNumMax){
                button3.setText(bookTimeSettingDialog.tempTimeAL[vartimePoint])
            }
            else{
                button3.setText("")
            }

            //Log.d("확인 오른쪽 버튼 바인드 ", timeText+vartimePoint.toString())

            button3.setOnClickListener {
                if (button3.text.toString()!=""){
                    showCheckDeleteTimeDialog(button3.text.toString())
                    //bookTimeSettingDialog.deleteTime(button3.text.toString())
                }
            }

            buttonAl.add(button3)
            //Log.d("확인 바인드  ", "3")

            var button4: Button = itemView.findViewById(R.id.timebtn4)
            vartimePoint+=1
            if (vartimePoint<timeNumMax){
                button4.setText(bookTimeSettingDialog.tempTimeAL[vartimePoint])
                vartimePoint+=1
            }
            else{
                button4.setText("")
            }

            button4.setOnClickListener {
                if (button4.text.toString()!=""){
                    showCheckDeleteTimeDialog(button4.text.toString())
                    //bookTimeSettingDialog.deleteTime(button4.text.toString())
                }
            }
            buttonAl.add(button4)



            //Log.d("확인 바인드  ", "4")



        }

    }


    public fun ButtonALReset(){
        buttonAl=ArrayList<Button>()
    }
    public fun setButtonText(){
        for (i in 0..bookTimeSettingDialog.tempTimeAL.size-2){
            buttonAl[i].setText(bookTimeSettingDialog.tempTimeAL[i])
        }
    }

    public fun timeSet():Int{
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("kk:mm")
        val curTime = dateFormat.format(Date(time))
        var timeString:String=""
        timeString=timeString+curTime[0]+curTime[1]+curTime[3]+curTime[4]

        var i = 0
        while (i<bookTimeSettingDialog.tempTimeAL.size){
            if(timeString <= bookTimeSettingDialog.tempTimeAL[i]){
                break
            }
            //Log.d("확인 time 현재 다음 ", timeString+" "+bookTimeData.getTimeArrayList()[i])
            i++
        }
        if(i==bookTimeSettingDialog.tempTimeAL.size){
            //이경우는 하루 영업이 끝남
            val myToast = Toast.makeText(context, "영업끝", Toast.LENGTH_SHORT).show()
            Log.d("확인 time 현재 다음 ", timeString+"영업끝")
        }
        //Log.d("확인 time 현재 다음 ", timeString+" "+bookTimeData.getTimeArrayList()[i])


        //Log.d("확인 time", timeString+" "+bookTimeData.getTimeArrayList()[0])

        return i
    }

    public fun showCheckDeleteTimeDialog(delTime:String){
        var customDialog = CheckDeleteTimeDialog(context, delTime, bookTimeSettingDialog)
        customDialog!!.show()
    }


}