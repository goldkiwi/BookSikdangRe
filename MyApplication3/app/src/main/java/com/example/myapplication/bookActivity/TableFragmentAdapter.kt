package com.example.myapplication.bookActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class TableFragmentAdapter(
        fa : FragmentActivity,
        val tableData : TableData
) : FragmentStateAdapter(fa)
{
    override fun getItemCount(): Int {
        return tableData.floor_list.size
    }

    override fun createFragment(position: Int): Fragment {
        val floor_name = tableData.floor_list[position]
        val tableInfo = tableData.current_table_list[floor_name]
        var tableFloorFragment = TableFloorFragment(floor_name, tableInfo!!)

        return tableFloorFragment
    }
}