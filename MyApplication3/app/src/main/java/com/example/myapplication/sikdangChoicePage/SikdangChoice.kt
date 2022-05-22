package com.example.myapplication.sikdangChoicePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.MapActivity
import com.example.myapplication.R

//SikdangMainCatAdapter에서 쓰임
//선택된 카테고리, 카테고리 리스트, 거리를 받음
class SikdangChoice : AppCompatActivity() {
    //리스트는 SikdangChoiceCatAdapter 클래스의 inner class인 Holder 클래스의 bind()함수에서 칵 카테고리의 toggle 버튼으로 채워준다
    private lateinit var sikdangChoice_catLine : RecyclerView
    private lateinit var sikdangChoiceCatAdapter: SikdangChoiceCatAdapter

    private lateinit var sikdangChoiceMenuViewPager : ViewPager2//메뉴부분 뷰페이저
    private lateinit var sikdangChoiceMenuViewPagerAdapter: SikdangChoiceMenuViewPagerAdapter

    lateinit var sikdangChoice_distET : EditText
    lateinit var range_from_text : EditText
    lateinit var find_from_map : Button
    lateinit var find_from_text : Button

    var map_range : Int = 0
    var dist_range : Int = 0
    var selectedCatIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sikdangchoice)

        init_data()
        init_ui()
        movePage(selectedCatIndex)
    }

    private fun init_data() {
        dist_range = intent.getIntExtra("dist", 1000)
        map_range = dist_range
        selectedCatIndex = intent.getIntExtra("cat", 0)


        Log.d("sikdangChoice", "${dist_range} ${map_range} ${selectedCatIndex}")
    }

    private fun init_ui() {
        sikdangChoiceMenuViewPager = findViewById(R.id.sikdangChoiceMenuViewPager)
        sikdangChoiceMenuViewPagerAdapter = SikdangChoiceMenuViewPagerAdapter(this, sikdangChoiceMenuViewPager, selectedCatIndex, dist_range)
        sikdangChoiceMenuViewPager.adapter = sikdangChoiceMenuViewPagerAdapter

        sikdangChoice_catLine = findViewById(R.id.sikdangChoice_catLine)
        sikdangChoiceCatAdapter = SikdangChoiceCatAdapter(this, selectedCatIndex)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sikdangChoice_catLine.adapter = sikdangChoiceCatAdapter
        sikdangChoice_catLine.layoutManager = linearLayoutManager

        sikdangChoiceCatAdapter.setRecylerView(sikdangChoice_catLine)
        sikdangChoiceCatAdapter.setViewPage(sikdangChoiceMenuViewPagerAdapter)

        sikdangChoiceMenuViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                movePage(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        sikdangChoice_distET = findViewById(R.id.sikdangChoice_distET)
        sikdangChoice_distET.imeOptions = EditorInfo.IME_ACTION_DONE
        sikdangChoice_distET.setOnEditorActionListener (object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    val dist = sikdangChoice_distET.text.toString().toInt()
                    range_from_text.setText(dist.toString())
                    var intent = Intent(this@SikdangChoice, MapActivity::class.java)
                    intent.putExtra("range", dist)
                    dist_range = dist
                    startActivity(intent)
                    return true
                }
                return false
            }
        })

        find_from_map = findViewById(R.id.find_from_map)
        find_from_map.setOnClickListener {
            var dist=0
            if(sikdangChoice_distET.text.toString()==""){
                dist = 200
            }
            else dist = sikdangChoice_distET.text.toString().toInt()
            var intent = Intent(this@SikdangChoice, MapActivity::class.java)
            intent.putExtra("range", dist)
            map_range = dist
            startActivity(intent)
        }

        range_from_text = findViewById(R.id.range_from_text)
        range_from_text.imeOptions = EditorInfo.IME_ACTION_DONE
        range_from_text.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val dist = range_from_text.text.toString().toInt()
                    map_range = dist
                    dist_range = dist
                    sikdangChoiceMenuViewPagerAdapter.updateFragment(-1, dist_range)
                    return true
                }
                return false
            }
        })

        find_from_text = findViewById(R.id.find_from_text)
        find_from_text.setOnClickListener {
            val dist = range_from_text.text.toString().toInt()
            map_range = dist
            sikdangChoiceMenuViewPagerAdapter.updateFragment(-1, dist)
        }
    }
    private fun movePage(position : Int) {
        sikdangChoiceMenuViewPagerAdapter.set_current_page(position)
        sikdangChoiceCatAdapter.pageChanged(position)
        (sikdangChoice_catLine.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
    }
}