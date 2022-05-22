package com.example.myapplication.bookActivity

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

// Draw
class UsedCouponAdapter(
        val context: Context,
        var used_coupons: ArrayList<Coupon>
) : RecyclerView.Adapter<UsedCouponAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.paypage_usedcouponline, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var coupon = used_coupons[position]
        holder.coupon_explanation.text = coupon.coupon_exp
        holder.coupon_discout.text = coupon.discount
    }

    override fun getItemCount(): Int {
        return used_coupons.size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var coupon_explanation : TextView
        var coupon_discout : TextView

        init {
            coupon_explanation = itemView.findViewById(R.id.UsedCouponExplanation)
            coupon_discout = itemView.findViewById(R.id.usedCouponDiscountTV)
        }
    }
}