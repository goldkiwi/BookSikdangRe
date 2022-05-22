package com.example.myapplication.bookActivity

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.ActivityChooserView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class MenuTableRVAdapter (
        val context : Context,
        var baskets : HashMap<String, ChoiceItem>,
        val callbackListener : MenuFragment
        ) : RecyclerView.Adapter<MenuTableRVAdapter.Holder>()
{
    var basket_name_list : ArrayList<String>
    init {
        basket_name_list = ArrayList(baskets.keys)
        basket_name_list.sortedBy { it }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmenu_eachtable, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val basket_name = basket_name_list[position]

        holder.table_name.text = basket_name
        holder.baskets.adapter = ItemRVAdapter(context, baskets[basket_name]!!)
        var basket_layout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.baskets.layoutManager = basket_layout
        holder.baskets.setHasFixedSize(true)

        holder.itemView.setOnClickListener {
            callbackListener.setTable(basket_name)
        }
    }

    override fun getItemCount(): Int {
        return baskets.size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var table_name : TextView
        var baskets : RecyclerView

        init {
            table_name = itemView.findViewById(R.id.tableNumTV)
            baskets = itemView.findViewById(R.id.tableMenuRV)
        }
    }

    inner class ItemRVAdapter(
            val context: Context,
            var items : ChoiceItem
    ) : RecyclerView.Adapter<ItemRVAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(context).inflate(R.layout.bookmenu_selectedline, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val item = items.choiced_Items[position]

            holder.menuName.text = item.product
            holder.menuNum.text = item.cnt.toString()
        }

        override fun getItemCount(): Int {
            return items.size()
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var menuName : TextView
            var menuNum : TextView

            init {
                menuName = itemView.findViewById(R.id.menuNameLineTV)
                menuNum = itemView.findViewById(R.id.menuNumLineTV)
            }
        }
    }
}