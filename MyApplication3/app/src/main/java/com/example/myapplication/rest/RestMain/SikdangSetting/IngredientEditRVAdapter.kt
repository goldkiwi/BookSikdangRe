package com.example.myapplication.rest.RestMain.SikdangSetting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication._Ingredient

//IngEditDialog 에서 사용
//재료 수정하는 다이얼로그에 재료와 연산지 들어간 에딧 텍스트 두개 한ㅁ 줄에 바인드
class IngredientEditRVAdapter(var context: Context, var menuNum:Int,  var ingEditDialog: IngEditDialog, var editMenuDialog: EditMenuDialog): RecyclerView.Adapter<IngredientEditRVAdapter.Holder>() {
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
        //Log.d("확인 IngredientEditRVAdapter","라인수"+ingEditDialog.changedIng.size.toString())
        return ingEditDialog.changedIng.size
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        public fun bind(pos:Int){
            //Log.d("확인 IngredientEditRVAdapter.Holder.bind",pos.toString()+" 시작")
            countAL.add(pos)


            var ie_ingET:EditText = itemView.findViewById(R.id.ie_ingET)
            ie_ingET.setText(ingEditDialog.changedIng[pos].ing)
            ingETAL.add(ie_ingET)
            tempingETAL.add(ie_ingET)
            //ingETAL[pos] = ie_ingET



            var ie_countryET:EditText = itemView.findViewById(R.id.ie_countryET)
            ie_countryET.setText(ingEditDialog.changedIng[pos].country)
            countryETAL.add(ie_countryET)
            tempcountryETAL.add(ie_countryET)
            //countryETAL[pos] = ie_countryET

            var ingLineXBTN:TextView=itemView.findViewById(R.id.ingLineXBTN)
            ingLineXBTN.setOnClickListener {
                deleteIng(pos)
            }

            //Log.d("확인 IngredientEditRVAdapter.Holder.bind",pos.toString()+" "+ingETAL.size.toString()+ingEditDialog.changedIng[pos].ing + ingEditDialog.changedIng.size+count.toString())
            count++
            if (count ==ingEditDialog.changedIng.size){
                setCorrectPos()
                count = 0
            }

        }
    }

    //현재까지 작성된 재료 정보 모두 다이얼로그의 배열에 저장장
   public fun setIng(){
        for (i in 0..ingETAL.size-1){
            //Log.d("확인 IngredientEditRVAdapter.setIng()", i.toString()+ingETAL.size.toString()+ingETAL[i].text.toString())
            var newIng = _Ingredient(ingETAL[i].text.toString(), countryETAL[i].text.toString())
            ingEditDialog.changedIng[i]=newIng
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

        //editMenuDialog.deleteIngAL.add(editMenuDialog.ingKeyAL[menuNum][pos])
        //Log.d("확인  MenuEditDialog.menuChange() : editMenuDialog.newIng.size ", newIng.size.toString())
        //editMenuDialog.ingKeyAL[menuNum].remove(editMenuDialog.ingKeyAL[menuNum][pos])

        ingEditDialog.changedIng.remove(ingEditDialog.changedIng[pos])
        ingEditDialog.renewalRV()
    }

    /*
    public fun setETText(){
        for (i in 0..ingETAL.size-2){
            ingETAL[i].setText(ingEditDialog.changedIng[i].ing)
            countryETAL[i].setText(ingEditDialog.changedIng[i].country)
        }
    }*/

    public fun setCorrectPos(){

        //Log.d("확인 IngredientEditRVAdapter.setCorrectPos()",ingETAL.size.toString()+" "+countAL.size+" 시작")

        /*
        for (i in 0..countAL.size-1){
            Log.d("확인 IngredientEditRVAdapter.setCorrectPos()","i: "+ i.toString()+" countAL[i]:"+countAL[i].toString()+" tempingETAL[i]:"+tempingETAL[i].text.toString())
        }*/

        for (i in 0..countAL.size-1){
            //Log.d("확인 IngredientEditRVAdapter.setCorrectPos()"," 내부"+i.toString()+" "+countAL[i].toString())
            ingETAL[countAL[i]] = tempingETAL[i]
            /*
            for (i in 0..countAL.size-1){
                Log.d("확인 IngredientEditRVAdapter.setCorrectPos()",i.toString()+" : "+ingETAL[i].text.toString() +"중간")
            }*/
            countryETAL[countAL[i]] = tempcountryETAL[i]
        }

        /*
        for (i in 0..countAL.size-1){
            Log.d("확인 IngredientEditRVAdapter.setCorrectPos()",ingETAL[i].text.toString() +"끝")
        }*/

    }

}