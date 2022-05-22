package com.example.myapplication.dataclass

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class StoreInfo(
        val store_image : String? = "",
        val phone_number : String? = "",
        val store_id : String?="",
        val store_name : String? = "",
        val store_type : String? = ""
) : Serializable
