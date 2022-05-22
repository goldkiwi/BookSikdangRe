package com.example.myapplication.bookActivity

import com.example.myapplication._Table
import java.io.Serializable

class Table (
        val id : String,
        val x : Float,
        val y : Float,
        val width : Int,
        val height : Int,
        val capacity : Int,
        val shape : String,
        val floor : String,
        var isBooked : Boolean? = null
) : Serializable {
    constructor(id : String, floor: String ,_table: _Table) :
            this(   id,
                    _table.x!!.toFloat(),
                    _table.y!!.toFloat(),
                    _table.width!!,
                    _table.height!!,
                    _table.capacity!!,
                    _table.shape!!,
                    floor,
                    null
            )

    fun setBookStatus(booked: Boolean) {
        isBooked = booked
    }
}