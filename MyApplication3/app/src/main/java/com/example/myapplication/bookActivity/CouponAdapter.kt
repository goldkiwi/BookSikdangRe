package com.example.myapplication.bookActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class CouponAdapter(
        val context: Context,
        var coupon_list: ArrayList<Coupon>,
        var listener: CouponDialog
) : RecyclerView.Adapter<CouponAdapter.Holder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.paypage_coupondialog_couponline, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var coupon = coupon_list[position]

        holder.check_box.isChecked = coupon.used
        holder.coupon_exp.text = coupon.coupon_exp
        holder.min_price.text = coupon.discount
        holder.overlap.text = coupon.type.toString()
        holder.coupon_expire.text = coupon.expire

        holder.check_box.setOnClickListener {
            listener.itemCheck(coupon)
        }

    }

    override fun getItemCount(): Int {
        return coupon_list.size
    }

    inner class Holder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var check_box : CheckBox
        var coupon_exp : TextView
        var min_price : TextView
        var overlap : TextView
        var coupon_expire : TextView

        init {
            check_box = itemView.findViewById(R.id.checkBox)
            coupon_exp = itemView.findViewById(R.id.couponExplanationTV)
            min_price = itemView.findViewById(R.id.minPriceTV)
            overlap = itemView.findViewById(R.id.overlapTV)
            coupon_expire = itemView.findViewById(R.id.couponEXPTV)

        }
    }

}