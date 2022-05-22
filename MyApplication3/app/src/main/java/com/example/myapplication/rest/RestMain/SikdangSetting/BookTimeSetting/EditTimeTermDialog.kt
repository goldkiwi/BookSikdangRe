package com.example.myapplication.rest.RestMain.SikdangSetting.BookTimeSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.R

//BookTimeSettingDialog 에서 사용
//다이얼 뜨고 시간 선택하면 예약 텀 선택 가능
class EditTimeTermDialog(context: Context, var nowTerm:String, var bookTimeSettingDialog: BookTimeSettingDialog): Dialog(context) {

    lateinit var hET1: EditText
    lateinit var hET2: EditText
    lateinit var mET1: EditText
    lateinit var mET2: EditText
    lateinit var hupBtn1: Button
    lateinit var hupBtn2: Button
    lateinit var hdownBtn1: Button
    lateinit var hdownBtn2: Button

    lateinit var mupBtn1: Button
    lateinit var mupBtn2: Button
    lateinit var mdownBtn1: Button
    lateinit var mdownBtn2: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_addtime_dialog)

        hET1 = findViewById<EditText>(R.id.at_hET1)
        hET2 = findViewById<EditText>(R.id.at_hET2)
        mET1 = findViewById<EditText>(R.id.at_mET1)
        mET2 = findViewById<EditText>(R.id.at_mET2)

        hupBtn1=findViewById(R.id.at_hup1)
        hupBtn2=findViewById(R.id.at_hup2)
        hdownBtn1=findViewById(R.id.at_hdown1)
        hdownBtn2=findViewById(R.id.at_hdown2)

        mupBtn1=findViewById(R.id.at_mup1)
        mupBtn2=findViewById(R.id.at_mup2)
        mdownBtn1=findViewById(R.id.at_mdown1)
        mdownBtn2=findViewById(R.id.at_mdown2)

        var at_expTV: TextView =findViewById(R.id.at_expTV)
        at_expTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

        at_expTV.setText("예약 시간 텀 설정\n소비자가 예약할 때의 기본 예약 텀 설정합니다."+
                "\n예)텀 01:20 예약 시작 13:20\n14:40분까지 예약")

        var a:String = nowTerm.slice(IntRange(0,0))
        var b:String = nowTerm.slice(IntRange(1,1))
        var c:String = nowTerm.slice(IntRange(2,2))
        var d:String = nowTerm.slice(IntRange(3,3))

        hET1.setText(a)
        hET2.setText(b)
        mET1.setText(c)
        mET2.setText(d)


        hET1.setText("0")
        hET1.addTextChangedListener {
            //Log.d("확인 hET1.addTextChangedListener", "1")
            //Log.d("확인 TableStateNotBookedDialog_res", "텍스트 변경")
            if (hET1.text.toString() == "0" || hET1.text.toString() == "1"){
                //Log.d("확인 hET1.addTextChangedListener", "2")

                //Log.d("확인 hET1.addTextChangedListener", "3")
                hET2.requestFocus()
            }else if(hET1.text.toString() == "2"){
                if(hET2.text.toString() == "" || hET2.text.toString() == "0" || hET2.text.toString() == "1" ||
                        hET2.text.toString() == "2" ||hET2.text.toString() == "3"){

                    hET2.requestFocus()
                }
                else{
                    hET1.setText("2")
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()

                }
            }
            else if(hET1.text.toString() == ""){

            }
            else{
                hET1.setText("2")
                val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()

            }
        }
        hET2.setText("0")
        hET2.addTextChangedListener {
            //Log.d("확인 TableStateNotBookedDialog_res", "텍스트 변경")
            if (hET1.text.toString() == "2"){
                if(hET2.text.toString().toString()=="0" || hET2.text.toString().toString()=="1" || hET2.text.toString().toString()=="2" ||
                        hET2.text.toString().toString()=="3"){

                    mET1.requestFocus()
                }
                else if(hET2.text.toString() == ""){

                }
                else{
                    hET2.setText("0")
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()

                }
            }
            else{
                if(hET2.text.toString() == ""){

                }else if(hET2.text.toString().toInt()>9){
                    val myToast = Toast.makeText(context, "입력오류", Toast.LENGTH_SHORT).show()
                    hET2.setText("0")
                }
                else {
                    mET1.requestFocus()

                }

            }
        }
        mET1.setText("0")
        mET1.addTextChangedListener{
            if(mET1.text.toString() == "0" || mET1.text.toString() == "1" || mET1.text.toString() == "2" || mET1.text.toString() == "3"
                    || mET1.text.toString() == "4" || mET1.text.toString() == "5"){

                mET2.requestFocus()
            }else if(mET1.text.toString() == ""){

            } else {
                mET1.setText("")
                val myToast = Toast.makeText(context, "최대 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()

            }
        }
        mET2.setText("1")
        mET2.addTextChangedListener{
            if(mET2.text.toString() == "0" || mET2.text.toString() == "1" || mET2.text.toString() == "2" || mET2.text.toString() == "3" ||
                    mET2.text.toString() == "4" || mET2.text.toString() == "5" || mET2.text.toString() == "6" || mET2.text.toString() == "7" ||
                    mET2.text.toString() == "8" || mET2.text.toString() == "9" ){

                //closeKeyBoard()

            }else if(mET2.text.toString() == ""){

            } else {
                mET2.setText("0")
                val myToast = Toast.makeText(context, "입력 오류", Toast.LENGTH_SHORT).show()

            }
        }




        hupBtn1.setOnClickListener {
            Log.d("확인 hupBtn1.setOnClickListener", "1")
            if (hET1.text.toString() == ""){
                hET1.setText("0")
            }else if (hET1.text.toString() == "0"){
                hET1.setText("1")
            }else if(hET1.text.toString() == "1"){
                if (hET2.text.toString().toInt()<4)hET1.setText("2")
                else {
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }

            }else if(hET1.text.toString() == "2"){
                hET1.setText("0")
            }else if(hET1.text.toString() == ""){
                hET1.setText("0")
            }else{
                val myToast = Toast.makeText(context, "최대 24시간까지 가능합니다.", Toast.LENGTH_SHORT).show()
                hET1.setText("2")
            }
        }
        hupBtn2.setOnClickListener {
            if (hET2.text.toString() == ""){
                hET2.setText("0")
            }else if (hET1.text.toString() == "0" || hET1.text.toString() == "1"){
                var tempInt = hET2.text.toString().toInt()
                if (tempInt >= 9){
                    hET2.setText("0")
                }else{
                    tempInt+=1
                    hET2.setText(tempInt.toString())
                }
            }else if(hET1.text.toString() == "2"){
                if(hET2.text.toString().toInt()<3){
                    var tempInt = hET2.text.toString().toInt()
                    tempInt++
                    hET2.setText(tempInt.toString())
                }
                else{
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }else{
                hET2.setText("0")
            }
        }
        hdownBtn1.setOnClickListener {
            if (hET1.text.toString() == ""){
                hET1.setText("0")
            }else if (hET1.text.toString().toInt()>0 ){
                var tempInt = hET1.text.toString().toInt()
                tempInt--
                hET1.setText(tempInt.toString())
            }else if(hET1.text.toString() == ""){
                hET1.setText("0")
            }else{
                if (hET2.text.toString().toInt()<4 ){
                    hET1.setText("2")
                }
                else{
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        hdownBtn2.setOnClickListener {
            if (hET2.text.toString() == ""){
                hET2.setText("0")
            }else if (hET1.text.toString().toInt()==2 ){
                if (hET2.text.toString().toInt()==0 ) hET2.setText("3")
                else if(hET2.text.toString() == ""){
                    hET2.setText("0")
                }
                else{
                    var tempInt = hET2.text.toString().toInt()
                    tempInt--
                    hET2.setText(tempInt.toString())
                }

            }
            else{
                if (hET2.text.toString().toInt()>0 ){
                    var tempInt = hET2.text.toString().toInt()
                    tempInt--
                    hET2.setText(tempInt.toString())
                }
                else if(hET2.text.toString() == ""){
                    hET1.setText("0")
                }
                else{
                    hET2.setText("9")
                }
            }
        }

        mupBtn1.setOnClickListener {
            if (mET1.text.toString() == ""){
                mET1.setText("0")
            }else if(mET1.text.toString().toInt()<5){
                var tempInt = mET1.text.toString().toInt()
                tempInt++
                mET1.setText(tempInt.toString())
            }else if(mET1.text.toString().toInt()==5){
                mET1.setText("0")
            }

        }
        mupBtn2.setOnClickListener {
            if (mET2.text.toString() == ""){
                mET2.setText("0")
            }else if(mET2.text.toString().toInt()<9){
                var tempInt = mET2.text.toString().toInt()
                tempInt++
                mET2.setText(tempInt.toString())
            }else if(mET2.text.toString().toInt()==9){
                mET2.setText("0")
            }else{
                mET2.setText("0")
            }
        }
        mdownBtn1.setOnClickListener {
            if (mET1.text.toString() == ""){
                mET1.setText("0")
            }else if(mET1.text.toString().toInt()>0){
                var tempInt = mET1.text.toString().toInt()
                tempInt--
                mET1.setText(tempInt.toString())
            }else if(mET1.text.toString().toInt()==0){
                mET1.setText("9")
            }
            else{
                mET1.setText("0")
            }

        }
        mdownBtn2.setOnClickListener {
            if (mET2.text.toString() == ""){
                mET2.setText("0")
            }
            else if(mET2.text.toString().toInt()>0){
                var tempInt = mET2.text.toString().toInt()
                tempInt--
                mET2.setText(tempInt.toString())
            }else if(mET2.text.toString().toInt()==0){
                mET2.setText("9")
            }else{
                mET2.setText("0")
            }

        }




        var at_cancelBtn:Button = findViewById(R.id.at_cancelBtn)
        at_cancelBtn.setOnClickListener { this.dismiss() }





        var at_addTimeBtn: Button = findViewById(R.id.at_addTimeBtn)
        at_addTimeBtn.setText("텀 변경")

        at_addTimeBtn.setOnClickListener {
            var newTerm:String = hET1.text.toString()+hET2.text.toString()+mET1.text.toString()+mET2.text.toString()
            var addChecked = bookTimeSettingDialog.editTerm(newTerm)
            this.dismiss()

        }
    }
}