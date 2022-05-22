package com.example.myapplication.mainPage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.*
import com.example.myapplication.R
import com.example.myapplication.bookActivity.Coupon
import com.example.myapplication.bookActivity.MenuData
import com.example.myapplication.bookActivity._coupon
//import com.example.myapplication.bookMenu.MenuData
import com.example.myapplication.dataclass.Banner
import com.example.myapplication.dataclass.HashTag
import com.example.myapplication.dataclass.Location
import com.example.myapplication.recommendation.MsgCat
import com.example.myapplication.sikdangChoicePage.SikdangChoice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.sikdangchoice.*
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val NUM_PAGE = 5
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class Sikdang_main : Fragment() {
    //음식 추천 태그
    var tagList_sm = arrayListOf<TagLine>();

    //배너 뷰페이저
    //배너 페이지의 수
    private var num_page = 5

    var msgCat = MsgCat()
    var tagList: Array<String> = msgCat.getTagList()

    //var tagLineList : TagLineList = TagLineList();
    var tagLineList: TagLineList = TagLineList(tagList);

    lateinit var hash_tag_menu_recycler_view: RecyclerView
    lateinit var hashTagAdapter: HashTagAdapter
    lateinit var hashTagList: ArrayList<HashTag>

    lateinit var banner_recycler_view: RecyclerView
    lateinit var bannerAdapter: BannerAdapter
    lateinit var bannerList: ArrayList<Banner>
    lateinit var banner_layout: LinearLayoutManager

    lateinit var plus_tag: TextView

    private var tag_result: ArrayList<CatorySet>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.sikdang_main, container, false)
        //Toast.makeText(requireContext(), "aaa", Toast.LENGTH_SHORT).show()

        //makeMenuDB()
        //makeCoupon()
        /*
        //뷰페이저

        viewPager2=findViewById(R.id.banner_view_pager_2)
        var pagerAdapter2 = ScreenSlidePagerAdapter2(this)
        viewPager2.adapter=pagerAdapter2
*/
        //에딧 텍스트 : 거리
        var editTextDist: EditText = view.findViewById(R.id.editTextDist)
        editTextDist.imeOptions = EditorInfo.IME_ACTION_DONE
        editTextDist.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(v!!.text.toString())) {
                        Toast.makeText(requireContext(), "거리 (숫자)를 입력하세요;;", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    val range = v!!.text.toString().toInt()
                    if (range == 0) {
                        Toast.makeText(requireContext(), "거리 (숫자)를 입력하세요;;", Toast.LENGTH_SHORT).show()
                        v!!.text = ""
                        return false
                    }
                    var intent = Intent(requireActivity(), MapActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("range", range)
                    startActivity(intent)
                }
                return false
            }
        })
        //editTextDist.editableText


        //음식 카테고리
        //어댑터에서 SikdangChoice 페이지를 불러오게 된다
        //Log.d("종료지점확인", "onCreate_2")
        var catList = CatList()
        //Log.d("종료지점확인", "onCreate_3")
        var dist: Int = 0
        //dist = editTextDist.getText().toString().toInt()
        //Log.d("종료지점확인", "onCreate_3.5 "+dist.toString())
        var sikdangMainCatAdapter = SikdangMainCatAdapter(requireContext(), catList.getCatArray(), editTextDist)
        //Log.d("종료지점확인", "onCreate_4")
        var sikdangCatView: RecyclerView = view.findViewById(R.id.sikdang_cat_view)
        //Log.d("종료지점확인", "onCreate_5")
        sikdangCatView.adapter = sikdangMainCatAdapter
        //Log.d("종료지점확인", "onCreate_6")

        var catLM = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        sikdangCatView.layoutManager = catLM
        sikdangCatView.setHasFixedSize(true)

        //오늘 뭐먹지? 누르면 페이지 호출
        //여기에 눌러진 버튼도 매개변수로 줘야함
        var button_call_what_eat_today: Button = view.findViewById(R.id.button_whatEatToday)

        //putExtra로 넘겨주는 객체는 원본 객체에 접근하게 하는 것이 아닌 constructor에 있는 부분만 복사한 객체 새로 생성
        //오늘 뭐먹지 버튼 누르면 msgCat 객체를 whatEatToday 클래스로 넘겨준다
        button_call_what_eat_today.setOnClickListener {
            val intent = Intent(requireActivity(), What_eat_today::class.java)
            var tempText = "asasa";
            tempText += msgCat.getText()
            Log.d("확인 putExtra", tempText)
            intent.putExtra("msgcat", msgCat)
            Log.d("종료지점확인", "call What_eat_today")
            startActivity(intent)
        }


        /*
        //음식 추천 태그
        if(true){
            tagList_sm=tagLineList.getTagLineList()
            var tag_Adapter= Sikdang_main_tagAdapter(requireContext(), tagList_sm, msgCat)
            var main_tagList: RecyclerView = view.findViewById(R.id.main_tagList);

            main_tagList.adapter=tag_Adapter;

            var tag_lm= LinearLayoutManager(requireContext())
            main_tagList.layoutManager=tag_lm;
            main_tagList.setHasFixedSize(true)

        }
         */

        hash_tag_menu_recycler_view = view.findViewById(R.id.hash_tag_recycler_view)
        hash_tag_menu_recycler_view.setHasFixedSize(true)
        var hash_tag_layout = GridLayoutManager(requireContext(), 1)
        hash_tag_layout.orientation = LinearLayoutManager.HORIZONTAL
        hash_tag_menu_recycler_view.layoutManager = hash_tag_layout
        hashTagList = arrayListOf(
                HashTag("닭밝"),
                HashTag("곱창"),
                HashTag("죽"),
                HashTag("버거"),
                HashTag("마라탕"),
                HashTag("찜닭"),
                HashTag("냉면"),
                HashTag("쌀국수")
        )

        hashTagAdapter = HashTagAdapter(requireContext(), hashTagList)
        hash_tag_menu_recycler_view.adapter = hashTagAdapter
        hashTagAdapter.notifyDataSetChanged()
        Log.d("종료지점확인", "167")

        banner_recycler_view = view.findViewById(R.id.banner_recycler_view)
        banner_recycler_view.setHasFixedSize(true)
        banner_layout = LinearLayoutManager(requireContext())
        banner_layout.orientation = LinearLayoutManager.HORIZONTAL
        banner_recycler_view.layoutManager = banner_layout
        bannerList = arrayListOf(
                Banner(R.drawable.ic_placeholder, "1"),
                Banner(R.drawable.add_main, "2"),
                Banner(R.drawable.add_main_2, "3"),
                Banner(R.drawable.add_main_3, "4"),
                Banner(R.drawable.add_main_4, "5"),
                Banner(R.drawable.add_main_5, "6"),
                Banner(R.drawable.add_main_6, "7")
        )
        bannerAdapter = BannerAdapter(requireContext(), bannerList)
        banner_recycler_view.adapter = bannerAdapter
        bannerAdapter.notifyDataSetChanged()

        plus_tag = view.findViewById(R.id.plus_tag)
        plus_tag.setOnClickListener {
            showCatorySelectDialog()
        }


        Timer().schedule(StartBanner(), 0, 1000)


        return view
    }

    private fun showCatorySelectDialog() {
        CatorySelectDialog(requireContext(), this).show()
    }

    public fun getCurrentTag() {

    }
    /*

    //백 버튼

    override fun onBackPressed() {
        if (viewPager2.currentItem==0){
            super.onBackPressed()
        }else{
            viewPager2.currentItem=viewPager2.currentItem-1
        }
    }*/

    /*
    @RequiresApi(Build.VERSION_CODES.P)
    private fun getKeyHashBase64(context : Context) {
        val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        val signatures = info.signingInfo.apkContentsSigners
        val md = MessageDigest.getInstance("SHA")
        for (signature in signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val key = String(Base64.encode(md.digest() , 0))
            Log.d("hash key", key.toString())
        }
    }

     */
