package com.example.myapplication.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.bookActivity.BookActivity
import com.example.myapplication.bookhistory.BookHistoryActivity
import com.example.myapplication.bookhistory.BookHistoryFragment
import com.example.myapplication.start.CustomerLogInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MyPage : Fragment() {
    lateinit var firebaseUser: FirebaseUser
    lateinit var user_name : TextView
    lateinit var my_setting : TextView
    lateinit var my_coupon : TextView
    lateinit var my_pay_history : TextView
    lateinit var my_book_history : TextView
    lateinit var notice : TextView
    lateinit var app_setting : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.mypage, container, false)
        init_data()
        init_UI(view)
        return view
    }

    private fun init_data() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
    }
    private fun init_UI(view: View) {
        user_name = view.findViewById(R.id.user_name)
        user_name.text = "${firebaseUser.displayName} 님"

        my_setting = view.findViewById(R.id.mp_privateSettingTV)
        my_setting.setOnClickListener {
            val _intent = Intent(requireContext(), MySettingActivity::class.java)
            startActivity(_intent)
        }

        my_coupon = view.findViewById(R.id.mp_couponTV)
        my_coupon.setOnClickListener {
            val _intent = Intent(requireContext(), MyCouponAcitivity::class.java)
            startActivity(_intent)
        }

        my_pay_history = view.findViewById(R.id.mp_paySetTV)
        my_pay_history.setOnClickListener {
            val _intent = Intent(requireContext(), PaymentActivity::class.java)
            requireContext().startActivity(_intent)
        }

        my_book_history = view.findViewById(R.id.mp_bookHistTV)
        my_book_history.setOnClickListener {
            show_book_history()
        }

        notice = view.findViewById(R.id.mp_noticeTV)
        notice.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            requireContext().startActivity(intent)
        }

        app_setting = view.findViewById(R.id.mp_appSettingTV)
        app_setting.setOnClickListener {
            val intent = Intent(requireContext(), AppSettingActivity::class.java)
            requireContext().startActivity(intent)
        }

    }

    private fun show_book_history() {
        var intent = Intent(context, BookHistoryActivity::class.java)
        intent.putExtra("user_id", firebaseUser.uid)
        startActivity(intent)

    }
}