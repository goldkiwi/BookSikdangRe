package com.example.myapplication.mypage


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class NotificationActivity : AppCompatActivity() {
    lateinit var toolbar : Toolbar
    lateinit var finish : ImageView
    lateinit var title : TextView
    lateinit var loading : ProgressBar

    lateinit var user_id : String
    lateinit var noticeListFragment: NoticeListFragment
    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        toolbar = findViewById(R.id.toolbar)
        finish = findViewById(R.id.finish)
        finish.setOnClickListener {
            finish()
        }
        title = findViewById(R.id.title)
        loading = findViewById(R.id.loading)


        user_id = FirebaseAuth.getInstance().uid!!
        ref = FirebaseDatabase.getInstance().getReference("Notifications")
            .child(user_id)
        noticeListFragment = NoticeListFragment(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, noticeListFragment)
            .commit()

    }

    fun thisItemSelected(messageId: String) {
        loading.visibility = android.view.View.VISIBLE
        ref.child("Messages")
            .child(messageId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val Message = snapshot.getValue(NoticeMessage::class.java)
                    if(Message == null) {
                        Toast.makeText(this@NotificationActivity, "불러오기 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    runOnUiThread {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ReadNoticeFragment(Message!!, this@NotificationActivity))
                            .commit()
                    }

                    FirebaseDatabase.getInstance().getReference("Notifications")
                        .child(user_id)
                        .child("Message_Header")
                        .child(messageId)
                        .child("is_read")
                        .setValue(true)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }

    fun getUserId() : String {
        return user_id
    }

    fun back() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, noticeListFragment)
            .commit()
    }
}