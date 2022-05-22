package com.example.myapplication.storeActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ActionAdapter (
    var context: Context
) : RecyclerView.Adapter<ActionAdapter.Holder>()
{
    private val actionList = arrayListOf("메뉴", "리뷰")
    var current : Int = 0
    lateinit var pagerAdapter : ActionPageViewPagerAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.store_action_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val actionPage = actionList[position]
        holder.action_page.isChecked = false
        holder.action_page.text = actionPage
        holder.action_page.textOff = actionPage
        holder.action_page.textOn = actionPage
        if (current == position) {
            holder.action_page.isChecked = true
        }
        holder.action_page.setOnClickListener {
            notifyItemChanged(current)
            current = position
            holder.action_page.isChecked = true
            pagerAdapter.setPagerPost(current)
        }
    }

    override fun getItemCount(): Int {
        return actionList.size
    }

    fun setViewPagerAdapeter(vp : ActionPageViewPagerAdapter) {
        pagerAdapter = vp
    }

    fun scrollPosition() {
        current = pagerAdapter.currentPage()
        notifyDataSetChanged()
    }

    inner class Holder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        var action_page : ToggleButton = itemview.findViewById(R.id.action_page)
    }


}