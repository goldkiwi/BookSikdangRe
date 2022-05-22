package com.example.myapplication.storeActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.dataclass.StoreInfo

class ActionPageViewPagerAdapter (
    fa : FragmentActivity,
    var view_pager : ViewPager2,
    var actionAdapter: ActionAdapter
) : FragmentStateAdapter(fa)
{
    var fragmentList : ArrayList<Fragment> = arrayListOf()
    // actionList = [메뉴, 리뷰]

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        val pos = actionAdapter.current
        view_pager.setCurrentItem(pos, true)
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }


    fun currentPage() : Int{
        return view_pager.currentItem
    }

    fun setPagerPost(position: Int) {
        view_pager.setCurrentItem(position, true)
    }
}