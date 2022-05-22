package com.example.myapplication.sikdangChoicePage

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.mainPage.CatList

class SikdangChoiceMenuViewPagerAdapter (
    fa : FragmentActivity,
    var viewPager: ViewPager2,
    var current_page : Int,
    var range : Int
) : FragmentStateAdapter(fa)
{
    var fragmentList : List<SikdangChoiceMenuFragment>

    init {
        fragmentList = CatList.getInstance().map {
            catory -> SikdangChoiceMenuFragment(catory, range)
        }
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("createFragment", "${fragmentList.size}, ${fragmentList[position].catory}")
        return fragmentList[position]
    }


    fun set_current_page(position: Int) {
        current_page = position
        viewPager.setCurrentItem(position, true)
    }

    fun updateFragment(position : Int, range : Int) {
        if(this.range == range) return

        this.range = range
        if(position != -1)
            current_page = position
        fragmentList[current_page].updateMenu(range)
    }
}