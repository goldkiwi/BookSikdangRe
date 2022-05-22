package com.example.myapplication.storeActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myapplication.BookActivityBuilder
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlin.math.round

class StoreActivity : AppCompatActivity() {
    companion object {
        val ratingBar = arrayListOf(
                R.drawable.icon_rating_0,
                R.drawable.icon_rating_1,
                R.drawable.icon_rating_2,
                R.drawable.icon_rating_3,
                R.drawable.icon_rating_4,
                R.drawable.icon_rating_5
        )
    }

    lateinit var store_image : ImageView
    lateinit var store_name : TextView
    lateinit var rating : TextView
    lateinit var rating_star : ImageView
    lateinit var phone_number : TextView
    lateinit var bookmark_number : TextView
    lateinit var user_review : TextView
    lateinit var market_review : TextView
    lateinit var bookmark : ImageView
    lateinit var reserve_now : ImageView

    lateinit var viewPager : ViewPager2
    lateinit var actionPageViewPagerAdapter: ActionPageViewPagerAdapter

    lateinit var rv_action : RecyclerView
    lateinit var actionAdapter: ActionAdapter

    lateinit var user_id : String
    lateinit var store_info : StoreInfo

    var isBookMarked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        Log.d("확인 StoreActivity", "시작")

        store_info = intent.getSerializableExtra("store_info") as StoreInfo
        init_DB()
        init_Ui()
        getReviewMetaFromDB()
        getBookMarkThisMarket()
    }

    private fun init_DB() {
        user_id = FirebaseAuth.getInstance().uid!!
    }

    private fun init_Ui() {
        store_image = findViewById(R.id.store_image)
        Glide.with(this).load(store_info.store_image).into(store_image)

        store_name = findViewById(R.id.store_name)
        store_name.text = store_info.store_name

        rating = findViewById(R.id.rating)
        rating_star = findViewById(R.id.rating_star)

        phone_number = findViewById(R.id.phone_number)
        phone_number.text = store_info.phone_number

        bookmark_number = findViewById(R.id.bookmark_number)
        user_review = findViewById(R.id.user_review)
        market_review = findViewById(R.id.martket_review)

        bookmark = findViewById(R.id.bookmark)
        bookmark.setOnClickListener {
            bookMark()
            Log.d("bookmark", "${isBookMarked}")
        }

        rv_action = findViewById(R.id.rv_action)
        actionAdapter = ActionAdapter(this)
        val actionLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_action.adapter = actionAdapter
        rv_action.layoutManager = actionLayoutManager

        viewPager = findViewById(R.id.viewPager)
        actionPageViewPagerAdapter = ActionPageViewPagerAdapter(this, viewPager, actionAdapter)
        actionPageViewPagerAdapter.addFragment(MenuFragment(store_info))
        actionPageViewPagerAdapter.addFragment(ReviewFragment(store_info))
        actionAdapter.setViewPagerAdapeter(actionPageViewPagerAdapter)
        viewPager.adapter = actionPageViewPagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                actionAdapter.scrollPosition()
            }
        })

        reserve_now = findViewById(R.id.btn_reserve)
        reserve_now.setOnClickListener {
            Log.d("확인 예약 버튼 누름", "시작")
            BookActivityBuilder(store_info.store_id!!, store_info.store_type!!, this).build()
        }
    }

    private fun getReviewMetaFromDB() {
        FirebaseDatabase.getInstance().getReference("Review")
            .child(store_info.store_id!!)
            .child("info")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(ReviewData::class.java)
                    if(data == null) return
                    user_review.text = data.cnt_user.toString()
                    market_review.text = data.cnt_owner.toString()
                    val total_rate = data.rating!!.toFloat()
                    val rate = total_rate / data.cnt_user!!
                    rating.text = "%.1f".format(rate)

                    rating_star.setImageResource(ratingBar[round(rate).toInt()])


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("StoreAcitivity", "getReviewMetaFromDB ${error}")
                }
            })
    }

    private fun getBookMarkThisMarket() {
        FirebaseDatabase.getInstance().getReference("BookMark")
                .child(user_id)
                .child(store_info.store_id!!)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            runOnUiThread {
                                bookmark.setImageResource(R.drawable.book_mark)
                            }
                        } else {
                            runOnUiThread {
                                bookmark.setImageResource(R.drawable.not_book_mark)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })


        FirebaseDatabase.getInstance().getReference("BookMark")
                .child(store_info.store_id!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val cnt_bookmark = snapshot.childrenCount
                        bookmark_number.text = cnt_bookmark.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("StoreAcitivity", "getBookMarkThisMarket ${error}")
                    }
                })
    }

    private fun bookMark() {
        if(isBookMarked) {
            removeSubcribe()
            isBookMarked = false
        } else {
            subscribeThisMarket()
            isBookMarked = true
        }
    }

    private fun subscribeThisMarket() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        FirebaseDatabase.getInstance().getReference("BookMark")
                .child(user_id)
                .child(store_info.store_id!!)
                .setValue(store_info)

        val user_info = hashMapOf<String, Any>(
            "id" to user_id,
            "username" to firebaseUser!!.displayName!!,
            "phone_number" to firebaseUser!!.phoneNumber!!
        )

        FirebaseDatabase.getInstance().getReference("BookMark")
                .child(store_info.store_id!!)
                .child(user_id)
                .setValue(user_info)
    }

    private fun removeSubcribe() {
        FirebaseDatabase.getInstance().getReference("BookMark")
                .child(user_id)
                .child(store_info.store_id!!)
                .removeValue()

        FirebaseDatabase.getInstance().getReference("BookMark")
                .child(store_info.store_id!!)
                .child(user_id)
                .removeValue()
    }
}