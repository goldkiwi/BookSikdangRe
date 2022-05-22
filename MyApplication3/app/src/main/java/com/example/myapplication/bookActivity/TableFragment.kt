package com.example.myapplication.bookActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timer

class TableFragment(val table_meta: HashMap<String, HashMap<String, Table>>,
                    val table_booked : HashMap<String, HashMap<String, Boolean>>,
                    val selected_time : String) : Fragment()
{
    lateinit var tableData: TableData
    lateinit var view_pager : ViewPager2
    lateinit var table_fragment_adapter : TableFragmentAdapter
    lateinit var selectedTables : HashMap<String, ArrayList<Table>>
    lateinit var tableCompleteButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.booktable_fragment, container, false)
        // Log
        tableData = TableData(table_meta, table_booked, selected_time)
        (requireActivity() as BookActivity).selected_time = selected_time
        for (floor_name in tableData.floor_list) {
            Log.d("TableFragment", "${tableData.current_table_list[floor_name]}")
        }

        view_pager = view.findViewById(R.id.tableVP)
        table_fragment_adapter = TableFragmentAdapter(this.requireActivity(), tableData)
        view_pager.adapter = table_fragment_adapter

        tableCompleteButton = view.findViewById(R.id.tableCompleteButton)
        tableCompleteButton.setOnClickListener {
            resultForOrder()
        }
        return view
    }

    fun resultForOrder() {
        selectedTables = hashMapOf()
        for (floor_fragment in parentFragmentManager.fragments) {
            if (floor_fragment is TableFloorFragment) {
                val result_item = floor_fragment.tableSelectedByUser
                val floor_name = floor_fragment.floor_name

                if(result_item.isEmpty()) continue

                var items = ArrayList(result_item.values)
                items.sortWith(compareBy { it.id })
                selectedTables.put(floor_name, items)
            }
        }
        if(selectedTables.isEmpty()) {
            Toast.makeText(context, "1개 이상 예약을 해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.bookFragment, MenuFragment(selectedTables))
                .commit()
    }
}