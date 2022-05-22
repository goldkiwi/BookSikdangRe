package com.example.myapplication.rest.RestMain.SikdangSetting

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication._Ingredient
import com.example.myapplication.bookActivity.MenuData
import com.example.myapplication.rest.Resmain.SikdangMain_res
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


//EditMenuRVAdapter 에서 사용
//선택된 메뉴의 상세 내용 수정하는 다이얼로그

class MenuEditDialog(context: Context, val sikdangNum: String, val menuNum: Int, val menuData: MenuData, var editMenuDialog: EditMenuDialog, var sikdangmainRes: SikdangMain_res): Dialog(context) {

    private final var REQUEST_CODE = 0

    var afterIngAL=ArrayList<_Ingredient>()

    lateinit var RVAdapter2:AfterIngRVAdapter

    lateinit var afterChangeImage:ImageView

    lateinit var beforeChangeImage : ImageView

    var isnewImageSetted = false


    init{
        afterIngAL=menuData.ingredients
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_menuedit_dialog)

        var entLayout=findViewById<LinearLayout>(R.id.entLayout)
        entLayout.setOnClickListener {
            closeKeyBoard()
        }
        sikdangmainRes.menuEditDialog=this

        setCanceledOnTouchOutside(false)


        var beforeNameTV = findViewById<TextView>(R.id.beforeNameTV)
        beforeNameTV.setText(menuData.product)

        var beforePriceTV:TextView = findViewById(R.id.beforePriceTV)
        beforePriceTV.setText(menuData.price.toString())

        var beforeExpTV:TextView = findViewById(R.id.beforeExpTV)
        beforeExpTV.setText(menuData.product_exp)


        var beforeIngRV:RecyclerView = findViewById(R.id.beforeIngRV)


        var RVAdapter1 = BeforeIngRVAdapter(context, menuData)
        beforeIngRV.adapter = RVAdapter1

        var LM1 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        beforeIngRV.layoutManager=LM1
        beforeIngRV.setHasFixedSize(true)

        beforeIngRV.setOnClickListener {
            //Log.d("확인 MenuEditDialog","리사이클러뷰 클릭시")
        }

        beforeChangeImage =findViewById(R.id.beforeChangeImage)

        afterChangeImage =findViewById(R.id.afterChangeImage)
        setImgOnView()
        //afterChangeImage.setImageResource()

        //var imageRes:Int=beforeImgRes
        var imgUrl=""
        //afterChangeImage.setImageResource(imageRes)
        afterChangeImage.setOnClickListener {
            //갤러리에서 이미지 불러오는 코드
            //imageRes에 변경할 이미지 저장
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            sikdangmainRes.startActivityForResult(intent, 1)



        }



        var afterIngRV :RecyclerView = findViewById(R.id.afterIngRV)

        RVAdapter2 = AfterIngRVAdapter(context, this)
        afterIngRV.adapter = RVAdapter2

        var LM2 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        afterIngRV.layoutManager=LM2
        afterIngRV.setHasFixedSize(true)



        var meEditIngBtn:Button = findViewById(R.id.meEditIngBtn)
        meEditIngBtn.setOnClickListener {
            showIngEditDialog()
        }

        afterIngRV.setOnClickListener {
            //재료 다이얼로그 출력
            //Log.d("확인 MenuEditDialog","리사이클러뷰 클릭시")
            showIngEditDialog()
        }


        var afterNameET:EditText=findViewById(R.id.afterNameET)
        afterNameET.setText(menuData.product)

        var afterPriceET:EditText=findViewById(R.id.afterPriceET)
        afterPriceET.setText(menuData.price.toString())

        var afterExpET:EditText=findViewById(R.id.afterExpET)
        afterExpET.setText(menuData.product_exp)


        var meChangeBtn:Button = findViewById(R.id.meChangeBtn)
        meChangeBtn.setOnClickListener {
            //afterNameET.text : 변경된 이름
            //afterPriceET.test : 변경된 가격
            //afterExpET : 변경된 메뉴 설명
            //afterIngAL _ingredient 의 ArrayList : 변경된 재료
            //imageRes : 변경할 메뉴 이미지
            Log.d("확인  MenuEditDialog.menuChange() : 변경버튼클릭 ", "1")
            menuChange(afterNameET.text.toString(), sikdangmainRes.newMenuImgUri.toString(), afterPriceET.text.toString().toInt(), afterExpET.text.toString(), afterIngAL)
            this.dismiss()
        }

        var meDeleteBtn:Button = findViewById(R.id.meDeleteBtn)
        meDeleteBtn.setOnClickListener {
            //데이터베이스에서 이 메뉴 삭제
            deleteMenu()
        }



