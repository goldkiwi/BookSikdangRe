package com.dsna19.capstone.Adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dsna19.capstone.Dataclass.SubMessage

class MessageAdapter (
    var context: Context,
    var message_group : ArrayList<SubMessage>
) : RecyclerView.Adapter<MessageAdapter.Holder>()
{
    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }
}