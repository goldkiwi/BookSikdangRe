package com.example.myapplication.bookActivity

import android.content.Intent
import android.graphics.drawable.shapes.PathShape
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.android.gms.common.internal.Objects
import java.util.concurrent.Semaphore

class MenuFragment (val selectedTables : HashMap<String, ArrayList<Table>>) : Fragment(){
    lateinit var menu_list: ArrayList<Pair<MenuData, Int>>
    lateinit var menuListRVAdapter : MenuListRVAdapter
    lateinit var menuListRV : RecyclerView
    lateinit var menu_data : ArrayList<MenuData>

    lateinit var menuTableRVAdapter: MenuTableRVAdapter
    lateinit var tableRV : RecyclerView
    lateinit var baskets : HashMap<String, ChoiceItem>

    lateinit var table_line : TextView
    lateinit var total_price : TextView
    lateinit var complete_button : Button
    lateinit var shopping_cart : ImageView

    var current_table : String? = null
    var price : Int= 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.bookmenu_fragment, container, false)
        menu_data = (requireActivity() as BookActivity).menuList
        init_UI(view)
        init_data()

        menuListRVAdapter = MenuListRVAdapter(requireContext(), menu_list, this)
        val menuListLayout =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        menuListRV.layoutManager = menuListLayout
        menuListRV.adapter = menuListRVAdapter
        menuListRV.setHasFixedSize(true)

        menuTableRVAdapter = MenuTableRVAdapter(requireContext(), baskets, this)
        val menu_table_layout = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tableRV.layoutManager = menu_table_layout
        tableRV.adapter = menuTableRVAdapter
        tableRV.setHasFixedSize(true)

        return view
    }



/* method for Listener */
    fun setTable(table_name : String) {
        current_table = table_name
        table_line.text = table_name
        menuListRV.adapter!!.notifyItemRangeChanged(0, menuListRV.adapter!!.itemCount)
        updateMenuBar()
    }

    private fun init_UI(view: View) {
        table_line = view.findViewById(R.id.tNumText)
        table_line.text = (requireActivity() as BookActivity).storeInfo.store_name
        total_price = view.findViewById(R.id.priceTV)
        menuListRV = view.findViewById(R.id.menuListRV)
        tableRV = view.findViewById(R.id.tableRV)
        complete_button = view.findViewById(R.id.menuSelectCompleteBtn)
        shopping_cart = requireActivity().findViewById(R.id.shopping_cart)
        shopping_cart.visibility = View.VISIBLE
        shopping_cart.setOnClickListener {
            showDialog()
        }
        complete_button.setOnClickListener {
            resultForPay()
        }
    }



    fun addMessage(menu : MenuData) {
        if(!check_table_selected()) return
        var basket = baskets[current_table]!!
        basket.addItem(menu.product, menu.price)
        updateMenuBar()
        updateTable()
        updatePrice(menu.price)
    }

    private fun updatePrice(menu_price: Int) {
        price += menu_price
        total_price.text = price.toString()
    }

    fun removeMessage(menu : MenuData) {
        if(!check_table_selected()) return
        var basket = baskets[current_table]!!
        val done = basket.removeItem(menu.product)
        updateMenuBar()
        updateTable()
        if(!done) return
        updatePrice(-menu.price)
    }

// private function for Fragment Activity

    private fun showDialog() {
        ShoppingDialog(requireContext(),(requireActivity() as BookActivity).storeInfo.store_name!!,baskets)!!.show()
    }
    private fun init_data() {
        baskets = hashMapOf()
        for (floor_name in selectedTables.keys) {
            for(table in selectedTables[floor_name]!!) {
                baskets.put("${floor_name}:${table.id}", ChoiceItem())
            }
        }

        menu_list = arrayListOf()
        for (data in menu_data) {
            menu_list.add(Pair(data, 0))
        }
    }

    private fun updateTable() {
        menuTableRVAdapter.notifyItemRangeChanged(0,menuTableRVAdapter.itemCount )
    }

    private fun check_table_selected() : Boolean{
        if(current_table == null) Toast.makeText(context, "Table을 선택해주세요!", Toast.LENGTH_SHORT).show()
        return current_table != null
    }

    private fun updateMenuBar() {
        menu_list.clear()
        var basket = baskets[current_table]!!
        for(data in menu_data) {
            if(!basket.product_exist(data.product)) menu_list.add(Pair(data, 0))
            else menu_list.add(Pair(data, basket.getItem(data.product)!!.cnt))
        }
        menuListRVAdapter.notifyItemRangeChanged(0, menuListRVAdapter.itemCount)
    }


    private fun resultForPay() {
        if(!is_all_basket_selected()) return

        var intent = Intent(context, PayActivity::class.java)
        intent.putExtra("stocks", baskets)
        intent.putExtra("price", price)
        intent.putExtra("store_info", (requireActivity() as BookActivity).storeInfo)
        intent.putExtra("bookTime", (requireActivity() as BookActivity).selected_time)
        startActivity(intent)
    }

    private fun get_result(): ChoiceItem {
        var result : ChoiceItem = ChoiceItem()
        for (floor_name in baskets.keys) {
            result.mergeChoiceItem(baskets[floor_name]!!)
        }

        return result!!
    }

    private fun is_all_basket_selected() : Boolean{
        for (floor_name in baskets.keys) {
            if(baskets[floor_name]!!.isEmpty()) {
                Toast.makeText(context, "폭력 멈춰!!", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }


}