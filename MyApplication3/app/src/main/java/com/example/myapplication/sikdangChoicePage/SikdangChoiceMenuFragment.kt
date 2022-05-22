package com.example.myapplication.sikdangChoicePage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.INotificationSideChannel
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.naver.maps.geometry.LatLng
import kotlin.math.hypot


class SikdangChoiceMenuFragment(var catory : String, var range : Int) :
        Fragment(), LocationListener {

    private var current_lng : Double = 0.0
    private var current_lat : Double = 0.0
    private lateinit var locationManager  : LocationManager

    private lateinit var loading : ProgressBar

    private lateinit var sikdangChoiceMenuFragmentRecyclerView : RecyclerView
    private lateinit var sikdangChoiceMenuAdapter: SikdangChoiceMenuAdapter

    var sikdangStoreMenuList: ArrayList<SikdangStoreMenu> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        //view 는 리사이클러뷰 하나 들어있는 레이아웃
        var view= inflater.inflate(R.layout.sikdangchoice_menu_fragment, container, false)
        loading = view.findViewById(R.id.loading)

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager


        sikdangChoiceMenuFragmentRecyclerView = view.findViewById(R.id.sikdangChoiceMenuFragmentRecyclerView)
        sikdangChoiceMenuAdapter = SikdangChoiceMenuAdapter(requireContext(), sikdangStoreMenuList)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        sikdangChoiceMenuFragmentRecyclerView.adapter = sikdangChoiceMenuAdapter
        sikdangChoiceMenuFragmentRecyclerView.layoutManager = linearLayoutManager

        if(!sikdangStoreMenuList.isEmpty())
            loading.visibility = View.GONE
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 20.0f, this)

        updateMenu(range)
        //View.setBackgroundResource(bannerImage)

        return view
    }

    fun updateMenu(range : Int) {
        this.range = range

        FirebaseDatabase.getInstance().getReference("Locations")
                .orderByChild("store_type")
                .equalTo(catory)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        sikdangStoreMenuList.clear()
                        for (data in snapshot.children) {

                            val store = data.getValue(SikdangReqMenu::class.java)
                            if(store == null) continue
                            Log.d("update Menu", "${catory} : ${store}")
                            val dist = distance(store!!.Lat!!, store!!.Lng!!)
                            Log.d("update Menu", "${store} : ${dist}")
                            if (dist > range) continue
                            sikdangStoreMenuList.add(SikdangStoreMenu(
                                    store.Lat!!,
                                    store.Lng!!,
                                    store.id!!,
                                    store.name!!,
                                    dist,
                                    store.store_image,
                                    store.store_type!!
                            ))


                        }

                        if (sikdangStoreMenuList.isEmpty()) {
                            loading.visibility = View.INVISIBLE
                        }
                        else loading.visibility = View.GONE
                        sikdangChoiceMenuAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
    }

    private fun distance(lat : Double, lng : Double) : Int {
        var result = floatArrayOf(0f, 0f)
        Location.distanceBetween(current_lat, current_lng, lat, lng, result)
        return hypot(result[0], result[1]).toInt()
    }


    override fun onLocationChanged(location: Location) {
        current_lat = location.latitude
        current_lng = location.longitude
        Log.d("location", "${current_lat}, ${current_lng}")
        updateMenu(range)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(provider: String) {
        TODO("Not yet implemented")
    }

    override fun onProviderDisabled(provider: String) {
        TODO("Not yet implemented")
    }
}