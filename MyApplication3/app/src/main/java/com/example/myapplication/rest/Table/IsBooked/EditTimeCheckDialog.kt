package com.example.myapplication.rest.Table.IsBooked

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.rest.Table.UserBookData

class EditTimeCheckDialog(context: Context, editTimeDialog: EditTimeDialog): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_checking_dialog)
    }

}