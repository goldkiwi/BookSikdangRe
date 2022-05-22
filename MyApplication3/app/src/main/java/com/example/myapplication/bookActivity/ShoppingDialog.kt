package com.example.myapplication.bookActivity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R

class ShoppingDialog (
        context: Context,
        val store_name : String,
        var baskets : HashMap<String, ChoiceItem>
        )
    : Dialog(context)
{
    lateinit var store_id : TextView
    lateinit var content : TextView
    lateinit var price : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_cart_dialog)
        store_id = findViewById(R.id.store_id)
        content = findViewById(R.id.content)
        price = findViewById(R.id.price)
        var payment = ""
        var _price = 0
        store_id.text = store_name
        for (floor_name in baskets.keys) {
            payment += "${floor_name}\n"
            val (item, cnt, table_price) = baskets[floor_name]!!.getReceipt()
            _price += table_price
            for (i in 0 until item.size) {
                payment += "\t\t\t%-60s : %d\n".format(item[i], cnt[i])
            }
            payment +="\n"
        }
        content.text = payment
        price.text = "총가격 : ${_price}"
    }
}