package com.example.myapplication.mypage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.google.firebase.database.ValueEventListener

class ReadNoticeFragment(
    var Message: NoticeMessage,
    var listener: NotificationActivity
        ): Fragment()
{
    lateinit var message_header : TextView
    lateinit var message : TextView
    lateinit var message_image : ImageView
    lateinit var message_tail : TextView

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            completed()
        }
    }

    private fun completed() {
        listener.back()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_read_notice, container, false)
        listener.loading.visibility = View.GONE
        listener.title.text = Message.title

        listener.finish.setOnClickListener { completed() }
        message_header = view.findViewById(R.id.message_header)
        message_header.text = Message.message_header

        message = view.findViewById(R.id.message)
        message.text = Message.message

        message_image = view.findViewById(R.id.message_image)
        if(Message.message_image != null) {
            Glide.with(requireContext()).load(Message.message_image).into(message_image)
        }
        message_tail = view.findViewById(R.id.message_tail)
        message_tail.text = Message.message_tail
        return view
    }

}