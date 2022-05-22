package com.example.sikdangbook_rest.Table

import com.example.myapplication.bookActivity.Table
import java.io.Serializable



//TableStateIsBookedDialog_res 와 TableStateNotBookedDialog_res, TableFloorFragment_res 에서 생성
//시간대에 따라 다르게 불러와야 함
class TableData_res():Serializable {
    //가로좌표 세로좌표 가로길이 세로길이





    var tableList = ArrayList<Table_res>()//각 테이블 정보 담긴 리스트
    var floorList = ArrayList<Int>()//식당 각 몇층인지 1층과 3층이 있으면 1, 3 의 값을 갖는다.
    var tableNumList = ArrayList<Int>()//각 층에 테이블 몇개인지
    var accumTableNumList = ArrayList<Int>()//테이블 개수 축적



    init{
        setData()
    }
    //여기서 DB 접근 현재는 임시데이터
    //tableList 만 채워주면 된다
    private fun setData(){

        /*
        floorList.add(1)
        floorList.add(3)

        tableList.add(Table_res(0.5F, 0.3F, 50, 50, 2, 1, true, true))
        tableList.add(Table_res(0.6F, 0.3F, 50, 50, 2, 1, false, true))
        tableList.add(Table_res(0.7F, 0.3F, 30, 30, 4, 1, false, true))
        tableList.add(Table_res(0.2F, 0.6F, 60, 30, 6, 1, false, false))
        tableList.add(Table_res(0.35F, 0.6F, 60, 30, 3, 1, true, false))

        tableList.add(Table_res(0.3F, 0.3F, 50, 50, 2, 3, true, true))
        tableList.add(Table_res(0.3F, 0.4F, 50, 50, 2, 3, false, true))
        tableList.add(Table_res(0.5F, 0.4F, 30, 30, 4, 3, false, true))
        tableList.add(Table_res(0.2F, 0.6F, 60, 30, 6, 3, false, false))
        tableList.add(Table_res(0.35F, 0.6F, 60, 30, 3, 3, true, false))
        tableList.add(Table_res(0.35F, 0.75F, 60, 30, 3, 3, false, false))
        setFloorTable()*/

    }
    //층별 테이블 수 계산
    private fun setFloorTable(){
        var i = 0
        var table = 0
        var floor = floorList[0]
        while(i < tableList.size){
            if (tableList[i].floor > floor){
                tableNumList.add(table)
                table = 0
                floor = tableList[i].floor
            }
            table ++
            i++
        }
        tableNumList.add(table)

        i = 0
        accumTableNumList.add(tableNumList[0])
        //Log.d("확인 tableAccum", accumTableNumList[i].toString())
        while(i < tableNumList.size-1){
            accumTableNumList.add(accumTableNumList[i]+tableNumList[i+1])
            i++
            //Log.d("확인 tableAccum", accumTableNumList[i].toString())
        }

    }








}