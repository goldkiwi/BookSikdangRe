package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.R

//시간 변경 다이얼로그
//다이얼로그 양쪽에 각각 시간 변경하는거 넣음
class EditTimeDialog(context: Context): Dialog(context) {

    lateinit var lh1:EditText
    lateinit var lh2:EditText
    lateinit var lm1:EditText
    lateinit var lm2:EditText

    lateinit var rh1:EditText
    lateinit var rh2:EditText
    lateinit var rm1:EditText
    lateinit var rm2:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_edittime_dialog)

        lh1=findViewById<EditText>(R.id.lh1)
        lh2=findViewById<EditText>(R.id.lh2)
        lm1=findViewById<EditText>(R.id.lm1)
        lm2=findViewById<EditText>(R.id.lm2)

        var lhup1=findViewById<Button>(R.id.lhup1)
        var lhup2=findViewById<Button>(R.id.lhup2)
        var lhdown1=findViewById<Button>(R.id.lhdown1)
        var lhdown2=findViewById<Button>(R.id.lhdown2)

        var lmup1=findViewById<Button>(R.id.lmup1)
        var lmup2=findViewById<Button>(R.id.lmup2)
        var lmdown1=findViewById<Button>(R.id.lmdown1)
        var lmdown2=findViewById<Button>(R.id.lmdown2)



        rh1=findViewById<EditText>(R.id.rh1)
        rh2=findViewById<EditText>(R.id.rh2)
        rm1=findViewById<EditText>(R.id.rm1)
        rm2=findViewById<EditText>(R.id.rm2)

        var rhup1=findViewById<Button>(R.id.rhup1)
        var rhup2=findViewById<Button>(R.id.rhup2)
        var rhdown1=findViewById<Button>(R.id.rhdown1)
        var rhdown2=findViewById<Button>(R.id.rhdown2)

        var rmup1=findViewById<Button>(R.id.rmup1)
        var rmup2=findViewById<Button>(R.id.rmup2)
        var rmdown1=findViewById<Button>(R.id.rmdown1)
        var rmdown2=findViewById<Button>(R.id.rmdown2)



        setPrimaryTime()

        var timeEditBtn:Button = findViewById(R.id.timeEditBtn)
        timeEditBtn.setOnClickListener {
            editTime()
            this.dismiss()
        }
        var timeEditCancelBtn:Button = findViewById(R.id.timeEditCancelBtn)
        timeEditCancelBtn.setOnClickListener { this.dismiss() }





        /*
        좌측 에딧 텍스트
        시간설정은 00:00~23:59
         */
        lh1.addTextChangedListener {
            //Log.d("확인 TableStateNotBookedDialog_res", "텍스트 변경")
            if (lh1.text.toString() == "0" || lh1.text.toString() == "1"){
                //setTime()
                lh2.requestFocus()
            }else if(lh1.text.toString() == "2"){
                if(lh2.text.toString() == "" || lh2.text.toString() == "0" || lh2.text.toString() == "1" ||
                        lh2.text.toString() == "2" ||lh2.text.toString() == "3"){
                    //setTime()
                    lh2.requestFocus()
                }
                else{
                    lh1.setText("2")
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                    //setTime()
                }
            }
            else if(lh1.text.toString() == ""){
                //setTime()
            }
            else{
                lh1.setText("2")
                val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                //setTime()
            }
        }

        lh2.addTextChangedListener {
            //Log.d("확인 TableStateNotBookedDialog_res", "텍스트 변경")
            if (lh1.text.toString() == "2"){
                if(lh2.text.toString().toString()=="0" || lh2.text.toString().toString()=="1" || lh2.text.toString().toString()=="2" ||
                        lh2.text.toString().toString()=="3"){
                    //setTime()
                    lm1.requestFocus()
                }
                else if(lh2.text.toString() == ""){
                    //setTime()
                }
                else{
                    lh2.setText("0")
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                    //setTime()
                }
            }
            else{
                if(lh2.text.toString() == ""){
                    //setTime()
                }else if(lh2.text.toString().toInt()>9){
                    val myToast = Toast.makeText(context, "입력오류", Toast.LENGTH_SHORT).show()
                    lh2.setText("0")
                }
                else {
                    lm1.requestFocus()
                    //setTime()
                }

            }
        }

        lm1.addTextChangedListener{
            if(lm1.text.toString() == "0" || lm1.text.toString() == "1" || lm1.text.toString() == "2" || lm1.text.toString() == "3"
                    || lm1.text.toString() == "4" || lm1.text.toString() == "5"){
                //setTime()
                lm2.requestFocus()
            }else if(lm1.text.toString() == ""){
                //setTime()
            } else {
                lm1.setText("")
                val myToast = Toast.makeText(context, "최대 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                //setTime()
            }
        }
        lm2.addTextChangedListener{
            if(lm2.text.toString() == "0" || lm2.text.toString() == "1" || lm2.text.toString() == "2" || lm2.text.toString() == "3" ||
                    lm2.text.toString() == "4" || lm2.text.toString() == "5" || lm2.text.toString() == "6" || lm2.text.toString() == "7" ||
                    lm2.text.toString() == "8" || lm2.text.toString() == "9" ){
                //setTime()
                closeKeyBoard()

            }else if(lm2.text.toString() == ""){
                //setTime()
            } else {
                lm2.setText("0")
                val myToast = Toast.makeText(context, "입력 오류", Toast.LENGTH_SHORT).show()
                //setTime()
            }
        }







        /*
        좌측 업다운버튼
        누르면 1씩 늘어나거나 감소
         */




        lhup1.setOnClickListener {
            closeKeyBoard()
            if (lh1.text.toString() == ""){
                lh1.setText("0")
            }else if (lh1.text.toString() == "0"){
                lh1.setText("1")
            }else if(lh1.text.toString() == "1"){
                if (lh2.text.toString().toInt()<4)lh1.setText("2")
                else {
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }

            }else if(lh1.text.toString() == "2"){
                lh1.setText("0")
            }else if(lh1.text.toString() == ""){
                lh1.setText("0")
            }else{
                val myToast = Toast.makeText(context, "최대 24시간까지 가능합니다.", Toast.LENGTH_SHORT).show()
                lh1.setText("2")
            }
        }
        lhup2.setOnClickListener {
            closeKeyBoard()
            if (lh2.text.toString() == ""){
                lh2.setText("0")
            }else if (lh1.text.toString() == "0" || lh1.text.toString() == "1"){
                var tempInt = lh2.text.toString().toInt()
                if (tempInt >= 9){
                    lh2.setText("0")
                }else{
                    tempInt+=1
                    lh2.setText(tempInt.toString())
                }
            }else if(lh1.text.toString() == "2"){
                if(lh2.text.toString().toInt()<3){
                    var tempInt = lh2.text.toString().toInt()
                    tempInt++
                    lh2.setText(tempInt.toString())
                }
                else{
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }else{
                lh2.setText("0")
            }
        }
        lhdown1.setOnClickListener {
            closeKeyBoard()
            if (lh1.text.toString() == ""){
                lh1.setText("0")
            }else if (lh1.text.toString().toInt()>0 ){
                var tempInt = lh1.text.toString().toInt()
                tempInt--
                lh1.setText(tempInt.toString())
            }else if(lh1.text.toString() == ""){
                lh1.setText("0")
            }else{
                if (lh2.text.toString().toInt()<4 ){
                    lh1.setText("2")
                }
                else{
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        lhdown2.setOnClickListener {
            closeKeyBoard()
            if (lh2.text.toString() == ""){
                lh2.setText("0")
            }else if (lh1.text.toString().toInt()==2 ){
                if (lh2.text.toString().toInt()==0 ) lh2.setText("3")
                else if(lh2.text.toString() == ""){
                    lh2.setText("0")
                }
                else{
                    var tempInt = lh2.text.toString().toInt()
                    tempInt--
                    lh2.setText(tempInt.toString())
                }

            }
            else{
                if (lh2.text.toString().toInt()>0 ){
                    var tempInt = lh2.text.toString().toInt()
                    tempInt--
                    lh2.setText(tempInt.toString())
                }
                else if(lh2.text.toString() == ""){
                    lh1.setText("0")
                }
                else{
                    lh2.setText("9")
                }
            }
        }

        lmup1.setOnClickListener {
            closeKeyBoard()
            if (lm1.text.toString() == ""){
                lm1.setText("0")
            }else if(lm1.text.toString().toInt()<5){
                var tempInt = lm1.text.toString().toInt()
                tempInt++
                lm1.setText(tempInt.toString())
            }else if(lm1.text.toString().toInt()==5){
                lm1.setText("0")
            }

        }
        lmup2.setOnClickListener {
            closeKeyBoard()
            if (lm2.text.toString() == ""){
                lm2.setText("0")
            }else if(lm2.text.toString().toInt()<9){
                var tempInt = lm2.text.toString().toInt()
                tempInt++
                lm2.setText(tempInt.toString())
            }else if(lm2.text.toString().toInt()==9){
                lm2.setText("0")
            }else{
                lm2.setText("0")
            }
        }
        lmdown1.setOnClickListener {
            closeKeyBoard()
            if (lm1.text.toString() == ""){
                lm1.setText("0")
            }else if(lm1.text.toString().toInt()>0){
                var tempInt = lm1.text.toString().toInt()
                tempInt--
                lm1.setText(tempInt.toString())
            }else if(lm1.text.toString().toInt()==0){
                lm1.setText("9")
            }
            else{
                lm1.setText("0")
            }

        }
        lmdown2.setOnClickListener {
            closeKeyBoard()
            if (lm2.text.toString() == ""){
                lm2.setText("0")
            }
            else if(lm2.text.toString().toInt()>0){
                var tempInt = lm1.text.toString().toInt()
                tempInt--
                lm2.setText(tempInt.toString())
            }else if(lm2.text.toString().toInt()==0){
                lm2.setText("9")
            }else{
                lm2.setText("0")
            }

        }















        /*
        우측 에딧 텍스트
        시간설정은 00:00~23:59
         */



        rh1.addTextChangedListener {
            //Log.d("확인 TableStateNotBookedDialog_res", "텍스트 변경")
            if (rh1.text.toString() == "0" || rh1.text.toString() == "1"){
                //setTime()
                rh2.requestFocus()
            }else if(rh1.text.toString() == "2"){
                if(rh2.text.toString() == "" || rh2.text.toString() == "0" || rh2.text.toString() == "1" ||
                        rh2.text.toString() == "2" ||rh2.text.toString() == "3"){
                    //setTime()
                    rh2.requestFocus()
                }
                else{
                    rh1.setText("2")
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                    //setTime()
                }
            }
            else if(rh1.text.toString() == ""){
                //setTime()
            }
            else{
                rh1.setText("2")
                val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                //setTime()
            }
        }
        rh2.addTextChangedListener {
            //Log.d("확인 TableStateNotBookedDialog_res", "텍스트 변경")
            if (rh1.text.toString() == "2"){
                if(rh2.text.toString().toString()=="0" || rh2.text.toString().toString()=="1" || rh2.text.toString().toString()=="2" ||
                        rh2.text.toString().toString()=="3"){
                    //setTime()
                    rm1.requestFocus()
                }
                else if(rh2.text.toString() == ""){
                    //setTime()
                }
                else{
                    rh2.setText("0")
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                    //setTime()
                }
            }
            else{
                if(rh2.text.toString() == ""){
                    //setTime()
                }else if(rh2.text.toString().toInt()>9){
                    val myToast = Toast.makeText(context, "입력오류", Toast.LENGTH_SHORT).show()
                    rh2.setText("0")
                }
                else {
                    rm1.requestFocus()
                    //setTime()
                }

            }
        }

        rm1.addTextChangedListener{
            if(rm1.text.toString() == "0" || rm1.text.toString() == "1" || rm1.text.toString() == "2" || rm1.text.toString() == "3"
                    || rm1.text.toString() == "4" || rm1.text.toString() == "5"){
                //setTime()
                rm2.requestFocus()
            }else if(rm1.text.toString() == ""){
                //setTime()
            } else {
                rm1.setText("")
                val myToast = Toast.makeText(context, "최대 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                //setTime()
            }
        }
        rm2.addTextChangedListener{
            if(rm2.text.toString() == "0" || rm2.text.toString() == "1" || rm2.text.toString() == "2" || rm2.text.toString() == "3" ||
                    rm2.text.toString() == "4" || rm2.text.toString() == "5" || rm2.text.toString() == "6" || rm2.text.toString() == "7" ||
                    rm2.text.toString() == "8" || rm2.text.toString() == "9" ){
                //setTime()
                closeKeyBoard()

            }else if(rm2.text.toString() == ""){
                //setTime()
            } else {
                rm2.setText("0")
                val myToast = Toast.makeText(context, "입력 오류", Toast.LENGTH_SHORT).show()
                //setTime()
            }
        }




        /*
        우측 업다운 버튼

         */








        rhup1.setOnClickListener {
            if (rh1.text.toString() == ""){
                rh1.setText("0")
            }else if (rh1.text.toString() == "0"){
                rh1.setText("1")
            }else if(rh1.text.toString() == "1"){
                if (rh2.text.toString().toInt()<4)rh1.setText("2")
                else {
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }

            }else if(rh1.text.toString() == "2"){
                rh1.setText("0")
            }else if(rh1.text.toString() == ""){
                rh1.setText("0")
            }else{
                val myToast = Toast.makeText(context, "최대 24시간까지 가능합니다.", Toast.LENGTH_SHORT).show()
                rh1.setText("2")
            }
        }
        rhup2.setOnClickListener {
            if (rh2.text.toString() == ""){
                rh2.setText("0")
            }else if (rh1.text.toString() == "0" || rh1.text.toString() == "1"){
                var tempInt = rh2.text.toString().toInt()
                if (tempInt >= 9){
                    rh2.setText("0")
                }else{
                    tempInt+=1
                    rh2.setText(tempInt.toString())
                }
            }else if(rh1.text.toString() == "2"){
                if(rh2.text.toString().toInt()<3){
                    var tempInt = rh2.text.toString().toInt()
                    tempInt++
                    rh2.setText(tempInt.toString())
                }
                else{
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }else{
                rh2.setText("0")
            }
        }
        rhdown1.setOnClickListener {
            if (rh1.text.toString() == ""){
                rh1.setText("0")
            }else if (rh1.text.toString().toInt()>0 ){
                var tempInt = rh1.text.toString().toInt()
                tempInt--
                rh1.setText(tempInt.toString())
            }else if(rh1.text.toString() == ""){
                rh1.setText("0")
            }else{
                if (rh2.text.toString().toInt()<4 ){
                    rh1.setText("2")
                }
                else{
                    val myToast = Toast.makeText(context, "최대 23시간 59분까지 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        rhdown2.setOnClickListener {
            if (rh2.text.toString() == ""){
                rh2.setText("0")
            }else if (rh1.text.toString().toInt()==2 ){
                if (rh2.text.toString().toInt()==0 ) rh2.setText("3")
                else if(rh2.text.toString() == ""){
                    rh2.setText("0")
                }
                else{
                    var tempInt = rh2.text.toString().toInt()
                    tempInt--
                    rh2.setText(tempInt.toString())
                }

            }
            else{
                if (rh2.text.toString().toInt()>0 ){
                    var tempInt = rh2.text.toString().toInt()
                    tempInt--
                    rh2.setText(tempInt.toString())
                }
                else if(rh2.text.toString() == ""){
                    rh1.setText("0")
                }
                else{
                    rh2.setText("9")
                }
            }
        }

        rmup1.setOnClickListener {
            if (rm1.text.toString() == ""){
                rm1.setText("0")
            }else if(rm1.text.toString().toInt()<5){
                var tempInt = rm1.text.toString().toInt()
                tempInt++
                rm1.setText(tempInt.toString())
            }else if(rm1.text.toString().toInt()==5){
                rm1.setText("0")
            }

        }
        rmup2.setOnClickListener {
            if (rm2.text.toString() == ""){
                rm2.setText("0")
            }else if(rm2.text.toString().toInt()<9){
                var tempInt = rm2.text.toString().toInt()
                tempInt++
                rm2.setText(tempInt.toString())
            }else if(rm2.text.toString().toInt()==9){
                rm2.setText("0")
            }else{
                rm2.setText("0")
            }
        }
        rmdown1.setOnClickListener {
            if (rm1.text.toString() == ""){
                rm1.setText("0")
            }else if(rm1.text.toString().toInt()>0){
                var tempInt = rm1.text.toString().toInt()
                tempInt--
                rm1.setText(tempInt.toString())
            }else if(rm1.text.toString().toInt()==0){
                rm1.setText("9")
            }
            else{
                rm1.setText("0")
            }

        }
        rmdown2.setOnClickListener {
            if (rm2.text.toString() == ""){
                rm2.setText("0")
            }
            else if(rm2.text.toString().toInt()>0){
                var tempInt = rm1.text.toString().toInt()
                tempInt--
                rm2.setText(tempInt.toString())
            }else if(rm2.text.toString().toInt()==0){
                rm2.setText("9")
            }else{
                rm2.setText("0")
            }

        }
























    }



    //데이터베이스에서 이 예약의 예약 시간 불러온다.
    private fun setPrimaryTime(){
        lh1.setText("1")
        lh2.setText("2")
        lm1.setText("3")
        lm2.setText("0")

        rh1.setText("1")
        rh2.setText("4")
        rm1.setText("5")
        rm2.setText("0")

    }

    private fun editTime(){
        //데이터베이스에 수정할 시간 보내서 수정한다.
    }




    fun closeKeyBoard(){
        var view = this.currentFocus
        if (view != null){
            //var act = activity as SikdangMain_res
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }
    }

    private fun showEditTimeCheckDialog(){
        var customDialog = EditTimeCheckDialog(context, this)
        customDialog!!.show()
    }
}