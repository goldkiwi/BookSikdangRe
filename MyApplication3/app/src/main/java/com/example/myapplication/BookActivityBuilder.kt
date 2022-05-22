package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.icu.text.DateTimePatternGenerator
import android.text.BoringLayout
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.bookActivity.*
import com.example.myapplication.dataclass.StoreInfo
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class BookActivityBuilder(val sikdangId : String, val category: String, var context: Context) {
    private var storeInfo : StoreInfo? = null
    private var menus : ArrayList<MenuData> = arrayListOf()
    private lateinit var book_time : BookTime
    private lateinit var book_check_list : ArrayList<Boolean>
    private lateinit var time_list : ArrayList<String>
    private lateinit var floor_tables : HashMap<String, HashMap<String, Table>>

    fun build(){
        Log.d("확인 BookActivityBuilder", "build 호출")
        var StoreInfoReference = FirebaseDatabase.getInstance().getReference("Restaurants")
                .child(category)
                .child(sikdangId)

        Log.d("확인 BookActivityBuilder", "build 2")
        StoreInfoReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("확인 BookActivityBuilder", "build 3")
                val done1 = getStoreInfo(snapshot)
                Log.d("확인 BookActivityBuilder", "build 3.1")
                if(!done1) {Log.d("getStoreInfo", "1fail"); return}
                Log.d("확인 BookActivityBuilder", "build 3.2")
                val done2 = getMenu(snapshot)
                Log.d("확인 BookActivityBuilder", "build 3.3")
                if(!done2){Log.d("getMenu", "2fail"); return}
                Log.d("확인 BookActivityBuilder", "build 4")

                var TableInfo = FirebaseDatabase.getInstance().getReference("Tables")
                        .child(sikdangId)

                Log.d("확인 BookActivityBuilder", "build 5")
                TableInfo.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(table_snapshot: DataSnapshot) {
                        Log.d("확인 BookActivityBuilder", "build 6")
                        val nfloor = table_snapshot.childrenCount
                        val done3 = getBookTime(table_snapshot.child("Booked"))
                        if(!done3) return
                        val done4 = getStoreTable(table_snapshot.child("TableInfo"))
                        if(!done4) return
                        book_time = BookTime(time_list, book_check_list)
                        var intent = Intent(context, BookActivity::class.java)

                        intent.putExtra("bookTime", book_time)
                        intent.putExtra("menuList", menus)
                        intent.putExtra("storeInfo", storeInfo)
                        intent.putExtra("TableMetaData", floor_tables)
                        Log.d("확인 BookActivityBuilder", "BookActivity 호출")
                        context.startActivity(intent)

                    }


                    override fun onCancelled(error: DatabaseError) {
                        Log.d("확인 BookActivityBuilder", "BookActivity.onCancelled")
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("BookActivityBuilder","StoreInfoReference : " + error.message)
            }
        })
    }

    fun getStoreInfo(store_snapshot: DataSnapshot) : Boolean {
        val data = store_snapshot.child("info").getValue(StoreInfo::class.java)
        if(data == null) return false
        storeInfo = data
        return true
    }

    fun getMenu(menu_snapshot: DataSnapshot) :Boolean {
        for (menu_data in menu_snapshot.child("menu").children) {
            //Log.d("확인 BookActivityBuilder.getMenu", "for문")
            val product = menu_data.getValue(_Menu::class.java)
            //Log.d("확인 BookActivityBuilder.getMenu", "for문2")
            if(product == null) return false
            //Log.d("확인 BookActivityBuilder.getMenu", "for문3")

            //Log.d("확인 BookActivityBuilder.getMenu", "for문4")
            var ingredients = arrayListOf<_Ingredient>()
            for (ingredient_data in menu_data.child("ingredients").children) {
                //Log.d("확인 BookActivityBuilder.getMenu", "for문5")
                val ing = ingredient_data.getValue(_Ingredient::class.java)
                //Log.d("확인 BookActivityBuilder.getMenu", "for문6")
                if(ing == null) return false
                //Log.d("확인 BookActivityBuilder.getMenu", "for문7")
                ingredients.add(ing)
            }

            //Log.d("확인 BookActivityBuilder.getMenu", "for문8")
            menus.add(MenuData(product, ingredients))
            //Log.d("확인 BookActivityBuilder.getMenu", "for문9")
        }
        //Log.d("확인 BookActivityBuilder.getMenu", "for문 끝")

        return true
    }

    fun getBookTime(booktime_snapshot : DataSnapshot) : Boolean{
        val timeMap = hashMapOf<String, Boolean>()
        for (floor in booktime_snapshot.children) {
            for (time in floor.children) {
                val occupy = time.child("BookInfo").getValue(IsFull::class.java) ?: return false
                val time_name = time.key.toString()
                if(occupy.current == 0) {
                    timeMap.put(time_name, true)
                } else {
                    timeMap.put(time_name, false)
                }
            }
        }
        time_list = ArrayList(timeMap.keys)
        time_list = ArrayList(time_list.sortedWith(compareBy ({ it.contains("오후") }, {it})))
        Log.d("확인 bookActivityBuilder timeList 확인", "${time_list}")
        book_check_list = arrayListOf()
        for (book_status in time_list) {
            book_check_list.add(timeMap[book_status]!!)
        }
        Log.d("확인 bookActivityBuilder book_check_list 확인", "${book_check_list}")

        Log.d("bookActivityBuilder", "${storeInfo}")
        for (menu in menus) {
            Log.d("bookActivityBuilder", "${menu.product} : ${menu.image_url} , ${menu.price}, ${menu.product_exp}, ${menu.ingredients}")
        }

        Log.d("bookActivityBuilder", "${time_list}")
        Log.d("bookActivityBuilder", "${book_check_list}")

        book_time = BookTime(time_list, book_check_list)
        return true
    }

    fun getStoreTable(table_info_snapshot : DataSnapshot) : Boolean{
        var floor_table = hashMapOf<String, Table>()
        floor_tables = hashMapOf()
        for (floor in table_info_snapshot.children) {
            Log.d("확인 getStoreTable ",floor.key.toString())
            val floor_name = floor.key.toString()
            for (table in floor.children) {
                val data = table.getValue(_Table::class.java)
                if(data == null) return false
                Log.d("확인 getStoreTable ",table.key.toString())
                floor_table.put(table.key.toString(), Table(table.key.toString(), floor_name, data))
            }
            floor_tables.put(floor_name, floor_table)
        }


        return true
    }
}

@IgnoreExtraProperties
data class _Menu(
        val image : String? = null,
        val product : String? = null,
        val price : Int? = null,
        val product_exp : String? = null
)

data class _Ingredient(
        val ing : String? = null,
        val country : String? = null
) : Serializable

@IgnoreExtraProperties
data class IsFull(val current : Int? = null, val max : Int? = null)

@IgnoreExtraProperties
data class _Table(
        val x : Double? = null,
        val y : Double? = null,
        val width : Int? = null,
        val height : Int? = null,
        val capacity : Int? = null,
        val shape : String? = null
)