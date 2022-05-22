package com.example.myapplication.mypage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class NoticeAdapter(
        var context: Context,
        var noticeHeaderList : List<NoticeHeader>,
        var listener : NotificationActivity
) : RecyclerView.Adapter<NoticeAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.notice_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val notice = noticeHeaderList[position]

        holder.notice_title.text = notice.title
        holder.notice_date.text = notice.date!!.substringBefore('T')
        Log.d("noticeAdapter", "${notice.title}, ${notice.is_read}")
        if(notice.is_read!!) {
            holder.not_read.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            listener.thisItemSelected(notice.message_id!!)
        }
    }

    override fun getItemCount(): Int {
        return noticeHeaderList.size
    }

    class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var notice_title : TextView
        var notice_date : TextView
        var not_read : ImageView

        init {
            notice_title = itemView.findViewById(R.id.notice_title)
            notice_date = itemView.findViewById(R.id.notice_date)
            not_read = itemView.findViewById(R.id.not_read)
        }
    }

}