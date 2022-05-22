package com.example.myapplication.bookActivity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PayActivity : AppCompatActivity() {
    companion object {
        val NotSelected = -1
    }
    lateinit var store_name : TextView
    lateinit var requset : TextView
    lateinit var face_price : TextView
    lateinit var coupon_directory : TextView
    lateinit var real_price : TextView
    lateinit var table_directory : RecyclerView
    lateinit var check_select_button : RadioGroup
    lateinit var pay_button : Button
    lateinit var recycler_view_usedCoupon : RecyclerView

    lateinit var stocks : HashMap<String, ChoiceItem>
    lateinit var storeInfo : StoreInfo
    lateinit var coupon_list : ArrayList<Coupon>
    lateinit var selected_time : String
    private lateinit var used_coupons : ArrayList<Coupon>
    var price : Int = 0

    private lateinit var firebaseUser : FirebaseUser
    private var isCouponSet : Boolean = false
    var duplicate : Coupon? = null
    var isFail : Boolean = false
    var isSuccess = false
    var semaphore = Semaphore(1)

    var bookCompleteSwitch = false

    lateinit var user_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("payActivity", "not uploaded ${isCouponSet}")
        setContentView(R.layout.activity_pay)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        user_id = firebaseUser.uid!!
        getCouponFromDB()
        stocks = intent.getSerializableExtra("stocks") as HashMap<String, ChoiceItem>
        price = intent.getIntExtra("price", 0)
        storeInfo = intent.getSerializableExtra("store_info") as StoreInfo
        selected_time = intent.getStringExtra("bookTime")!!
        used_coupons = arrayListOf()

        if(price == 0) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            finish()
        }
        val complete_intent = Intent(this, PayCompleteSuccess::class.java)
        init_UI()
    }

