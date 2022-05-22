package com.example.myapplication.mypage

import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalDate

@IgnoreExtraProperties
data class NoticeHeader (
    val message_id : String? = null,
    val title : String? = null,
    val date : String? = null,
    var is_read : Boolean? = null
)

data class NoticeMessage (
        val title : String? = null,
        val message_header : String? = null,
        val message : String? = null,
        val message_image : String? = null,
        val message_tail : String? = null
)