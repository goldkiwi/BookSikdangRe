package com.example.myapplication.storeActivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication._Menu
import com.example.myapplication.dataclass.StoreInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuFragment(var storeInfo: StoreInfo) : Fragment() {

    lateinit var rv_Menu: RecyclerView
    lateinit var store_menu_list : ArrayList<StoreMenu>
    lateinit var storeMenuAdapter: StoreMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_menu, container, false)
        init_ui(view)
        getFromDB()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init_ui(view : View) {
        rv_Menu = view.findViewById(R.id.rv_menu)
        store_menu_list = arrayListOf()
        storeMenuAdapter = StoreMenuAdapter(requireContext(), store_menu_list)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_Menu.adapter = storeMenuAdapter
        rv_Menu.layoutManager = linearLayoutManager
    }

    private fun getFromDB() {
        FirebaseDatabase.getInstance().getReference("Restaurants")
            .child(storeInfo.store_type!!)
            .child(storeInfo.store_id!!)
            .child("menu")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    store_menu_list.clear()
                    for (menus in snapshot.children) {
                        val menu = menus.getValue(StoreMenu::class.java)
                        if(menu == null) return
                        store_menu_list.add(menu!!)
                    }
                    storeMenuAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("MenuFragment", "${error}")
                }
            })
    }
}