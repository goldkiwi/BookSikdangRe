package com.example.myapplication.rest.Resmain

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.rest.RestMain.SikdangSetting.SikdangSettingDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OrderMessageDialog(context: Context, var orderNum:Int, var orederId:String, var sikdangmainRes: SikdangMain_res): Dialog(context) {
    lateinit var omd_tableRV:RecyclerView
    lateinit var rvAdapter:OrderMessageTableRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_ordermessage_dialog)

        var omd_nameTV:TextView=findViewById(R.id.omd_nameTV)
        omd_nameTV.setText(sikdangmainRes.userDataAL[orderNum].username)

        var omd_pnTV:TextView=findViewById(R.id.omd_pnTV)
        omd_pnTV.setText(sikdangmainRes.userDataAL[orderNum].phone_number)

        var omd_priceTV:TextView=findViewById(R.id.omd_priceTV)
        omd_priceTV.setText(sikdangmainRes.msgAL[orderNum].totalPay.toString()+"원")


        omd_tableRV=findViewById(R.id.omd_tableRV)
        rvAdapter = OrderMessageTableRVAdapter(context, orderNum, sikdangmainRes)
        omd_tableRV.adapter = rvAdapter

        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        omd_tableRV.layoutManager=layoutManager
        omd_tableRV.setHasFixedSize(true)



        var omd_applyBtn: Button =findViewById(R.id.omd_permitBtn)
        omd_applyBtn.setOnClickListener {
            showOrderPermitCheckingDialog()
        }

        var omd_denyBtn:Button = findViewById(R.id.omd_denyBtn)
        omd_denyBtn.setOnClickListener {
            showOrderDenyCheckingDialog()
        }



        var omd_cancelBtn:Button = findViewById(R.id.omd_cancelBtn)
        omd_cancelBtn.setOnClickListener { this.dismiss() }


    }

    public fun showOrderPermitCheckingDialog(){
        var customDialog = OrderPermitCheckingDialog(context, this)
        customDialog!!.show()
    }

    public fun showOrderDenyCheckingDialog(){
        var customDialog = OrderDenyCheckingDialog(context, this)
        customDialog!!.show()
    }

    //데이터베이스 접속해서 이 예약 등록
    public fun orderPermit(){

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangmainRes.sikdangId).child(sikdangmainRes.msgKeyAL[orderNum])

        ref.removeValue().addOnCompleteListener {
            sikdangmainRes.renewalOrder()
            this.dismiss()
        }

    }
    //데이터베이스 접속해서 이 예약 거절
    public fun orderDeny(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Store_reservation").child(sikdangmainRes.sikdangId).child(sikdangmainRes.msgKeyAL[orderNum])

        val bookedRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId).child("Booked")

        for (i in 0..sikdangmainRes.msgBookInfoDataAL[orderNum].size-1){
            bookedRef.child(sikdangmainRes.msgBookInfoDataAL[orderNum][i].floor).child(sikdangmainRes.msgAL[orderNum].bookTime).child(sikdangmainRes.msgBookInfoDataAL[orderNum][i].table).setValue(1)
        }

        ref.removeValue().addOnCompleteListener {
            sikdangmainRes.renewalOrder()
            this.dismiss()
        }


    }
}