/*
    //배너 어댑터
    //최대 12페이지까지=> 진짜 12페이지까지 하려면 배너이미지 추가하고 아래 코드 좀 수정해야함
    private inner class ScreenSlidePagerAdapter2(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = num_page

        override fun createFragment(position: Int): Fragment {
            if (position == 0){
                return BannerSlideFragment(R.drawable.add_main, 0)
            }else if(position == 1){
                return BannerSlideFragment(R.drawable.add_main_2, 1)
            }else if(num_page >=3 && position == 2){
                return BannerSlideFragment(R.drawable.add_main_3, 2)
            }else if(num_page >=4 && position == 3){
                return BannerSlideFragment(R.drawable.add_main_4, 3)
            }else if(num_page >=5 && position == 4){
                return BannerSlideFragment(R.drawable.add_main_5, 4)
            }else if(num_page >=6 && position == 5){
                return BannerSlideFragment(R.drawable.add_main_6, 5)
            }else if(num_page >=7 && position == 6){
                return BannerSlideFragment(R.drawable.add_main_6, 6)
            }else if(num_page >=8 && position == 7){
                return BannerSlideFragment(R.drawable.add_main_6, 7)
            }else if(num_page >=9 && position == 8){
                return BannerSlideFragment(R.drawable.add_main_6, 8)
            }else if(num_page >=10 && position == 9){
                return BannerSlideFragment(R.drawable.add_main_6, 9)
            }else if(num_page >=11 && position == 10){
                return BannerSlideFragment(R.drawable.add_main_6, 10)
            }else if(num_page >=12 && position == 11){
                return BannerSlideFragment(R.drawable.add_main_6, 11)
            }else{
                return BannerSlideFragment(R.drawable.add_main, 0)
            }

        }
*/


    inner class StartBanner : TimerTask() {
        override fun run() {
            val currentPos = banner_layout.findFirstVisibleItemPosition()
            activity?.runOnUiThread({
                banner_recycler_view.scrollToPosition((currentPos + 1) % bannerAdapter.itemCount)
                //Log.d("StartBanner", "called")
            })

            try {
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    class RestaurantMenu(
            var restaurant_id: String,
            var menu: _Menu,
            var ingredients: ArrayList<_Ingredient>
    ) {
        fun updateDB() {
            lateinit var menuRef: DatabaseReference
            if (restaurant_id == "NOT") {
                menuRef = FirebaseDatabase.getInstance().getReference("Restaurants")
                        .child("아시안*양식")
                        .push()
                        .child("menu")
                        .push()
            } else {
                menuRef = FirebaseDatabase.getInstance().getReference("Restaurants")
                        .child("아시안*양식")
                        .child(restaurant_id)
                        .child("menu")
                        .push()
            }

            var menu__ = hashMapOf<String, Any>(
                    "product" to menu.product!!,
                    "price" to menu.price!!,
                    "product_exp" to menu.product_exp!!
            )
            menuRef.setValue(menu__)
            var ingredientRef = menuRef.child("ingredients").ref
            for (ingredient in ingredients) {
                var ing = hashMapOf<String, Any>(
                        "ing" to ingredient.ing!!,
                        "country" to ingredient.country!!
                )
                ingredientRef.push().setValue(ing)
            }
        }
    }

/*
    fun makeMenuDB() {
        var name = "-MZPej5um-h_y_KhWuuQ"
        var DB = arrayListOf<Sikdang_main.RestaurantMenu>(
                Sikdang_main.RestaurantMenu(name, _Menu("왕새우소세지오므라이스", 12900, "소세지 + 왕새우 + 샐러드"
                ), arrayListOf(_Ingredient("새우"	,"베트남산"))),
                Sikdang_main.RestaurantMenu(name, _Menu("킹타이거 감바스", 15500, "타이거새우 + 파스타면 + 샐러드"
                ), arrayListOf(_Ingredient("새우"	,"베트남산"))),
                Sikdang_main.RestaurantMenu(name, _Menu("로제 감바스", 14500, "타이거새우 + 파스타면 + 샐러드"
                ), arrayListOf(_Ingredient("새우"	,"베트남산"))),
                Sikdang_main.RestaurantMenu(name, _Menu("올리오 감바스", 12900, "타이거새우 + 파스타면 + 샐러드"
                ), arrayListOf(_Ingredient("새우"	,"베트남산"))),
                Sikdang_main.RestaurantMenu(name, _Menu("로제파스타", 11900, "로제파스타 + 바게뜨 + 샐러드"
                ), arrayListOf(_Ingredient("면"	,"국내산"))),
                Sikdang_main.RestaurantMenu(name, _Menu("크림치즈파스타", 11900, "크림치즈파스타 + 바게뜨 + 샐러드"
                ), arrayListOf(_Ingredient("면"	,"국내산"))),
                Sikdang_main.RestaurantMenu(name, _Menu("매운 떡볶이", 13900, "매운 떡볶이 2인 + 타이거새우"
                ), arrayListOf(_Ingredient("새우"	,"베트남산")))

        )

        for (data in DB) {
            data.updateDB()
        }
    }



 */
}

/*
fun makeLocationsDB(){
    Log.d("makeLo", "here")
    val cat = "닭고기"
    var locationRef = FirebaseDatabase.getInstance().getReference("Locations")
            .child(cat)


    var DB = arrayListOf(
            Location("와우빌딩", 37.535512, 126.824337),
            Location("짜투리 닷컴", 37.535677, 126.825981),
            Location("신월중학교", 37.536663, 126.824309),
            Location("계레얼학교", 37.535352, 126.826523),
            Location("광음교회", 37.535789, 126.826523),
            Location("제이씨빌딩", 37.535776, 126.826325),
            Location("흡연장", 37.535879, 126.825058)
    )

    for (data in DB){
        var pushed = locationRef.push()
        val key = pushed.key
        var map = makeData(data)
        map.put("id", key!!)
        Log.d("map",map.toString())
        pushed.setValue(map)
    }

}

fun makeData(loc : Location) : HashMap<String, Any> {
    Log.d("dsds", "sdsds")
    return hashMapOf<String, Any>(
            "name" to loc.name,
            "Lat" to loc.Lat,
            "Lng" to loc.Lng
    )
}
*/



fun makeCoupon(){
    val ref = FirebaseDatabase.getInstance().getReference("SystemCoupon")

    val db = arrayListOf<TEMP>(
            TEMP(10000, 30000, "쿠폰 1", 1, "2021-05-31"),
            TEMP(10000, 30000, "쿠폰 2", 1, "2021-05-31"),
            TEMP(5000, 0, "쿠폰 3", 1, "2021-05-31"),
            TEMP(3000, 50000, "쿠폰 4", 1, "2021-05-31"),
            TEMP(15000, 40000, "쿠폰 5", 2, "2021-05-31"),
            TEMP(10000, 0, "쿠폰 6", 2, "2021-05-31"),
            TEMP(4000, 0, "쿠폰 7", 2, "2021-05-31"),
            TEMP(3000, 100000, "쿠폰 8", 2, "2021-05-31"),
            TEMP(2000, 10000, "쿠폰 9", 3, "2021-05-31"),
            TEMP(3000, 30000, "쿠폰 10", 1, "2021-05-31"),
            TEMP(12000, 30000, "쿠폰 11", 1, "2021-05-31"),
            TEMP(5000, 0, "쿠폰 12", 1, "2021-05-31"),
            TEMP(4000, 50000, "쿠폰 13", 1, "2021-05-31"),
            TEMP(10000, 40000, "쿠폰 14", 2, "2021-05-31"),
            TEMP(8000, 0, "쿠폰 15", 2, "2021-05-31"),
            TEMP(4000, 0, "쿠폰 16", 2, "2021-05-31"),
            TEMP(3000, 100000, "쿠폰 17", 2, "2021-05-31"),
            TEMP(2000, 10000, "쿠폰 18", 3, "2021-05-31")
    )

    for(data in db){
        val pushed = ref.push()
        val coupon = data.TEMP2_coupon(pushed.key!!)
        pushed.setValue(coupon2Json(coupon))
    }
}

fun coupon2Json(coupon: _coupon) : HashMap<String, Any>{
    return hashMapOf(
            "coupon_id" to coupon.coupon_id!!,
            "discount" to coupon.discount!!,
            "min_price" to coupon.min_price!!,
            "coupon_exp" to coupon.coupon_exp!!,
            "type" to coupon.type!!,
            "expire" to coupon.expire!!,
            "user_id" to "대충관리자가주기적으로업데이트!"
        )
}
data class TEMP (
        val discount : Int,
        val minPrice : Int,
        val exp : String,
        val type : Int,
        val expire : String
        )
{
    fun TEMP2_coupon(key : String): _coupon {
        return _coupon("대충관리자가주기적으로업데이트!", key, discount.toString(), minPrice, exp, type, expire)
    }
}

