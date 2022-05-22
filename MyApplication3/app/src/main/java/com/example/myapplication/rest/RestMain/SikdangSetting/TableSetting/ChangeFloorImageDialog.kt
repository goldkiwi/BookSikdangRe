package com.example.myapplication.rest.RestMain.SikdangSetting.TableSetting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

//TableSettingDialog에서 사용
//층 단면도 변경 다이얼로그
//
class ChangeFloorImageDialog(context: Context, val sikdangNum: String, val floor: Int, var tableSettingDialog: TableSettingDialog, var sikdangmainRes: SikdangMain_res): Dialog(context) {
    //var newUrl:String=""
    lateinit var newImg:Bitmap
    var isImeSetted = false
    lateinit var cfi_newIV:ImageView

    var inImageSetted = false
    var newUri = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_changefloorimage_dialog)

        cfi_newIV=findViewById(R.id.cfi_newIV)

        var cfi_getImgBtn:Button=findViewById(R.id.cfi_getImgBtn)
        cfi_getImgBtn.setOnClickListener {
            setImgOnGall()
        }

        var cfi_changeBtn:Button = findViewById(R.id.cfi_changeBtn)
        cfi_changeBtn.setOnClickListener {
            setNewImgOnDB()
            if (isImeSetted == true){//이미 새 이미지 갤러리에서 가져온 경우
                if(true){//데이터 올리기 성공한 경우
                    //tableSettingDialog.setNewFloorImg(newImg)
                    this.dismiss()
                }
                else{//데이터베이스에 올리기 실패
                    val myToast = Toast.makeText(context, "서버 접속 실패", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                val myToast = Toast.makeText(context, "새 이미지를 불러오십시오", Toast.LENGTH_SHORT).show()
            }

        }

        var cfi_cancelBtn:Button = findViewById(R.id.cfi_cancelBtn)
        cfi_cancelBtn.setOnClickListener { this.dismiss() }


    }

    //이미지 갤러리에서 찾아와서 newImg에 넣는다.
    private fun setImgOnGall(){
        //아래는 임시로 넣음


        sikdangmainRes.changeFloorImageDialog=this
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        sikdangmainRes.startActivityForResult(intent, 2)


        /*
        newImg = R.drawable.store_layout_example
        cfi_newIV.setImageResource(newImg)

        if(true){//갤러리에서 가져오기 성공시
            isImeSetted = true
        }*/
    }

    public fun setNewImage(){
        if(sikdangmainRes.sikdangimgCheckNum==1){
            Log.d("확인 ChangeFloorImageDialog.setNewImage", "수행")
            setImgOnView(sikdangmainRes.newFloorImageUri.toString())
            sikdangmainRes.sikdangimgCheckNum=0
            newUri=sikdangmainRes.newFloorImageUri.toString()
            isImeSetted = true
            //afterChangeImage.setImageResource(imageRes)
        }
    }

    public fun setImgOnView(url:String){
        Glide.with(context)
                .load(url)
                .apply(RequestOptions())
                .into(cfi_newIV)
    }

    /*
    public fun setNewImg(){
        if(sikdangmainRes.sikdangimgCheckNum==1){
            Log.d("확인 setNewImg()", "이미지 변경1")
            cfi_newIV.setImageBitmap(sikdangmainRes.sikdangimg)
            //newImg=sikdangmainRes.sikdangimg.toString().toInt()

            sikdangmainRes.sikdangimgCheckNum=0

            //afterChangeImage.setImageResource(imageRes)

            //cfi_newIV.setImageResource(newImg)

            //var btd:Drawable = BitmapDrawable(sikdangmainRes.sikdangimg)
            newImg=sikdangmainRes.sikdangimg
            Log.d("확인 setNewImg()", "이미지 변경2")
            isImeSetted = true

        }
        else{
            Log.d("확인 setNewImg()", sikdangmainRes.sikdangimgCheckNum.toString())
            isImeSetted = false
        }
    }*/

    //데이터베이스에 이미지 올림
    private fun setNewImgOnDB(){

        var newUrl = ""
        val storageRef = FirebaseStorage.getInstance().getReference()
        Log.d("확인 ChangeFloorImageDialog", "1 "+newUri.toString())
        //storageRef.
        var file = sikdangmainRes.newFloorImageUri
        Log.d("확인 ChangeFloorImageDialog", "2 "+sikdangmainRes.sikdangName+"/floor"+floor.toString() + ".jpg")
        val riversRef = storageRef.child(sikdangmainRes.sikdangName+"/floor_"+floor.toString() + ".jpg")
        Log.d("확인 ChangeFloorImageDialog", "3 ")
        val uploadTask = riversRef.putFile(file!!).addOnSuccessListener {
            val imgurl = riversRef.downloadUrl.addOnSuccessListener {
                uri -> Log.d("확인 ChangeFloorImageDialog", uri.toString())
                newUrl=uri.toString()
                upUrlOnDB(newUrl)
                tableSettingDialog.setNewFloorImg(newUri)
            }.toString()
        }
        //upUrlOnDB(newUrl



    }

    public fun upUrlOnDB(newUrl:String){
        /*
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId)*/

        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Tables").child(sikdangmainRes.sikdangId)
        Log.d("확인 upUrlOnDB()", sikdangmainRes.sikdangId)
        Log.d("확인 upUrlOnDB()", newUrl.toString())
        ref.child("floorUrl").child("floor_"+floor.toString()).setValue(newUrl)
    }

}