package com.example.myapplication.storeActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.google.firebase.database.IgnoreExtraProperties


class StoreMenuAdapter (
    var context: Context,
    var storeMenuList : ArrayList<StoreMenu>
) : RecyclerView.Adapter<StoreMenuAdapter.Holder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.store_menu_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val storeMenu = storeMenuList[position]

        holder.product.text = storeMenu.product
        holder.product_exp.text = storeMenu.product_exp
        holder.price.text = storeMenu.price.toString()
        Glide.with(context).load(storeMenu.image).into(holder.menu_image)
    }

    override fun getItemCount(): Int {
        return storeMenuList.size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var product : TextView
        var product_exp : TextView
        var price : TextView
        var menu_image : ImageView

        init {
            product = itemView.findViewById(R.id.product)
            product_exp = itemView.findViewById(R.id.product_exp)
            price = itemView.findViewById(R.id.price)
            menu_image = itemView.findViewById(R.id.menu_image)
        }
    }
}

@IgnoreExtraProperties
data class StoreMenu(
    val image : String? = null,
    val product : String? = null,
    val product_exp : String? = null,
    val price : Int? = 0
)