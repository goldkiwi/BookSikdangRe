package com.example.myapplication.bookActivity

import android.util.Log

class TableData(
        val table_map : HashMap<String, HashMap<String, Table>>,
        val table_booked : HashMap<String, HashMap<String, Boolean>>,
        val time : String)
{
    var floor_list : ArrayList<String>
    var current_table_list : HashMap<String, ArrayList<Table>>

    init {
        floor_list = ArrayList(table_booked.keys)
        floor_list.sortedBy { it }
        current_table_list = hashMapOf()
        for (floor_name in floor_list) {
            current_table_list.put(floor_name, ArrayList(table_map[floor_name]!!.values))

/*
            for (table in current_table_list[floor_name]!!) {
                Log.d("TableFragment", "init -> ${floor_name} : ${table.id}")
                Log.d("TableFragment", "init -> ${floor_name} : ${table.id} , ${table_booked[floor_name]!![table.id]}")
            }
 */
            for (table in current_table_list[floor_name]!!) {
                table.isBooked = table_booked[floor_name]!![table.id] == true
            }
/*
            Log.d("TableFragment", "stop1 ${floor_name} : ${table_booked[floor_name]}")
            Log.d("TableFragment", "stop2 ${floor_name} : ${current_table_list[floor_name]}")
            for (table in current_table_list[floor_name]!!) {
                Log.d("TableFragment", "${floor_name} :${table.id} ${table.isBooked}")
            }
 */
        }
    }
}
