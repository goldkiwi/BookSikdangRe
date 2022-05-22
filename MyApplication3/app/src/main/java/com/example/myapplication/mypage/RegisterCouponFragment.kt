package com.example.myapplication.mypage

import android.content.Context
import android.icu.util.ValueIterator
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import com.example.myapplication.R
import com.example.myapplication.bookActivity._coupon
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RegisterCouponFragment : Fragment() {
    lateinit var input_coupon_code : EditText
    lateinit var invalid_text : TextView
    lateinit var clear_text : ImageView
    lateinit var btn_register : Button

    private var text_state = false
    private lateinit var user_id : String
    private lateinit var callback : OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                completed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register_coupon, container, false)

        user_id = FirebaseAuth.getInstance().uid!!
        input_coupon_code = view.findViewById(R.id.input_coupon_code)
        invalid_text = view.findViewById(R.id.invalid_text)

        clear_text = view.findViewById(R.id.clear_text)
        clear_text.setOnClickListener {
            input_coupon_code.text.clear()
        }

        btn_register = view.findViewById(R.id.btn_register)
        btn_register.setOnClickListener {
            tryRegister()
        }

        input_coupon_code.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(TextUtils.isEmpty(s)) {
                    update_ui_if_blank()
                    text_state = false
                    return
                }

                if(text_state == false) {
                    update_ui()
                    text_state = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        return view
    }

    private fun tryRegister() {
        val code = input_coupon_code.text.toString()

        val ref = FirebaseDatabase.getInstance().getReference("SystemCoupon")
            .child(code)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var coupon = snapshot.getValue(_coupon::class.java)

                    if(!snapshot.exists() || coupon == null) {
                        invalid_text.text = getString(R.string.invalid_coupon)
                        invalid_text.visibility = View.VISIBLE
                        return
                    }

                    if(coupon.user_id!!.compareTo("대충관리자가주기적으로업데이트!") != 0) {
                        invalid_text.text = getString(R.string.used_coupon)
                        invalid_text.visibility = View.VISIBLE
                        return
                    }

                    val update = hashMapOf<String, Any>(
                        "user_id" to user_id,
                        "coupon_id" to coupon.coupon_id!!,
                        "coupon_exp" to coupon.coupon_exp!!,
                        "discount" to coupon.discount!!,
                        "expire" to coupon.expire!!,
                        "min_price" to coupon.min_price!!,
                        "type" to coupon.type!!
                    )

                    ref.setValue(update)

                    FirebaseDatabase.getInstance().getReference("UserCoupon")
                        .child(user_id)
                        .child(coupon.coupon_id!!)
                        .setValue(update)

                    Toast.makeText(requireContext(), "쿠폰 등록에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                    completed()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun update_ui_if_blank() {
        clear_text.visibility = View.GONE
        btn_register.isEnabled = false
    }

    private fun update_ui() {
        clear_text.visibility = View.VISIBLE
        btn_register.isEnabled = true
    }

    private fun completed() {
        (requireActivity() as MyCouponAcitivity).fragment_name.text = "쿠폰함"
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ViewMyCouponFragment())
            .commit()
    }
}