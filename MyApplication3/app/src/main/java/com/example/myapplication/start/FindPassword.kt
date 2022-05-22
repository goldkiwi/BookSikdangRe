package com.example.myapplication.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import android.os.Handler
import android.os.HandlerThread
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class FindPassword : AppCompatActivity() {
    lateinit var email : EditText
    lateinit var username : EditText
    lateinit var phone_number : EditText
    lateinit var show_problem : TextView

    lateinit var change_password : Button

    lateinit var reference : DatabaseReference
    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)

        email = findViewById(R.id.email)
        username = findViewById(R.id.username)
        show_problem = findViewById(R.id.show_problem)
        phone_number = findViewById(R.id.phone_number)
        phone_number.imeOptions = EditorInfo.IME_ACTION_DONE
        phone_number.setOnEditorActionListener (object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    val str_email = email.text.toString()
                    val str_username = username.text.toString()
                    val str_phone_number = phone_number.text.toString()

                    isEmailExist(str_email, str_username, str_phone_number)
                    return true
                }
                return false
            }
        })
        change_password = findViewById(R.id.change_password)

        change_password.setOnClickListener {
            val str_email = email.text.toString()
            val str_username = username.text.toString()
            val str_phone_number = phone_number.text.toString()

            isEmailExist(str_email, str_username, str_phone_number)
        }

    }

    fun isEmailExist(email : String, username : String, phone_number : String) {
        if(isProfileEmpty(email, username, phone_number)) {
            Toast.makeText(this, "정보를 기입해 주세요!!", Toast.LENGTH_SHORT).show()
            return
        }

        reference = FirebaseDatabase.getInstance().getReference("Users")
        val getProfile = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    val user_info = user.getValue(User::class.java)
                    Log.d("getProfile", user_info.toString())
                    if (user_info!!.email.equals(email)) {
                        if(user_info!!.username.equals(username)
                            and user_info!!.phone_number.equals(phone_number)) {
                            sendEmail(email)
                            return
                        }
                        else {
                            show_problem.text = "회원 정보가 일치하지 않습니다."
                            return
                        }
                    }
                }
                show_problem.text = "${email}은 존재하지 않습니다."
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        reference.addListenerForSingleValueEvent(getProfile)
    }

    fun sendEmail(email: String) {
        auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                show_problem.text = "인증메일이 보내졌습니다. 비밀번호를 변경하세요"
                Handler(mainLooper).postDelayed({finish()}, 10000)
            }

    }

    fun isProfileEmpty(email : String, username : String, phone_number : String) : Boolean{
        return TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(phone_number)
    }
}