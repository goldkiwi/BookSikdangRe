package com.example.sikdangbook_rest.Table

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.rest.Resmain.SikdangMain_res

//SikdangMain_res 에서 사용
class TableFloorVPAdapter_res(fa:FragmentActivity, val sikdangmainRes: SikdangMain_res): FragmentStateAdapter(fa) {
    //가계 층수

    var tableFloorFragmentAL = ArrayList<TableFloorFragment_res>()
    override fun getItemCount(): Int {
        return sikdangmainRes.floorList.size
    }

    override fun createFragment(position: Int): Fragment {
        var tableFloorFragment = TableFloorFragment_res(position, sikdangmainRes,sikdangmainRes.getTimeNum())
        tableFloorFragmentAL.add(tableFloorFragment)
        return tableFloorFragment
    }

    public fun setImageByUrl(){
        for (i in 0..sikdangmainRes.floorList.size-1){
            for (j in 0..sikdangmainRes.floorUrlAL.size-1){
                if (sikdangmainRes.floorList[i] == sikdangmainRes.floorUrlAL[j].floor){
                    tableFloorFragmentAL[i].setFloorImage(sikdangmainRes.floorUrlAL[j].url)
                }
            }
        }
    }


    public fun bind(){
        val time = System.currentTimeMillis()
        //val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        //val curTime = dateFormat.format(Date(time))
    }
}