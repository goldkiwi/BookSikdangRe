package com.example.myapplication.bookActivity

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class TableDirectoryAdapter (
        val context: Context,
        val stock_list : HashMap<String, ChoiceItem>
    ) : RecyclerView.Adapter<TableDirectoryAdapter.TableHolder>()
{
    lateinit var table_names : ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableHolder {
        table_names = ArrayList(stock_list.keys)
        table_names.sortedBy { it }
        val view = LayoutInflater.from(context).inflate(R.layout.pay_page_table_item, parent, false)
        return TableHolder(view)
    }

    override fun onBindViewHolder(holder: TableHolder, position: Int) {
        val table_name = table_names[position]!!

        holder.table_name.text = table_name
        holder.recyclerView.adapter = MenuAdapter(context, stock_list[table_name]!!)
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.recyclerView.setHasFixedSize(true)

        holder.table_name.setOnClickListener {
            if(holder.recyclerView.visibility == View.GONE) {
                holder.recyclerView.visibility = View.VISIBLE
            } else if(holder.recyclerView.visibility == View.VISIBLE) {
                holder.recyclerView.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return stock_list.size
    }

    inner class TableHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var table_name : TextView
        var recyclerView : RecyclerView

        init {
            table_name = itemView.findViewById(R.id.table_name)
            recyclerView = itemView.findViewById(R.id.table_items)
        }
    }

    inner class MenuAdapter(
        val context: Context,
        var menus: ChoiceItem
    ) : RecyclerView.Adapter<MenuAdapter.MenuHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.pay_page_table_menu_item, parent, false)
            return MenuHolder(view)
        }

        override fun onBindViewHolder(holder: MenuHolder, position: Int) {
            val menu = menus.choiced_Items[position]
            holder.menu_name.text = menu.product
            holder.menu_count.text = menu.cnt.toString()
        }

        override fun getItemCount(): Int {
            return menus.size()
        }

        inner class MenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var menu_name : TextView
            val menu_count : TextView

            init {
                menu_name = itemView.findViewById(R.id.menu_name)
                menu_count = itemView.findViewById(R.id.menu_count)
            }
        }
    }
}