package com.example.myapplication.sikdangChoicePage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.mainPage.CatList

class SikdangChoiceCatAdapter (
    var context: Context,
    var current_page : Int
) : RecyclerView.Adapter<SikdangChoiceCatAdapter.Holder>()
{
    var catories = CatList.getInstance()
    private lateinit var viewPager : SikdangChoiceMenuViewPagerAdapter
    private lateinit var recycler : RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.sikdangchoice_cat, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val catory = catories[position]
        holder.sikdangChoice_toggleButton.text = catory
        holder.sikdangChoice_toggleButton.textOn = catory
        holder.sikdangChoice_toggleButton.textOff = catory
        holder.sikdangChoice_toggleButton.isChecked = false
        if (current_page == position) {
            holder.sikdangChoice_toggleButton.isChecked = true
        }
        holder.sikdangChoice_toggleButton.setOnClickListener {
                pageChanged(position)
                viewPager.set_current_page(current_page)
                (recycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(current_page, 0)
            }
        }

    public fun setViewPage(vp:SikdangChoiceMenuViewPagerAdapter) {
        viewPager = vp
    }

    fun setRecylerView(rv : RecyclerView) {
        recycler = rv
    }

    fun pageChanged(new_page : Int) {
        val old = current_page
        current_page = new_page
        this.notifyItemChanged(old)
        this.notifyItemChanged(current_page)
    }

    override fun getItemCount(): Int {
        return CatList.getInstance().size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var sikdangChoice_toggleButton : ToggleButton

        init {
            sikdangChoice_toggleButton = itemView.findViewById(R.id.sikdangchice_toggleButton)
        }
    }
}