        var meCancelBtn: Button = findViewById(R.id.meCancelBtn)
        meCancelBtn.setOnClickListener { this.dismiss() }




    }






    public fun setNewImg(){
        isnewImageSetted=true
        if(sikdangmainRes.sikdangimgCheckNum==1){
            Glide.with(context)
                    .load(sikdangmainRes.newMenuImgUri)
                    .apply(RequestOptions())
                    .into(afterChangeImage)
        }

    }


    //이미지뷰에 기존의 이미지 넣음

    public fun setImgOnView(){
        Log.d("확인  setImgOnView()", "1")


        if((editMenuDialog.menuDataAL[menuNum].image_url == "")||editMenuDialog.menuDataAL[menuNum].image_url == "Null") {
            var beforeImgRes = R.drawable.foodimage
            beforeChangeImage.setImageResource(beforeImgRes)
            afterChangeImage.setImageResource(beforeImgRes)
        }
        else{
            Glide.with(context)
                    .load(editMenuDialog.menuDataAL[menuNum].image_url)
                    .apply(RequestOptions())
                    .into(beforeChangeImage)
            Glide.with(context)
                    .load(editMenuDialog.menuDataAL[menuNum].image_url)
                    .apply(RequestOptions())
                    .into(afterChangeImage)
        }





    }






    fun closeKeyBoard(){
        var view = this.currentFocus
        if (view != null){
            //var act = activity as SikdangMain_res
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }
    }

    private fun showIngEditDialog(){
        var customDialog = IngEditDialog(context, sikdangNum, menuNum, this, editMenuDialog)
        customDialog!!.show()

    }

    public fun renewalAfterIngRV(){
        RVAdapter2.notifyDataSetChanged()
    }


    private fun menuChange(newName: String, imgUrl: String, newPrice: Int, newEXP: String, newIng: ArrayList<_Ingredient>){
        Log.d("확인  MenuEditDialog.menuChange() : editMenuDialog.ingKeyAL.size ", "시작")
        //var newMenu = MenuData(newName, imgUrl, newPrice, newEXP, newIng)
        var sRef = FirebaseStorage.getInstance().getReference()

        var check1=false
        var check2 = false
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("menu").child(editMenuDialog.menuKeyAL[menuNum])
        ref.child("product").setValue(newName)
        ref.child("price").setValue(newPrice)
        ref.child("product_exp").setValue(newEXP)
        ref.child("image").setValue("Null")

        if (isnewImageSetted== true) setNewImgOnDB()

        Log.d("확인  MenuEditDialog.menuChange() : editMenuDialog.ingKeyAL.size ", editMenuDialog.ingKeyAL[menuNum].size.toString())

        //for(i in 0..editMenuDialog.deleteIngAL.size-1)

        for (i in 0..editMenuDialog.ingKeyAL[menuNum].size-1){
            Log.d("확인  MenuEditDialog.menuChange() : 이쓴ㄴ 재료 넣음 ", editMenuDialog.ingKeyAL[menuNum].size.toString())
            ref.child("ingredients").child(editMenuDialog.ingKeyAL[menuNum][i]).child("ing").setValue(newIng[i].ing)
            ref.child("ingredients").child(editMenuDialog.ingKeyAL[menuNum][i]).child("country").setValue(newIng[i].country)
            if(i == editMenuDialog.ingKeyAL.size-1){
                check1 = true
                if (check2 == true){
                    editMenuDialog.getMenuDataOnDB()
                }
            }
        }
        Log.d("확인  MenuEditDialog.menuChange() : editMenuDialog.newIng.size ", newIng.size.toString())

        var k = editMenuDialog.ingKeyAL[menuNum].size
        while (k < newIng.size){
            Log.d("확인  MenuEditDialog.menuChange() : 재료 추가", newIng.size.toString())
            var pushRef = ref.child("ingredients").push()
            pushRef.child("ing").setValue(newIng[k].ing)
            pushRef.child("country").setValue(newIng[k].country)
            if(k == newIng.size-1){
                check2 = true
                if (check1 == true){
                    editMenuDialog.getMenuDataOnDB()
                }
            }
            k+=1
        }




        //newData를 데이터베이스로 보내서 수정
    }



    public fun setNewImgOnDB(){
        var newUrl = ""
        val storageRef = FirebaseStorage.getInstance().getReference()
        var file = sikdangmainRes.newMenuImgUri
        val riversRef = storageRef.child(sikdangmainRes.sikdangName + "/" + editMenuDialog.menuDataAL[menuNum].product+".jpg")
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
                .child("Restaurants").child(sikdangmainRes.sikdangType).child(sikdangmainRes.sikdangId).child("menu").child(editMenuDialog.menuKeyAL[menuNum]).child("image")
        //Log.d("확인 upUrlOnDB()", sikdangmainRes.sikdangId)
        //Log.d("확인 upUrlOnDB()", newUrl.toString())
        ref.setValue(newUrl)
    }



    private fun deleteMenu(){
        //이 메뉴 삭제
    }
}