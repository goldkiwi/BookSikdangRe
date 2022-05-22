package com.example.myapplication.sikdangChoicePage

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

//식당 위치 관련 클래스
//SikdangMenuData 에 데이터 전하는 용도의 클래스
//통째로 SikdangMenuData 객체로 넘긴다
//SikdsngChoice 에서 생성되어 maxDist 넣어줌
//SikdangChoiceMenuViewPagerAdapter 클래스에서 cat 채우고 ->  SikdangChoiceMenuFragment -> SikdangChoiceMenuAdapter 에서 pos 채우고
//마지막으로 SikdangMenuData 클래스 생성하면서 넘긴다.
//CoorXT는 언제넣을지 아직 미정
//여기에 식당 목록 몇개인지도 넣어서 중간에 다른 객체로 넘길 용도로 추가하는것도 괜찮을듯


class SikdangStoreMenu(
        val Lat : Double,
        val Lng : Double,
        val id : String,
        val name : String,
        val dist : Int,
        val store_image: String? = null,
        val store_type: String
) {

}

@IgnoreExtraProperties
data class SikdangReqMenu(
    val Lat : Double? = null,
    val Lng : Double? = null,
    val id : String? = null,
    val name : String? = null,
    val store_type : String? = null,
    val store_image: String? = null
)
