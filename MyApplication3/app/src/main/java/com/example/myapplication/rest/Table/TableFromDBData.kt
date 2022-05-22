package com.example.myapplication.rest.Table

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class TableFromDBData(
        val capacity : Int? = null,
        val height : Int? = null,
        val width : Int?=null,
        val shape : String?=null,
        val x : Float?=null,
        val y : Float?=null){

        fun toMap(): Map<String, Any?> {
            return mapOf(
                    "capacity" to capacity,
                    "height" to height,
                    "width" to width,
                    "shape" to shape,
                    "x" to x,
                    "y" to y

            )
        }
}
