package com.example.myapplication.bookActivity

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class MenuListRVAdapter (
        val context : Context,
        var menu_list : ArrayList<Pair<MenuData, Int>>,
        val itemChangeLister : MenuFragment
): RecyclerView.Adapter<MenuListRVAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmenu_menubar, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val stock = menu_list[position]

        Glide.with(context).load(stock.first.image_url).into(holder.menuImage)
        holder.menuName.text = stock.first.product
        holder.menuPrice.text = stock.first.price.toString()
        holder.menuExp.text = "${stock.first.product_exp} 원"
        holder.menuCount.text = stock.second.toString()
        holder.increaseItem.setOnClickListener {
            itemChangeLister.addMessage(stock.first)
        }

        holder.decreaseItem.setOnClickListener {
            itemChangeLister.removeMessage(stock.first)
        }
    }

    override fun getItemCount(): Int {
        return menu_list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menuImage : ImageView
        var menuName : TextView
        var menuExp : TextView
        var menuPrice : TextView
        var menuCount : TextView
        var increaseItem : TextView
        var decreaseItem : TextView

        init {
            menuImage = itemView.findViewById(R.id.menuImage)
            menuName = itemView.findViewById(R.id.menuNameTV)
            menuExp = itemView.findViewById(R.id.menuExpTV)
            menuPrice = itemView.findViewById(R.id.menuPriceTV)
            menuCount = itemView.findViewById(R.id.menuCountText)
            increaseItem = itemView.findViewById(R.id.plusTV)
            decreaseItem = itemView.findViewById(R.id.minusTV)
        }
    }
}