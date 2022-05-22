package com.example.myapplication.mainPage

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.sikdangChoicePage.SikdangChoice
import kotlin.jvm.internal.Intrinsics
//Sikdang_main 에서 사용 카테고리 리스트와 거리가 적힌 에딧텍스트 받는다
class SikdangMainCatAdapter(var context: Context, val catArrayList: ArrayList<String>, val distText : EditText) : RecyclerView.Adapter<SikdangMainCatAdapter.Holder>() {
    var arrayMax = catArrayList.size
    override fun  onCreateViewHolder(parent: ViewGroup, viewType: Int):SikdangMainCatAdapter.Holder{
        val view = LayoutInflater.from(context).inflate(R.layout.cat_line, parent, false)
        return Holder(view)

    }



    override fun getItemCount(): Int {
        //if ((catArrayList.size) % 2) ==0)
        return (((catArrayList.size))/2).toInt()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(catArrayList[position],catArrayList[(arrayMax/2)+position])
        //holder.bind(catArrayList[position*2],catArrayList[position*2 +1])
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    //버튼업과 버튼 다운을 똑같이 만들어줘야함
    inner class Holder (itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        fun bind(item1:String, item2:String){
            Log.d("확인 SikdangMinCatAdapter", "Holder.bind")
            var catButtonUp : Button = itemView.findViewById(R.id.cat_button_up)
            catButtonUp.setText(item1)
            catButtonUp.setOnClickListener {
                //광고페이지 클래스를 호출해야 함

                val intent :Intent
                //리사이클러 뷰 어댑터 내에서 intent 생성 좀 다르게
                intent=Intent(itemView.context, SikdangChoice::class.java)

                //intent.putExtra("pos", 1)
                //var a = catButtonUp.getTag()
                intent.putExtra("cat", CatList.getInstance().indexOf(item1))
                var dist = 0
                if (distText.getText().toString().length == 0 ){
                    dist = 1000
                }
                else{
                    dist = distText.getText().toString().toInt()
                }

                intent.putExtra("dist", dist)

                context.startActivity(intent)
            }


            var catButtonDown : Button = itemView.findViewById(R.id.cat_button_down)
            catButtonDown.setText(item2)
            catButtonDown.setOnClickListener {
                //광고페이지 클래스를 호출해야 함
                val intent :Intent
                intent=Intent(itemView.context, SikdangChoice::class.java)
                intent.putExtra("cat", CatList.getInstance().indexOf(item2))
                intent.putExtra("catArrayList", catArrayList)
                var dist = 0
                if (distText.getText().toString().length == 0 ){
                    dist = 0
                }
                else{
                    dist = distText.getText().toString().toInt()
                }

                intent.putExtra("dist", dist)
                context.startActivity(intent)
            }

        }

    }


}


