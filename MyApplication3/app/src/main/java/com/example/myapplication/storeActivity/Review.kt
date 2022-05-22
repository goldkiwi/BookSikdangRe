package com.example.myapplication.storeActivity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Review(
    val user_name : String? = null,
    val date : String? = null,
    val image : String? = null,
    val comment : String? = null,
    val rating : Int? = null,
    val recomment : String? = null
)


data class ReviewData(
    val cnt_owner : Int? = 0,
    val cnt_user : Int? = 0,
    val rating : Int? = 0
)