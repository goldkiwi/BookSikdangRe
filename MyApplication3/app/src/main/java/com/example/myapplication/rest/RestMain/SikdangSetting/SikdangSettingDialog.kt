package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.example.myapplication.rest.RestMain.SikdangSetting.BookTimeSetting.BookTimeSettingDialog
import com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting.TableFloorSettingDialog
import com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting.TableSettingDialog

//SikdangMain_res 에서 사용
//식당 정보 수정 - 어떤거 수정할기 고르는 다이얼로그
class SikdangSettingDialog(context: Context, val sikdangNum:String, var sikdangmainRes: SikdangMain_res): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_sikdangsetting_dialog)


        var changeSikdangNameBtn: Button =findViewById(R.id.changeSikdangNameBtn)
        changeSikdangNameBtn.setOnClickListener {
            showChangeSikdangNameDialog()
        }


        var editSikdangExpBntn:Button = findViewById(R.id.editSikdangExpBntn)
        editSikdangExpBntn.setOnClickListener {
            showEditSikdangExpDialog()
        }

        var editSikdangImageBtn:Button = findViewById(R.id.editSikdangImageBtn)
        editSikdangImageBtn.setOnClickListener {
            showEditSikdangImageDialog()
        }

        var editMenuBtn:Button = findViewById(R.id.editMenuBtn)
        editMenuBtn.setOnClickListener {
            showEditMenuDialog()
        }

        var ss_editTableBtn:Button = findViewById(R.id.ss_editTableBtn)
        ss_editTableBtn.setOnClickListener {
            showTableFloorSettingDialog()
        }

        var booktimeSettingBtn:Button = findViewById(R.id.booktimeSettingBtn)
        booktimeSettingBtn.setOnClickListener {
            showBookTimeSettingDialog()
        }

        var ss_cancelBtn:Button=findViewById(R.id.ss_cancelBtn)
        ss_cancelBtn.setOnClickListener { this.dismiss() }




    }


    private fun showChangeSikdangNameDialog(){
        var customDialog = ChangeSikdangNameDialog(context,sikdangNum, sikdangmainRes)
        customDialog!!.show()
    }

    private fun showEditSikdangExpDialog(){
        var customDialog = EditSikdangExpDialog(context,sikdangNum, sikdangmainRes)
        customDialog!!.show()
    }

    private fun showEditMenuDialog(){
        var customDialog = EditMenuDialog(context,sikdangNum, sikdangmainRes)
        customDialog!!.show()
    }

    private fun showTableFloorSettingDialog(){
        var customDialog = TableFloorSettingDialog(context,sikdangNum, sikdangmainRes)
        customDialog!!.show()
    }

    private fun showBookTimeSettingDialog(){
        var customDialog = BookTimeSettingDialog(context,sikdangNum, sikdangmainRes)
        customDialog!!.show()
    }

    private fun showEditSikdangImageDialog(){
        var customDialog = EditSikdangImageDialog(context,sikdangNum, sikdangmainRes)
        customDialog!!.show()
    }


}