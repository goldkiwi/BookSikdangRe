package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

//예약 수정 누르면 나오는 다이얼로그
//예약 수정을 얶떤 식으로 진행할지에 대한 버튼이 지정되어있음
//TableNotBookedRVAdapter 에서 사용
class BookEditDialog(context: Context, val userBookData: UserBookData): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_bookedit_dialog)

        var addTableBtn = findViewById<Button>(R.id.addTableBtn)
        addTableBtn.setOnClickListener {
            showAddTableDialog()
        }

        var editTableBtn = findViewById<Button>(R.id.editTableBtn)
        editTableBtn.setOnClickListener {
            showEditTableDialog()
        }

        var cancelTableBtn = findViewById<Button>(R.id.cancelTableBtn)
        cancelTableBtn.setOnClickListener {
            showDeleteTableDialog()
        }

        var editTimeBtn = findViewById<Button>(R.id.editTimeBtn)
        editTimeBtn.setOnClickListener {
            showEditTimeDialog()
        }

        //취소버튼
        var cancelBtn = findViewById<Button>(R.id.cancelBtn)
        cancelBtn.setOnClickListener {
            this.dismiss()
        }
    }

    private fun showAddTableDialog(){
        var customDialog = AddTableDialog(context, userBookData)
        customDialog!!.show()
    }

    private fun showEditTableDialog(){
        var customDialog = EditTableDialog(context, userBookData)
        customDialog!!.show()
    }

    private fun showDeleteTableDialog(){
        var customDialog = DeleteTableDialog(context, userBookData)
        customDialog!!.show()
    }

    private fun showEditTimeDialog(){
        var customDialog = EditTimeDialog(context)
        customDialog!!.show()
    }
}