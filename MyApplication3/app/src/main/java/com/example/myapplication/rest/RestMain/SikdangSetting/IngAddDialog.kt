package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication._Ingredient


//AddMenuDialog 에서 사용
//재료를 추가하고 삭제하는 다이얼로그

class IngAddDialog(context: Context, val sikgangNum: String, var addMenuDialog: AddMenuDialog): Dialog(context) {

    lateinit var ia_RV: RecyclerView
    lateinit var rVAdapter:AddIngRVAdapter

    var changedIng = ArrayList<_Ingredient>()

    init{
        //상위 다이얼로그에서 재료들 불러옴
        for (i in 0..addMenuDialog.addIngAL.size-1){
            var tempIng=_Ingredient(addMenuDialog.addIngAL[i].ing, addMenuDialog.addIngAL[i].country)
            changedIng.add(tempIng)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_ingedit_dialog)

        ia_RV  = findViewById(R.id.ie_RV)

        rVAdapter = AddIngRVAdapter(context, this)
        ia_RV.adapter = rVAdapter

        var LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        ia_RV.layoutManager=LM
        ia_RV.setHasFixedSize(true)

        var ie_ingAddBtn = findViewById<Button>(R.id.ie_ingAddBtn)
        ie_ingAddBtn.setOnClickListener {
            changedIng.add(_Ingredient("", ""))
            rVAdapter.setIng()
            renewalRV()
        }

        var ie_ChangeBtn = findViewById<Button>(R.id.ie_ChangeBtn)
        ie_ChangeBtn.setOnClickListener {
            //changedIng.add(_Ingredient("", ""))
            rVAdapter.setIng()
            addMenuDialog.addIngAL = changedIng
            addMenuDialog.renewalRV()

            this.dismiss()
        }
    }

    public fun renewalRV(){
        rVAdapter.notifyDataSetChanged()
    }
}