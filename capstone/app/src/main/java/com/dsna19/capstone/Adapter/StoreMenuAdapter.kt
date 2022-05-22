package com.dsna19.capstone.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dsna19.capstone.Dataclass.StoreItem
import com.dsna19.capstone.Listener.StoreSelectedListener
import com.dsna19.capstone.R
import com.github.florent37.androidslidr.Slidr
import de.hdodenhof.circleimageview.CircleImageView
import net.cachapa.expandablelayout.ExpandableLayout

class StoreMenuAdapter (
    var context: Context,
    var menubar_list : List<String>,
    var menuMap : HashMap<String, List<StoreItem>>,
    val storeSelectedListener : StoreSelectedListener
) : BaseExpandableListAdapter()
{
    override fun getGroupCount(): Int {
        return menubar_list.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return menuMap[menubar_list[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return menubar_list[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return menuMap[menubar_list[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }


    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var menubar_view = convertView
        val menu_bar_name = getGroup(groupPosition) as String

        if(menubar_view == null) {
            val inflater = parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            menubar_view = inflater.inflate(R.layout.store_item_menubar_item, parent, false)
        }

        val menubar_name : TextView= menubar_view!!.findViewById(R.id.menubar_name)
        val menubar_icon : ImageView = menubar_view!!.findViewById(R.id.menubar_icon)
        menubar_name.text = menu_bar_name

        return menubar_view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var store_itemView = convertView
        val storeItem = getChild(groupPosition, childPosition) as StoreItem

        if(store_itemView == null) {
            val inflater = parent!!.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            store_itemView = inflater.inflate(R.layout.store_item_item, null)
        }
        
        child_bind2View(store_itemView!!, storeItem)

        return store_itemView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private fun child_bind2View(childView: View, storeItem: StoreItem) {
        val item_type : TextView = childView.findViewById(R.id.item_type)
        item_type.text = storeItem.item_catory

        val item_price : TextView = childView.findViewById(R.id.item_price)
        item_price.text = storeItem.item_price.toString()

        val detail_layout : ExpandableLayout = childView.findViewById(R.id.detail_layout)
        layout_setting(detail_layout, storeItem.item_name!!)
        val decision_icon : CircleImageView = childView.findViewById(R.id.decision_icon)

        decision_icon.setOnClickListener{
            drawer_toggle(detail_layout)
        }

        val slidr : Slidr = childView.findViewById(R.id.slidr)
        slidr.max = 300f
        slidr.currentValue = 100f
    }

    private fun layout_setting(detailLayout: ExpandableLayout, item: String) {
        val add_item : ImageView = detailLayout.findViewById(R.id.add_item)
        add_item.setOnClickListener {
            Toast.makeText(context, "${item} 아이템 추가", Toast.LENGTH_SHORT).show()
        }

        val drop_item : ImageView = detailLayout.findViewById(R.id.drop_item)
        drop_item.setOnClickListener {
            Toast.makeText(context, "${item} 아이템 감소", Toast.LENGTH_SHORT).show()
        }
    }


    private fun drawer_toggle(layout: ExpandableLayout) {
        if(layout.isExpanded == true) {
            layout.collapse()
        } else {
            layout.expand()
        }
    }
}