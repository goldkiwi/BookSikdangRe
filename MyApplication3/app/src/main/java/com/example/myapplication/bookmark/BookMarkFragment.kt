package com.example.myapplication.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookMarkFragment : Fragment() {
    lateinit var cnt_bookmark : TextView

    lateinit var rv_book_mark : RecyclerView
    lateinit var bookMarkAdapter: BookMarkAdapter
    lateinit var user_id : String

    var store_list : ArrayList<StoreInfo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.bookmark, container, false)
        init_UI(view)
        user_id = FirebaseAuth.getInstance().uid!!
        getFromMyBookMark()
        return view
    }

    private fun init_UI(view : View) {
        cnt_bookmark = view.findViewById(R.id.cnt_bookmark)
        rv_book_mark = view.findViewById(R.id.bookMarkListRV)
        bookMarkAdapter = BookMarkAdapter(requireContext(), store_list)
        rv_book_mark.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_book_mark.adapter = bookMarkAdapter
    }

    private fun getFromMyBookMark() {
        FirebaseDatabase.getInstance().getReference("BookMark")
            .child(user_id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    store_list.clear()
                    for (data in snapshot.children) {
                        val store = data.getValue(StoreInfo::class.java)
                        if(store == null) continue
                        store_list.add(store)
                    }
                    val cnt = snapshot.childrenCount
                    cnt_bookmark.text = "총 ${cnt}개"
                    bookMarkAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
}