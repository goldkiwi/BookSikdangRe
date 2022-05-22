package com.example.myapplication.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import android.graphics.Color
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.dataclass.User
import com.google.firebase.database.*

class FindEmail : AppCompatActivity() {
    lateinit var username : EditText
    lateinit var phone_number : EditText
    lateinit var show_problem : TextView
    lateinit var findEmail: Button
    lateinit var reference: DatabaseReference

    var isExist : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_email)

        username = findViewById(R.id.username)
        phone_number = findViewById(R.id.phone_number)
        show_problem = findViewById(R.id.show_problem)
        findEmail = findViewById(R.id.find_email)

        findEmail.setOnClickListener {
            val str_username = username.text.toString()
            val str_phone_number = phone_number.text.toString()
            isEmailExist(str_username, str_phone_number)
        }
    }

    fun isEmailExist(username : String, phone_number : String) {
        isExist = false
        reference = FirebaseDatabase.getInstance().getReference("Users")
        val getProfile = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    val user_info = user.getValue(User::class.java)
                    if (user_info!!.username.equals(username)
                        and user_info!!.phone_number.equals(phone_number)) {
                        show_problem.text = user_info!!.email
                        show_problem.setTextColor(Color.parseColor("#808080"))
                        isExist = true
                        break
                    }
                }

                if(!isExist) {
                    show_problem.text = "회원의 정보가 불일치합니다."
                    show_problem.setTextColor(Color.parseColor("#FF0000"))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        reference.addListenerForSingleValueEvent(getProfile)
    }
}