/* Listener */
    fun updatePrice() {
        var discout = 0
        used_coupons.clear()
        for(data in coupon_list) {
            if(data.used == true) {
                discout += data.discount.toInt()
                used_coupons.add(data)
            }
        }
        real_price.text =  (price - discout).toString()
        recycler_view_usedCoupon.adapter!!.notifyDataSetChanged()
    }

    /* Private Function */
    private fun init_UI() {
        store_name = findViewById(R.id.payPageSikdangName)
        store_name.text = storeInfo.store_name
        requset = findViewById(R.id.reqEditText)

        face_price = findViewById(R.id.payPageOriginalPrice)
        face_price.text = "매장 가격 : ${price}"

        coupon_directory = findViewById(R.id.couponNumTV)
        table_directory = findViewById(R.id.couponRV)

        real_price = findViewById(R.id.totalPriceTV)
        real_price.text = "총 가격 : ${price}"

        pay_button = findViewById(R.id.payButton)
        check_select_button = findViewById(R.id.check_select_button)
        recycler_view_usedCoupon = findViewById(R.id.recycler_view_used_coupon)

        pay_button.setOnClickListener {
            val radioId = check_select_button.checkedRadioButtonId
            when (radioId) {
                NotSelected->{
                    Toast.makeText(this, "사용하실 수단을 선택해 주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                R.id.kakaoPayCB-> {kakaoPayProcess(price, firebaseUser.uid, storeInfo.store_name!!)}
                R.id.secondPayCB->{bookComplete()}
            }
        }

        coupon_directory.setOnClickListener {
            if(isCouponSet) {
                showDiaglog()
            } else { Toast.makeText(this, "쿠폰 불러오기가 아직 완료되지 않았습니다.", Toast.LENGTH_SHORT).show()}
        }
        init_table_and_coupons()
    }

    private fun init_table_and_coupons() {
        table_directory.adapter = TableDirectoryAdapter(applicationContext, stocks)
        table_directory.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        table_directory.setHasFixedSize(true)

        recycler_view_usedCoupon.adapter = UsedCouponAdapter(applicationContext, used_coupons)
        recycler_view_usedCoupon.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recycler_view_usedCoupon.setHasFixedSize(true)
    }

    private fun getCouponFromDB() {
        FirebaseDatabase.getInstance().getReference("UserCoupon")
                .child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        coupon_list  = arrayListOf()
                        getCoupon(snapshot)
                        isCouponSet = true
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("PayActivity", "getCouponFromDB: ${error.message}")
                    }
                })
    }

    private fun getCoupon(snapshot: DataSnapshot) {
        for (data in snapshot.children) {
            val proto_coupon = data.getValue(_coupon::class.java)
            if(proto_coupon == null) continue
            if(!Coupon.IsValidCoupon(proto_coupon)) {
                Log.d("PayActivity", "IsValidCoupon : ${proto_coupon.coupon_id} is expired")
                FirebaseDatabase.getInstance().getReference("UserCoupon")
                        .child(firebaseUser.uid)
                        .child(data.key!!).removeValue()
                continue
            }
            coupon_list.add(Coupon(
                    proto_coupon.user_id!!,
                    proto_coupon.coupon_id!!,
                    proto_coupon.min_price!!,
                    proto_coupon.discount!!,
                    proto_coupon.coupon_exp!!,
                    proto_coupon.type!!,
                    proto_coupon.expire!!,
                    false
            ))
        }
        for (data in coupon_list) {
            Log.d("coupon_list", "${data.coupon_id}")
        }
    }

    private fun showDiaglog() {
        CouponDialog(this, coupon_list, this).show()
    }

    private fun kakaoPayProcess(price : Int, user_id : String, store_id : String) {
        val requestBody = FormBody.Builder()
            .add("cid", "TC0ONETIME")
            .add("partner_order_id", user_id)
            .add("partner_user_id", store_id)
            .add("item_name", "예약")
            .add("quantity", "1")
            .add("total_amount", price.toString())
            .add("vat_amount", "200")
            .add("tax_free_amount", "0")
            .add("approval_url", "http://192.168.147.1:3000/kakaopayment")
            .add("fail_url", "http://192.168.147.1:3000/kakaopayment")
            .add("cancel_url", "http://192.168.147.1:3000/kakaopayment")
            .build()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        var client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        var request = okhttp3.Request.Builder()
            .url("https://kapi.kakao.com/v1/payment/ready")
            .addHeader("Authorization", "KakaoAK 697a966c0fe76c2e80470060ab00fe30")
            .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            //.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("fail 1", request.headers.toString())
                Log.d("fail 2", request.body.toString())
                Log.d("fail 2", requestBody.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var buffer = Buffer()
                request.body!!.writeTo(buffer)
                Log.d("sucess 1", request.headers.toString())
                Log.d("sucess 2", buffer.readUtf8())
                val jsonData = response.body!!.string()
                val jobject = JSONObject(jsonData)

                val tid = jobject.getString("tid")
                val popup = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(jobject.getString("next_redirect_pc_url"))
                )
                startActivity(popup)
                var is_sign_up = false
                while (!is_sign_up) {
                    Thread.sleep(4000)
                    client = OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build()

                    val requestBody = FormBody.Builder()
                        .add("cid", "TC0ONETIME")
                        .add("tid", tid)
                        .add("partner_order_id", "partner_order_id")
                        .add("partner_user_id", "partner_user_id")
                        .add("pg_token", "pg_token=xxxxxxxxxxxxxxxxxxxx")
                        .build()

                    var request = okhttp3.Request.Builder()
                        .url("https://kapi.kakao.com/v1/payment/approve")
                        .addHeader("Authorization", "KakaoAK f16d4e8048a301aa905a42c285e88b9d")
                        .addHeader(
                            "Content-Type",
                            "application/x-www-form-urlencoded;charset=utf-8"
                        )
                        //.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
                        .post(requestBody)
                        .build()


                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.d("fail_after 1", request.headers.toString())
                            Log.d("fail_after 2", request.body.toString())
                            Log.d("fail_after 3", requestBody.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            Log.d("sucess_after 1", request.headers.toString())
                            Log.d("sucess_after 2", buffer.readUtf8())
                            Log.d("sucess_after 3", response.code.toString())
                            val jsonData = response.body!!.string()
                            val jobject = JSONObject(jsonData)
                            if (jobject.getString("code") == "-708") {
                                Log.d("확인 success", "임시적 성공")
                                Log.d("kakaoPayProcess 주문 물품", "${stocks}")

                                semaphore.acquire()
                                isSuccess = is_sign_up
                                //bookComplete()
                                if(!isSuccess) {
                                    Log.d("확인 sucess", "한번만 나와야 정상~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`")
                                    bookComplete()
                                }
                                is_sign_up = true
                                semaphore.release()
                                return
                            }
                        }
                    })

                }
            }
        })
    }

    private fun bookComplete() {
        Log.d("확인 bookComplete() 호출", "${stocks}")
        var bookHere = ArrayList(stocks.keys)
        var tryToBook: ArrayList<Pair<String, String>> = arrayListOf()
        var cnt = 0
        for (floor_table in bookHere) {
            tryToBook.add(splitTable(floor_table))
            cnt += 1
        }
        var isCached = false
        var complete = false
        var sem = Semaphore(1)

        Log.d("확인 bookComplete()", "2")


        var firstSwitch = true
        var secondSwitch = false

        bookCompleteSwitch = true

        var tempRef = FirebaseDatabase.getInstance().getReference("Tables")
                .child(storeInfo.store_id!!)
                .child("Booked")

        var isNasted = false
        for (i in 0..tryToBook.size-1) {
            Log.d("확인 bookComplete()", tryToBook.size.toString()+"반복 시작 tryToBook: ${tryToBook} ")
            tempRef.child(tryToBook[i].first).child(selected_time).child(tryToBook[i].second).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("확인 bookComplete() tryToBook", "2")
                    if (bookCompleteSwitch == true){
                        //Log.d("확인 bookComplete() tryToBook", "3")
                        for (tableInfo in snapshot.children) {
                            Log.d("확인 bookComplete()", tableInfo.toString() + tableInfo.value.toString())
                            if(tableInfo.value == 0){ // mutex 가 0인게 있으면 실패
                                //Log.d("확인 bookComplete() tryToBook", "4")
                                isNasted = true
                            }
                            //Log.d("확인 bookComplete() tryToBook", "5")
                        }
                        bookCompleteSwitch = false
                        //Log.d("확인 bookComplete() tryToBook", "6")
                        if(isNasted){
                            Toast.makeText(this@PayActivity, "테이블이 중복됐습니다.", Toast.LENGTH_SHORT).show()
                        }
                        //getTableOnFloor()
                        setTablesBooked(tryToBook)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
                }

            })
            //Log.d("확인 bookComplete() tryToBook", "7")
        }




