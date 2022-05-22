package com.example.myapplication.mainPage

import java.util.*
import kotlin.collections.ArrayList

data class CatorySet(
        var catory_name : String,
        var catory_list : ArrayList<String>
) {
    companion object {
        val CatoryDataSet = arrayListOf<CatorySet>(
                CatorySet("방식", arrayListOf("한식", "중식", "양식", "일식", "분식")),
                CatorySet("재료", arrayListOf("돼지", "소", "닭", "면")),
                CatorySet("맛", arrayListOf("매운맛", "짠맛", "쓴맛", "고소한맛")),
                CatorySet("데이트코스", arrayListOf("카페", "감성", "시네마")),
                CatorySet("TV 맛집", arrayListOf("수요미식회", "SBS 3대 천왕", "맛있는 녀석들"))
        )
    }
}