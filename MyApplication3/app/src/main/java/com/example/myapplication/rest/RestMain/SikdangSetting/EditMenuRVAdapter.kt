package com.example.myapplication.rest.RestMain.SikdangSetting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication._Ingredient
import com.example.myapplication.bookActivity.MenuData
import com.example.myapplication.rest.Resmain.SikdangMain_res

//EditMenuDialog 에서 사용
//메뉴 여러개 리사이클러뷰에 넣어줌
class EditMenuRVAdapter(var context: Context, val sikdangNum:String, var sikdangmainRes: SikdangMain_res, var editMenuDialog: EditMenuDialog): RecyclerView.Adapter<EditMenuRVAdapter.Holder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.res_menuline, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        //메뉴 개수
        //Log.d("확인 EditMenuRVAdapter", menuDataAL.size.toString())
        //Log.d("확인 바인드 될 메뉴 라인 수",editMenuDialog.menuDataAL.size.toString())
        return editMenuDialog.menuDataAL.size
        //return 2
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        var menuImage : ImageView = itemView.findViewById(R.id.ml_imageView)
        var menuName : TextView = itemView.findViewById(R.id.ml_menuNameTV)
        var menuExp : TextView = itemView.findViewById(R.id.ml_menuExpTV)
        var menuPrice : TextView = itemView.findViewById(R.id.ml_menuPriceTV)
        var menuIng : TextView = itemView.findViewById(R.id.ml_ingTV)

        public fun bind(pos:Int){
            //Log.d("확인 BeforeIngRVAdapter","###############################")
            if(editMenuDialog.menuDataAL[pos].image_url == ""){menuImage.setBackgroundResource(R.drawable.food_placeholder)}
            else{
                Glide.with(context)
                        .load(editMenuDialog.menuDataAL[pos].image_url)
                        .apply(RequestOptions())
                        .into(menuImage)
            }

            menuName.setText(editMenuDialog.menuDataAL[pos].product)
            menuExp.setText(editMenuDialog.menuDataAL[pos].product)
            menuPrice.setText(editMenuDialog.menuDataAL[pos].price.toString()+" 원")
            var ing=""
            for (i in 0 until editMenuDialog.menuDataAL[pos].ingredients.size){
                ing +=editMenuDialog.menuDataAL[pos].ingredients[i].ing
                ing+=":"
                ing +=editMenuDialog.menuDataAL[pos].ingredients[i].country
                ing += " "
            }
            menuIng.setText(ing)

            itemView.setOnClickListener {
                showMenuEditDialog(pos)
            }
        }
    }



    public fun showMenuEditDialog(pos:Int){
        var customDialog = MenuEditDialog(context, sikdangNum, pos, editMenuDialog.menuDataAL[pos], editMenuDialog, sikdangmainRes)
        customDialog!!.show()
    }


}