/*

        while (!isCached) {
            //Log.d("확인 while문", "@@@@@@@@@@@@@@@@@@@@@@@")
            var store_ref = FirebaseDatabase.getInstance().getReference("Tables")
                .child(storeInfo.store_id!!)
                .child("Booked")
                .runTransaction(object :Transaction.Handler{
                    override fun doTransaction(currentData: MutableData): Transaction.Result {

                        for (location in tryToBook) {
                            //Log.d("확인 for문", "#####################################")
                            //Log.d("확인 for문", "${tryToBook}")
                            var ref = currentData.child(location.first)
                                .child(selected_time)
                                .child(location.second)

                            val mutex = ref.getValue(IsTableBooKed::class.java)
                            //Log.d("확인 for문2 ", mutex.toString())

                            if (mutex == null) {
                                Log.d("in transaction", "Not cached")
                                return Transaction.abort()
                            }
                            //Log.d("확인 for문3 ", mutex.toString())
                            isCached = true
                            //Log.d("확인 for문4 ", mutex.toString())
                            if (mutex!!.mutex == 0) { //예약 들어있으면 중지?
                                return Transaction.abort()
                            }
                            ref.value = hashMapOf(
                                "mutex" to 0
                            )

                            var bookInfoRef = currentData.child(location.first)
                                .child(selected_time)
                                .child("BookInfo")

                            var bookInfo = bookInfoRef.getValue(BookInfo::class.java)
                            if(bookInfo == null) return Transaction.abort()
                            if (complete == true) return Transaction.abort()
                            if(!complete) {
                                bookInfoRef.value = hashMapOf(
                                    "current" to (bookInfo!!.current!! - cnt),
                                    "max" to bookInfo!!.max
                                )


                            }
                            Log.d("확인 bookComplete()", "complete = true")
                            complete = true
                        }

                        Log.d("확인 bookComplete() reservate 호출", "${stocks}")
                        reservate()
                        use_up_coupons()
                        val _intent = Intent(this@PayActivity, PayCompleteSuccess::class.java)
                        _intent.putExtra("request", requset.text.toString())
                        _intent.putExtra("stocks", stocks)
                        startActivity(_intent)
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                    }
                })
        }
    */

    }

    public fun setTablesBooked(tryToBook: ArrayList<Pair<String, String>>){

        var tempRef = FirebaseDatabase.getInstance().getReference("Tables")
                .child(storeInfo.store_id!!)
                .child("Booked")

        for ( i in 0..tryToBook.size-1){
            Log.d("확인 setTablesBooked ", "반복")
            var ref = tempRef.child(tryToBook[i].first).child(selected_time).child(tryToBook[i].second)
            ref.setValue(hashMapOf(
                    "mutex" to 0
            ))

            var bookInfoRef = tempRef.child(tryToBook[i].first)
                    .child(selected_time)
                    .child("BookInfo")

            //var bookInfo = bookInfoRef.getValue(BookInfo::class.java)
            bookCompleteSwitch = true

            var isDataerrored = false
            bookInfoRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Log.d("확인 bookComplete() tryToBook", "2")
                    if (bookCompleteSwitch == true){
                        for (tableInfo in snapshot.children) {
                            var bookInfo = snapshot.value
                            if(bookInfo == null) isDataerrored = true
                            Log.d("확인 setTablesBooked ", "${bookInfo}")
                        }
                        bookCompleteSwitch = false
                        reservate()
                        use_up_coupons()
                        val _intent = Intent(this@PayActivity, PayCompleteSuccess::class.java)
                        _intent.putExtra("request", requset.text.toString())
                        _intent.putExtra("stocks", stocks)
                        startActivity(_intent)
                        //return Transaction.success(currentData)
                        /*
                        bookInfoRef.setValue(hashMapOf(
                                "current" to (bookInfo!!.current!! - cnt),
                                "max" to bookInfo!!.max
                        ))*/
                        //setTablesBooked(tryToBook)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인 setSikdangListInfo()", "5 getFromDB : ${error}")
                }

            })

            /*
            if(bookInfo == null) return Transaction.abort()
            if (complete == true) return Transaction.abort()
            if(!complete) {
                bookInfoRef.value = hashMapOf(
                        "current" to (bookInfo!!.current!! - cnt),
                        "max" to bookInfo!!.max
                )


            }
            Log.d("확인 bookComplete()", "complete = true")
            complete = true*/
        }



    }


    private fun splitTable(name : String) : Pair<String, String> {
        var temp = name.split( ":")
        return Pair(temp[0], temp[1])
    }

    private fun reservate() {
        val current_date = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val pay_time = SimpleDateFormat("HH:mm a", Locale.KOREA).format(Calendar.getInstance().time)
        var request = requset.text.toString()

        if(TextUtils.isEmpty(request)) request == "NULL"



        var ref = FirebaseDatabase.getInstance().getReference().child("Store_reservation").child(storeInfo.store_id.toString())
        Log.d("reservate 확인 주문 물품", "${stocks}")

        var resInfo = hashMapOf<String, Any>(
                "userId" to firebaseUser.uid,
                "sikdangId" to storeInfo.store_id.toString(),
                "totalPay" to price,
                "bookTime" to selected_time,
                "payTime" to pay_time,
                "store_type" to storeInfo.store_type!!
        )





        var reservation = hashMapOf<String, Any>(
            "userId" to firebaseUser.uid,
            "sikdangId" to storeInfo.store_id.toString(),
            "totalPay" to price,
            "bookTime" to selected_time,
            "payTime" to pay_time,
            "store_type" to storeInfo.store_type!!
        )

        var floor_table_list = hashMapOf<String, String>()
        for (table_id in stocks.keys) {
            floor_table_list.put(table_id, stocks[table_id].toString())
        }

        reservation.put("tables", floor_table_list)




        ref.push().setValue(reservation).addOnSuccessListener {
            //finish()
            //upSikdangOnUser(postId)
            //setTimeAl(newFloor)
            Log.d("확인 PayActivity.reservate", "예약 추가 성공")

        }.addOnFailureListener {
            Log.d("확인 PayActivity.reservate", "예약 추가 실패")
        }

        // Reservation for user
        FirebaseDatabase.getInstance().getReference()
            .child("Reservations")
            .child(firebaseUser.uid)
            .child(current_date)
            .push()
            .setValue(reservation)

        // Reservation for Sikdang
        reservation.put("request", request)

        FirebaseDatabase.getInstance().getReference()
            .child("Reservations")
            .child(storeInfo.store_id!!)
            .child(current_date)
            .push()
            .setValue(reservation)
    }

    private fun use_up_coupons() {
        Log.d("use_up_coupons", "use_up_coupons")
        for (use_up in used_coupons) {
            Log.d("ss", "${use_up.coupon_id}")
            FirebaseDatabase.getInstance().getReference("UserCoupon")
                .child(user_id)
                .child(use_up.coupon_id)
                .removeValue()

            FirebaseDatabase.getInstance().getReference("SystemCoupon")
                .child(use_up.coupon_id)
                .removeValue()
        }
    }
}

data class BookInfo(val current : Int? = null, val max : Int? = null)