package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.example.myapplication.rest.Table.UserBookData
import com.example.sikdangbook_rest.Table.TableData_res

//TableFloorFragment_res 에서 사용
//예약된 테이블 선택하면 가장 먼저 나오는 다이얼로그
//여기서 여러 기능 선택
class TableStateIsBookedDialog_res(context: Context, val tableNum:Int, val sikdangmainRes: SikdangMain_res): Dialog(context) {
    var userBookData = UserBookData(tableNum)

    //var tableData= TableData_res()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_tablestateisbooked_dialog)

        var floorNumTV_BookedDialog = findViewById<TextView>(R.id.floorNumTV_BookedDialog)
        var tableNumTV_BookedDialog = findViewById<TextView>(R.id.tableNumTV_BookedDialog)

        floorNumTV_BookedDialog.setText(sikdangmainRes.tableData.tableList[tableNum].floor.toString()+" 층")
        tableNumTV_BookedDialog.setText(tableNum.toString()+" 번 테이블")

        var bookNumberTV = findViewById<TextView>(R.id.bookNumberTV)
        bookNumberTV.setText(userBookData.bookNumber.toString())

        var bookInfoBtn = findViewById<Button>(R.id.bookInfoBtn)
        //예약 정보 버튼
        bookInfoBtn.setOnClickListener {
            showBookInfoDialog()
        }

        //x버튼
        var xTV_bookedDialog:TextView = findViewById(R.id.xTV_bookedDialog)
        xTV_bookedDialog.setOnClickListener { this.dismiss() }


        //예약 수정 버튼
        var editBookBtn:Button = findViewById(R.id.editBookBtn)
        editBookBtn.setOnClickListener {
            showBookEditDialog()
        }

        //예약취소 버튼
        var cancelBookBtn:Button  = findViewById(R.id.cancelBookBtn)
        cancelBookBtn.setOnClickListener {
            showCheckCancelBookDialog()
        }

        //테이블 정리 완료 버튼
        var completeBookBtn:Button = findViewById(R.id.completeBookBtn)
        completeBookBtn.setOnClickListener {
            showTableCompleteDialog()
        }




    }

    public fun showBookEditDialog(){
        var customDialog = BookEditDialog(context, userBookData)
        customDialog!!.show()
    }

    public fun showBookInfoDialog(){
        //tableNum을 그대로 넣으면 모든 층의 테이블 순서
        //층과 그 층의 테이블 번호 따로 하고자 하면
        //층:tableData.tableList[tableNum].floor
        //그 층의 테이블번호 :
        var customDialog = bookInfoDialog(context, userBookData)
        customDialog!!.show()
    }

    public fun showCheckCancelBookDialog(){
        var customDialog = CheckCancelBookDialog(context, userBookData.bookNumber, this)
        customDialog!!.show()
    }

    public fun showTableCompleteDialog(){
        var customDialog = TableCompleteDialog(context, userBookData.bookNumber, this)
        customDialog!!.show()
    }


    public fun cancelBook(){
        //예약 취소
        //데이터베이스에 접속하여 예약 삭제 진행한다. - 데이터를 삭제하지 않고 취소 테이블로 이동시킨다.
        //사용자에게 메세지 보낸다.
        showCancelBookDialog()

    }
    //환불할거냐고 묻는 다이얼로그 호출
    public fun showCancelBookDialog(){
        var customDialog = CancelBookDialog(context, userBookData.bookNumber, this)
        customDialog!!.show()
    }

    //이 테이블들 정리가 끝남 - 새 손님 받을 수 있도록 데이터베이스의 정보 세팅
    public fun tableComplete(){


        closeThis()
    }

    public fun closeThis(){
        this.dismiss()
    }
}