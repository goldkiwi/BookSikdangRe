package com.example.myapplication.bookActivity

import java.io.Serializable
import java.lang.StringBuilder

class ChoiceItem : Serializable{
    var choiced_Items : ArrayList<Item> = arrayListOf()

    override fun toString(): String {
        var sb = StringBuilder()
        for (item in choiced_Items) {
            sb.append("${item.product} : ${item.cnt} ,")
        }
        sb.deleteCharAt(sb.lastIndex)
        sb.deleteCharAt(sb.lastIndex)

        return sb.toString()
    }

    fun addItem(product : String, price: Int){
        if(!product_exist(product))
            choiced_Items.add(Item(product, price, 1))
        else
            getItem(product)!!.cnt += 1
    }

    fun isEmpty() = choiced_Items.isEmpty()

    fun removeItem(product: String) : Boolean{
        if(!product_exist(product)) return false
        var item = getItem(product)
        item!!.cnt -= 1
        check_product(item!!)
        return true
    }

    fun getTotalPrice() : Int{
        var price = 0
        for(item in choiced_Items) {
            price += item.price * item.cnt
        }
        return price
    }

    fun getReceipt() : Triple<ArrayList<String>, ArrayList<Int>, Int>{
        var product_list = arrayListOf<String>()
        var cnt_list = arrayListOf<Int>()
        for (item in choiced_Items) {
            product_list.add(item.product)
            cnt_list.add(item.cnt)
        }
        return Triple(product_list, cnt_list, getTotalPrice())
    }

    fun product_exist(product: String) : Boolean{
        for(item in choiced_Items) {
            if(item.product == product) return true
        }
        return false
    }

    fun mergeChoiceItem(other : ChoiceItem) : ChoiceItem{
        for (item in other.choiced_Items) {
            var exist = getItem(item.product)
            if(exist == null) addItem(item.product, item.price)
            else {
                exist.cnt += item.cnt
            }
        }
        return this
    }

    fun size() = choiced_Items.size

    fun getItem(product : String) : Item? {
        for (item in choiced_Items) {
            if(item.product == product) return item
        }
        return null
    }

    private fun check_product(item: Item) : Boolean{
        if(item.cnt == 0) {
            choiced_Items.remove(item)
            return true
        }
        return false
    }
}

class Item(
        val product : String,
        val price : Int,
        var cnt : Int
        ) : Serializable
{
}