package com.example.myapplication.mainPage

import android.util.Log

class TagLineList(){

    constructor(tagArray: Array<String>):this(){
        Log.d("확인 TagLineList constructor 호출", "호출 되면 작동이 해야하는데")
        setTagList(tagArray)
        Log.d("확인 TagLineList constructor 종료", "종료후에 다른것이 호출되는가?")
    }

    var tagList:ArrayList<TagLine> = arrayListOf(
            TagLine("소고기", "닭고기", "돼지고기"),
            TagLine("매운맛", "달콤한맛", "구수한맛"),
            TagLine("짠맛", "뜨거운것", "시원한것"),
            TagLine("생선", "새우", "조개"),
            TagLine("소주", "맥주", "막걸리"),
            TagLine("데이트", "단체", "혼밥")
            //TagLine("test1", "test2", "test3")
    )
    //var tagList = arrayOfNulls<TagLine?>(100)
    var arrayMax=18
    fun getTagLineList(): ArrayList<TagLine> {
        return tagList;
    }
    private fun setTagList(tagArray: Array<String>){
        arrayMax=tagArray.size
        var i = 0
        var j = 0
        tagList.clear()
        var tempString = arrayMax.toString()
        while(i<arrayMax){
            var tempI = i.toString()
            tempI+=" "
            tempI+=tempString
            //Log.d("확인 TagLineList.setTagLine while문 반복", tempI)
            if ((i+2)<arrayMax){
                var tagLine = TagLine(tagArray[i], tagArray[i+1], tagArray[i+2])
                tagList.add(tagLine)
                j++
                i+=3
            }
            else if ((i+2) == arrayMax){
                var tagLine = TagLine(tagArray[i], tagArray[i+1], "졸려")
                tagList.add(tagLine)
                j++
                i+=3
            }
            else if ((i+1) == arrayMax){
                var tagLine = TagLine(tagArray[i], "하기 싫어", "졸려")
                tagList.add(tagLine)
                j++
                i+=3
            }
            else{
                Log.d("TagLineList.setTagLine 오류", "선택지 오류")
            }
        }
    }


}