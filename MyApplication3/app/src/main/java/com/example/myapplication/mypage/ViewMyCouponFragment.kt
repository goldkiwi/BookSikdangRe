package com.example.myapplication.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bookActivity.Coupon
import com.example.myapplication.bookActivity._coupon
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewMyCouponFragment() : Fragment() {
    lateinit var register_coupon : RelativeLayout

    lateinit var rv_coupon : RecyclerView
    var coupon_list : ArrayList<_coupon> = arrayListOf()
    lateinit var myCouponAdapter : MyCouponAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_view_my_coupon, container, false)

        register_coupon = view.findViewById(R.id.register_coupon)
        register_coupon.setOnClickListener {
            (requireActivity() as MyCouponAcitivity).fragment_name.text = "쿠폰 등록하기"
            (requireContext() as FragmentActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, RegisterCouponFragment())
                .commit()
        }

        rv_coupon = view.findViewById(R.id.rv_copoun)
        myCouponAdapter = MyCouponAdapter(requireContext(), coupon_list)
        val linearLayout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_coupon.adapter = myCouponAdapter
        rv_coupon.layoutManager = linearLayout

        getFromDB()
        return view
    }

    private fun getFromDB() {
        val user_id = FirebaseAuth.getInstance().uid!!
        FirebaseDatabase.getInstance().getReference("UserCoupon")
            .child(user_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val coupon = data.getValue(_coupon::class.java)
                        if(coupon == null) continue
                        if(!Coupon.IsValidCoupon(coupon)) {
                            FirebaseDatabase.getInstance().getReference("UserCoupon")
                                .child(user_id)
                                .child(data.key!!).removeValue()
                            continue
                        }
                        coupon_list.add(coupon)
                    }
                    myCouponAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}