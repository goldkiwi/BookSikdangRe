package com.example.myapplication.rest.Resmain


import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SikdangId(
        val sikdangId : String? = null,
        val store_type : String? = null


){
    /*
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "sikdangId" to sikdangId,
                "store_type" to store_type
        )
    }*/
}


/*
class SikdangId {
}*/