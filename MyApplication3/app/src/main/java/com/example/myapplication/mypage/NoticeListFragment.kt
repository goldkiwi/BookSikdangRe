package com.example.myapplication.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_notification.*

class NoticeListFragment (var listner: NotificationActivity) : Fragment() {
    lateinit var noticeHeaderList : ArrayList<NoticeHeader>
    lateinit var rv_notice : RecyclerView
    lateinit private var noticeAdapter : NoticeAdapter
    private lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ref = FirebaseDatabase.getInstance()
            .getReference("Notifications")
            .child(listner.getUserId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notice_list, container, false)
        listner.title.text = "공지사항"
        listner.finish.setOnClickListener {
            listner.finish()
        }

        noticeHeaderList = arrayListOf()
        listner.loading.visibility = View.VISIBLE

        rv_notice = view.findViewById(R.id.rv_notice)

        noticeAdapter = NoticeAdapter(requireContext(), noticeHeaderList, listner)
        rv_notice.adapter = noticeAdapter
        rv_notice.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        getNotificationFromDB()
        return view
    }

    fun getNotificationFromDB() {
        FirebaseDatabase.getInstance().getReference("Notifications")
            .child(listner.getUserId())
            .child("Message_Header")
            .orderByChild("date")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noticeHeaderList.clear()
                    for (data in snapshot.children) {
                        var notice = data.getValue(NoticeHeader::class.java)
                        if (notice == null) continue
                        notice.is_read = data.child("is_read").getValue(Boolean::class.java)
                        noticeHeaderList.add(notice)
                    }

                    requireActivity().runOnUiThread {
                        listner.loading.visibility = View.GONE
                    }
                    noticeHeaderList.reverse()
                    noticeAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}