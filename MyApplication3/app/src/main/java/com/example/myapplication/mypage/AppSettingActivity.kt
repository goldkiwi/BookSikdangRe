package com.example.myapplication.mypage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.dataclass.User
import com.example.myapplication.start.CustomerLogInActivity
import com.example.myapplication.start.SelectLoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.suke.widget.SwitchButton

class AppSettingActivity : AppCompatActivity() {
    companion object {
        val Settings = "Settings"
        val Notice = "Notice"
        val Etc = "Etc"
        val Recomment = "Recomment"
    }
    lateinit var back : ImageView

    lateinit var switch_notice : SwitchButton
    lateinit var switch_recomment : SwitchButton
    lateinit var switch_etc : SwitchButton

    lateinit var btn_logout : Button
    lateinit var btn_change_password : Button
    lateinit var btn_appout : Button

    lateinit var pref : SharedPreferences

    private var Notice_On : Boolean = false
    private var Recomment_On : Boolean = false
    private var Etc_On : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_setting)

        init_Data()
        init_UI()
    }

    private fun init_UI() {
        back = findViewById(R.id.back)
        back.setOnClickListener { finish() }

        switch_notice = findViewById(R.id.switch_notice)
        switch_notice.isChecked = Notice_On
        switch_notice.setOnCheckedChangeListener { view, isChecked ->
            Notice_On = isChecked
            update_state(Notice, isChecked)
        }

        switch_recomment = findViewById(R.id.switch_recomment)
        switch_recomment.isChecked = Recomment_On
        switch_recomment.setOnCheckedChangeListener { view, isChecked ->
            Recomment_On = isChecked
            update_state(Recomment, isChecked)
        }

        switch_etc = findViewById(R.id.switch_etc)
        switch_etc.isChecked = Etc_On
        switch_etc.setOnCheckedChangeListener { view, isChecked ->
            Etc_On = isChecked
            update_state(Etc, isChecked)
        }

        btn_logout = findViewById(R.id.btn_logout)
        btn_logout.setOnClickListener {
            //Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            //Log.d("확인 로그아웃버튼", "sdsds")
            log_out()
        }

        btn_change_password = findViewById(R.id.btn_change_password)
        btn_change_password.setOnClickListener {
            change_password()
        }

        btn_appout = findViewById(R.id.btn_appout)
        btn_appout.setOnClickListener {
            app_out()
        }
    }

    private fun init_Data() {
        pref = getSharedPreferences(Settings, Context.MODE_PRIVATE)
        Notice_On = pref.getBoolean(Notice, true)
        Recomment_On = pref.getBoolean(Recomment, true)
        Etc_On = pref.getBoolean(Etc, true)
    }

    private fun update_state(key : String, toggle : Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, toggle)
        editor.commit()
    }

    private fun log_out() {
        //Log.d("확인 로그아웃버튼", "sdsds")
        //Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
        var intent = Intent(this, SelectLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //Log.d("확인 로그아웃버튼", "2")
        startActivity(intent)
    }

    private fun app_out() {
        update_state(Notice, true)
        update_state(Recomment, true)
        update_state(Etc, true)

        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .removeValue()
    }

    private fun change_password() {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if(user == null) {
                        Toast.makeText(this@AppSettingActivity, "오류발생", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if(user.email == null) {
                        Toast.makeText(this@AppSettingActivity, "이메일이 등록되어있지 않습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(user.email)
                        .addOnSuccessListener {
                            Toast.makeText(this@AppSettingActivity, "등록하신 메일로 비밀번호 변경 메일이 갔습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }
}