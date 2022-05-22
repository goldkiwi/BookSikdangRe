package com.example.myapplication.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bookActivity._coupon

class MyCouponAdapter (
    var context: Context,
    var coupon_list : ArrayList<_coupon>
) : RecyclerView.Adapter<MyCouponAdapter.Holder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_coupon_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val coupon = coupon_list[position]

        holder.coupon_name.text = coupon.coupon_exp
        holder.coupon_exp.text = coupon.coupon_exp
        holder.discount.text = "할인가격 ${coupon.discount} 원"
        holder.min_price.text = "최소주문금액 ${coupon.min_price} 원"
        holder.coupon_expire.text = "완료기간 ${coupon.expire}"
    }

    override fun getItemCount(): Int {
        return coupon_list.size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var coupon_name : TextView
        var coupon_exp : TextView
        var discount : TextView
        var min_price : TextView
        var coupon_expire : TextView

        init {
            coupon_name = itemView.findViewById(R.id.coupon_name)
            coupon_exp = itemView.findViewById(R.id.coupon_exp)
            discount = itemView.findViewById(R.id.discount)
            min_price = itemView.findViewById(R.id.min_price)
            coupon_expire = itemView.findViewById(R.id.coupon_expire)
        }
    }
}