package com.example.myapplication.bookActivity

import com.example.myapplication._Ingredient
import com.example.myapplication._Menu
import java.io.Serializable

class MenuData(var product : String, var image_url : String, var price : Int, var product_exp: String, var ingredients : ArrayList<_Ingredient>) : Serializable {
    constructor(_menu: _Menu, ingredients: ArrayList<_Ingredient>):this(_menu.product!!, _menu.image!!,  _menu.price!!, _menu.product_exp!!, ingredients)
}