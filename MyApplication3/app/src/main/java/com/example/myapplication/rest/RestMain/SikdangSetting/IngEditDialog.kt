package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication._Ingredient

//MenuEditDialog에서 사용
//재료들 수정할 수 있는 라인 바인드
class IngEditDialog(context: Context, val sikgangNum: String, var menuNum:Int, var menuEditDialog: MenuEditDialog, var editMenuDialog: EditMenuDialog): Dialog(context) {

    var changedIng = ArrayList<_Ingredient>()
    lateinit var ie_RV:RecyclerView
    lateinit var rVAdapter:IngredientEditRVAdapter

    init{
        for(i in 0..menuEditDialog.afterIngAL.size-1){
            changedIng.add(_Ingredient(menuEditDialog.afterIngAL[i].ing,menuEditDialog.afterIngAL[i].country ))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_ingedit_dialog)


        ie_RV  = findViewById(R.id.ie_RV)

        rVAdapter = IngredientEditRVAdapter(context, menuNum, this, editMenuDialog)
        ie_RV.adapter = rVAdapter

        var LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        ie_RV.layoutManager=LM
        ie_RV.setHasFixedSize(true)
        //rVAdapter.setETText()

        var ie_ingAddBtn = findViewById<Button>(R.id.ie_ingAddBtn)
        ie_ingAddBtn.setOnClickListener {
            Log.d("확인 IngEditDialog", "추가버튼 클릭")
            changedIng.add(_Ingredient("", ""))
            rVAdapter.setIng()
            renewalRV()
            //rVAdapter.setETText()
            //rVAdapter.setCorrectPos()
        }

        var ie_ChangeBtn = findViewById<Button>(R.id.ie_ChangeBtn)
        ie_ChangeBtn.setOnClickListener {
            //changedIng.add(_Ingredient("", ""))
            rVAdapter.setIng()
            menuEditDialog.afterIngAL = changedIng
            menuEditDialog.renewalAfterIngRV()

            this.dismiss()
        }

        var ie_CancelBtn:Button = findViewById(R.id.ie_CancelBtn)
        ie_CancelBtn.setOnClickListener { this.dismiss() }
    }

    public fun renewalRV(){
        rVAdapter.notifyDataSetChanged()

    }
}