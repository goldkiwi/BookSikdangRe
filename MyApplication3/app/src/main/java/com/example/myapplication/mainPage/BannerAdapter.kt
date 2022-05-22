package com.example.myapplication.mainPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataclass.Banner

class BannerAdapter(var mContext : Context?, var mBannerList: ArrayList<Banner>)
    :RecyclerView.Adapter<BannerAdapter.ViewHolder>()
{
    inner class ViewHolder (itemview : View) : RecyclerView.ViewHolder(itemview) {
        var banner_image : ImageView = itemview.findViewById(R.id.banner)
        var id : TextView = itemview.findViewById(R.id.banner_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val banner = mBannerList[position]

        holder.banner_image.setImageResource(banner.imageUrl)
        holder.id.text = banner.data
    }

    override fun getItemCount(): Int {
        return mBannerList.size
    }
}