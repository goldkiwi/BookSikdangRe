package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

//SikdangSettingDialog에서 사용
class EditSikdangImageDialog(context: Context, val sikdangNum: String, var sikdangmainRes: SikdangMain_res): Dialog(context) {

    var isImgAdded = false
    var newUrl = ""

    lateinit var esi_newSikdangImgIV:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_editsikdangimage_dialog)

        esi_newSikdangImgIV=findViewById(R.id.esi_newSikdangImgIV)
        esi_newSikdangImgIV.setOnClickListener {
            sikdangmainRes.editSikdangImageDialog=this
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            sikdangmainRes.startActivityForResult(intent, 3)
        }
        setImgOnView()

        var esi_cancelBtn: Button = findViewById(R.id.esi_cancelBtn)
        esi_cancelBtn.setOnClickListener {
            this.dismiss()
        }

        var esi_editBtn:Button = findViewById(R.id.esi_editBtn)
        esi_editBtn.setOnClickListener {
            if(isImgAdded){
                setNewImgOnDB()
                //upUrlOnDB()
                this.dismiss()
            }
            else Toast.makeText(context, "이미지를 불러와주세요", Toast.LENGTH_SHORT).show()

        }
    }

    public fun setImgOnView(){
        Log.d("확인  setImgOnView()", "1")

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("info")


        var setUrl = ""
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("확인  setImgOnView()", "2")
                for (tableInfo in snapshot.children) {
                    if(tableInfo.key =="store_image"){
                        setUrl = tableInfo.value.toString()
                        Log.d("확인  setImgOnView()", "url : " + setUrl)
                        newUrl=setUrl
                    }
                    Log.d("확인  setImgOnView()", tableInfo.key.toString())
                    //setUrl = tableInfo.key.toString()
                }
                //esi_newSikdangImgIV.setImageURI(setUrl)
                setImgOnView2(setUrl)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
            }
        })
        /*
        val storageRef = FirebaseStorage.getInstance().getReference()
        var islandRef = storageRef.child(sikdangmainRes.sikdangName+"/"+"sikdangImg.jpg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            var a = Data.data
        }.addOnFailureListener {
            // Handle any errors
        }*/




    }

    public fun setImgOnView2(setUrl:String){
        Glide.with(context)
                .load(setUrl)
                .apply(requestOptions)
                .into(esi_newSikdangImgIV)
    }

    val requestOptions : RequestOptions by lazy {
        RequestOptions()
    }

    public fun setNewImg(){
        Log.d("확인 EditSikdangImageDialog.setNewImage", sikdangmainRes.sikdangimgCheckNum.toString())
        Log.d("확인 EditSikdangImageDialog.setNewImage", sikdangmainRes.newSikdangImgUri.toString())
        if(sikdangmainRes.sikdangimgCheckNum==1){
            Log.d("확인 EditSikdangImageDialog.setNewImage", "수행")
            //esi_newSikdangImgIV.setImageBitmap(sikdangmainRes.sikdangimg)
            //esi_newSikdangImgIV.setImageURI(sikdangmainRes.newSikdangImgUri)
            setImgOnView2(sikdangmainRes.newSikdangImgUri.toString())
            sikdangmainRes.sikdangimgCheckNum=0

            isImgAdded = true
            //afterChangeImage.setImageResource(imageRes)
        }


    }

    public fun setNewImgOnDB(){
        var newUrl = ""
        val storageRef = FirebaseStorage.getInstance().getReference()
        //storageRef.
        var file = sikdangmainRes.newSikdangImgUri
        Log.d("확인 ChangeFloorImageDialog", "2 "+sikdangmainRes.sikdangName + "/" + "sikdangImg.jpg")
        val riversRef = storageRef.child(sikdangmainRes.sikdangName + "/" + "sikdangImg.jpg")
        val uploadTask = riversRef.putFile(file!!).addOnSuccessListener {
            val imgurl = riversRef.downloadUrl.addOnSuccessListener {
                uri -> Log.d("확인 ★★★★★★★★11111", uri.toString())
                newUrl=uri.toString()
                upUrlOnDB(newUrl)
            }.toString()
        }
        //upUrlOnDB(newUrl)

    }

    public fun upUrlOnDB(newUrl:String){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("info").child("store_image")
        //Log.d("확인 upUrlOnDB()", sikdangmainRes.sikdangId)
        //Log.d("확인 upUrlOnDB()", newUrl.toString())
        ref.setValue(newUrl)

        var ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Locations").child(sikdangmainRes.sikdangId).child("store_image")
        ref2.setValue(newUrl)

    }





}