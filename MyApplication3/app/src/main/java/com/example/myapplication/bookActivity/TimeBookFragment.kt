package com.example.myapplication.bookActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo

class TimeBookFragment(val bookTime: BookTime) : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var timeLineAdapter : TimeLineAdapter
    private lateinit var sikdangId : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.booktime_fragment, container, false)
        sikdangId = (activity as BookActivity).storeInfo.store_id!!
        Log.d("확인 TimeBookFragment", sikdangId)

        recyclerView = view.findViewById(R.id.bookTimeRV2)
        recyclerView.setHasFixedSize(true)
        timeLineAdapter = TimeLineAdapter(context, bookTime, sikdangId, activity as BookActivity)
        val GridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = GridLayoutManager
        recyclerView.adapter = timeLineAdapter

        return view
    }

}
