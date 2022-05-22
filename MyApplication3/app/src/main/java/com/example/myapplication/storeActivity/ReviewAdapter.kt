package com.example.myapplication.storeActivity

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class ReviewAdapter(
    var context: Context,
    var review_list : ArrayList<Review>
) : RecyclerView.Adapter<ReviewAdapter.Holder>()
{
    companion object {
        val USER_RATING_IMAGES = arrayListOf(
                R.drawable.rating_bad,
                R.drawable.rating_bad,
                R.drawable.rating_soso,
                R.drawable.rating_soso,
                R.drawable.rating_good,
                R.drawable.rating_good
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.Holder, position: Int) {
        val review = review_list[position]

        holder.user_name.text = review.user_name
        holder.rating_star.setImageResource(USER_RATING_IMAGES[review.rating!!])
        holder.date.text = review.date
        if(review.image!!.compareTo("NULL") != 0) {
            holder.review_image.visibility = View.VISIBLE
            Glide.with(context).load(review.image).into(holder.review_image)
        }
        holder.user_comment.text = review.comment

        if(review.recomment!!.compareTo("NULL") != 0) {
            holder.owner_layout.visibility = View.VISIBLE
            holder.owner_review.text = review.recomment
        }
    }

    override fun getItemCount(): Int {
        return review_list.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var user_name : TextView
        var rating_star : ImageView
        var date : TextView
        var review_image : ImageView
        var user_comment : TextView

        var owner_layout : LinearLayout
        var owner_review : TextView

        init {
            user_name = itemView.findViewById(R.id.user_name)
            rating_star = itemView.findViewById(R.id.rating_star)
            date = itemView.findViewById(R.id.date)

            review_image = itemView.findViewById(R.id.review_image)
            review_image.visibility = View.GONE

            user_comment = itemView.findViewById(R.id.user_comment)
            owner_layout = itemView.findViewById(R.id.owner_layout)

            owner_layout.visibility = View.GONE
            owner_review = itemView.findViewById(R.id.owner_review)
        }
    }
}