package com.example.myapplication.storeActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewFragment(var storeInfo: StoreInfo) : Fragment() {
    lateinit var write_review : TextView

    lateinit var rv_review : RecyclerView
    lateinit var review_list : ArrayList<Review>
    lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)
        init_UI(view)
        getFromDB()
        return view
    }

    private fun init_UI(view : View) {
        write_review = view.findViewById(R.id.write_review)
        write_review.setOnClickListener {
            var _intent = Intent(requireActivity(), EditReviewActivity::class.java)
            _intent.putExtra("store_info", storeInfo)
            startActivity(_intent)
        }

        rv_review = view.findViewById(R.id.rv_review)
        review_list = arrayListOf()
        reviewAdapter = ReviewAdapter(requireContext(), review_list)
        val linearLayout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_review.adapter = reviewAdapter
        rv_review.layoutManager = linearLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getFromDB() {
        FirebaseDatabase.getInstance().getReference("Review")
            .child(storeInfo.store_id!!)
            .child("Comments")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    review_list.clear()
                    for (reviews in snapshot.children) {
                        val review = reviews.getValue(Review::class.java)
                        if(review == null) continue
                        Log.d("ReviewFragment", "getFromDB : ${review}")
                        review_list.add(review)
                    }
                    Log.d("ReviewFragment", "getFromDB : ${review_list}")
                    reviewAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ReviewFragment", "getFromDB : ${error}")
                }
            })
    }
}