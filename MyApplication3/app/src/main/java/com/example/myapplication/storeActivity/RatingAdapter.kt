package com.example.myapplication.storeActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RatingAdapter (
    var context: Context
) : RecyclerView.Adapter<RatingAdapter.Holder>()
{
    var current = -1
    val ratingSet = arrayListOf("1점", "2점", "3점", "4점", "5점")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.review_rating_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val rating = ratingSet[position]

        holder.rating.isChecked = false
        holder.rating.textOn = rating
        holder.rating.textOff = rating
        holder.rating.text = rating

        if(current == position) {
            holder.rating.isChecked = true
        }

        holder.rating.setOnClickListener {
            notifyDataSetChanged()
            current = position
            holder.rating.isChecked = true
        }
    }

    fun getRating() : Int {
        return current + 1
    }

    override fun getItemCount(): Int {
        return ratingSet.size
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var rating : ToggleButton
        init {
            rating = itemView.findViewById(R.id.rating)
        }
    }
}