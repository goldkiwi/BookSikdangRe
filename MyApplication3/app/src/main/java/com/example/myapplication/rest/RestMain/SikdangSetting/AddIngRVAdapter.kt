package com.example.myapplication.rest.RestMain.SikdangSetting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication._Ingredient

//IngAddDialog 에서 사용
//재료들 바인드
class AddIngRVAdapter(var context: Context, var ingAddDialog: IngAddDialog): RecyclerView.Adapter<AddIngRVAdapter.Holder>() {

    var ingETAL = ArrayList<EditText>()
    var countryETAL = ArrayList<EditText>()

    var tempingETAL = ArrayList<EditText>()
    var tempcountryETAL = ArrayList<EditText>()

    var countAL = ArrayList<Int>()
    var count = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.res_ing_line, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return ingAddDialog.changedIng.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){

            countAL.add(pos)


            var ie_ingET: EditText = itemView.findViewById(R.id.ie_ingET)
            ie_ingET.setText(ingAddDialog.changedIng[pos].ing)
            ingETAL.add(ie_ingET)
            tempingETAL.add(ie_ingET)
            //ingETAL[pos] = ie_ingET



            var ie_countryET: EditText = itemView.findViewById(R.id.ie_countryET)
            ie_countryET.setText(ingAddDialog.changedIng[pos].country)
            countryETAL.add(ie_countryET)
            tempcountryETAL.add(ie_countryET)
            //countryETAL[pos] = ie_countryET

            var ingLineXBTN: TextView =itemView.findViewById(R.id.ingLineXBTN)
            ingLineXBTN.setOnClickListener {
                deleteIng(pos)
            }

            //Log.d("확인 IngredientEditRVAdapter.Holder.bind",pos.toString()+" "+ingETAL.size.toString()+ingEditDialog.changedIng[pos].ing + ingEditDialog.changedIng.size+count.toString())
            count++
            if (count ==ingAddDialog.changedIng.size){
                setCorrectPos()
                count = 0
            }


        }

    }

    public fun setIng(){
        for (i in 0..ingETAL.size-1){
            //Log.d("확인 IngredientEditRVAdapter.setIng()", i.toString()+ingETAL.size.toString()+ingETAL[i].text.toString())
            var newIng = _Ingredient(ingETAL[i].text.toString(), countryETAL[i].text.toString())
            ingAddDialog.changedIng[i]=newIng
        }
        clearAL()

    }
    public fun clearAL(){

        ingETAL.clear()
        countryETAL.clear()
        tempingETAL.clear()
        tempcountryETAL.clear()
        countAL.clear()
        //Log.d("확인 IngredientEditRVAdapter","AL 클리어"+ingETAL.size.toString())
    }

    public fun deleteIng(pos:Int){
        //Log.d("확인 IngredientEditRVAdapter.deleteIng()", pos.toString()+" 삭제 "+ingETAL[pos].text.toString())
        setIng()
        ingAddDialog.changedIng.remove(ingAddDialog.changedIng[pos])
        ingAddDialog.renewalRV()
    }

    public fun setCorrectPos(){

        for (i in 0..countAL.size-1){
            ingETAL[countAL[i]] = tempingETAL[i]
            countryETAL[countAL[i]] = tempcountryETAL[i]
        }

    }
}