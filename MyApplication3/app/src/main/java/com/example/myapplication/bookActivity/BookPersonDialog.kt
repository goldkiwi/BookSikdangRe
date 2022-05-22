package com.example.myapplication.bookActivity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class BookPersonDialog (
        context: Context,
        val menuResultCallback: TableFloorFragment,
        val table_info : Table
        ) : Dialog(context)
{
    lateinit var personNumRVAdapter: PersonNumRVAdapter
    lateinit var recyclerView: RecyclerView
    var listener = Listener()

    lateinit var cancelButton: Button
    lateinit var menuChoiceButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booktable_person_num_dialog)

        cancelButton = findViewById(R.id.cancelButton)
        menuChoiceButton = findViewById(R.id.menuChoiceButton)

        menuChoiceButton.setOnClickListener {
            if (!listener.isSelected()) {
                Toast.makeText(context, "인원 수를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            menuResultCallback.setResult(table_info)
            this.dismiss()
        }
        cancelButton.setOnClickListener {
            menuResultCallback.invalid(table_info)
            this.dismiss()
        }

        recyclerView = findViewById(R.id.personNumRV)
        recyclerView.setHasFixedSize(true)
        personNumRVAdapter = PersonNumRVAdapter(context, table_info, listener)
        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = personNumRVAdapter
    }

    inner class Listener {
        var currentPosition : Int = -1

        fun holder_notify(position : Int) {
            recyclerView.adapter!!.notifyItemRangeChanged(0, recyclerView.adapter!!.itemCount, "invalid")
            recyclerView.adapter!!.notifyItemChanged(position, "select")
            currentPosition = position
        }

        fun isSelected() : Boolean {
            return currentPosition != -1
        }
    }
}