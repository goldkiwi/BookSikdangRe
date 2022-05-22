package com.example.myapplication

import android.content.Intent
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.bookActivity.BookActivity

import com.example.myapplication.dataclass.LocationWithID
import com.example.myapplication.mainPage.CatorySetAdapter
import com.example.myapplication.mainPage.Sikdang_main
import com.google.firebase.database.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.internal.resource.OverlayImageLoader
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import kotlin.math.hypot
import kotlin.random.Random

val occupies = arrayOf<String>("plenty", "some", "flew", "none")
class MapActivity : AppCompatActivity(),
        OnMapReadyCallback, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener, NaverMap.OnMapClickListener,
        Overlay.OnClickListener
{
    lateinit var locationSource: FusedLocationSource
    val ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100
    lateinit var mNaverMap : NaverMap
    var markerList : ArrayList<Marker> = arrayListOf()
    var isCameraAnimated = false
    var catory : String = "ALL"
    lateinit var lastLocation : LatLng
    lateinit var results : ArrayList<Store>
    lateinit var reference : DatabaseReference

    var infoWindow : InfoWindow? = null
    var range : Int = 0

    lateinit var text_range : EditText
    lateinit var search_btn : Button

    lateinit var rv_catory : RecyclerView
    lateinit var naviAdapter: NaviCatoryAdapter

    override fun onCameraChange(reason: Int, animated: Boolean) {
        isCameraAnimated = animated
    }

    override fun onCameraIdle() {
        if (isCameraAnimated) {
            val mapCenter = mNaverMap.cameraPosition.target
            val distance = distance(lastLocation, mapCenter)
            if (distance > range) {
                lastLocation = mapCenter
            }

            fetchStore(mapCenter, range)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        rv_catory = findViewById(R.id.rv_catory)
        naviAdapter = NaviCatoryAdapter(this, this)
        var linearLayoutManager = GridLayoutManager(this, 2)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_catory.layoutManager = linearLayoutManager
        rv_catory.adapter = naviAdapter

        range = intent.extras!!.getInt("range")
        text_range = findViewById(R.id.text_range)

        search_btn = findViewById(R.id.search_btn)

        reference = FirebaseDatabase.getInstance().getReference("Locations")

        text_range.imeOptions = EditorInfo.IME_ACTION_DONE
        text_range.setOnEditorActionListener (object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(EditorInfo.IME_ACTION_DONE == actionId) {
                    updateRange(text_range)
                    fetchStore(lastLocation, range)

                    return true
                }

                return false
            }
        })

        search_btn.setOnClickListener { updateRange(text_range);fetchStore(lastLocation, range) }


        var mapFragment :MapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        // Naver Map 초기 세팅
        mNaverMap = naverMap
        mNaverMap.maxZoom = 18.0
        mNaverMap.minZoom = 15.0
        locationSource = FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE)
        lastLocation = mNaverMap.cameraPosition.target
        var uiSettings = mNaverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true
        mNaverMap.locationSource = locationSource
        results = arrayListOf()
        // 현재 사용자를 기준으로 setting
        mNaverMap.locationTrackingMode = LocationTrackingMode.Follow

        // naverMap listener 설정

        mNaverMap.addOnCameraChangeListener(this)
        mNaverMap.addOnCameraIdleListener(this)
        //mNaverMap.onMapDoubleTapListener = doubleClickListener

        infoWindow = InfoWindow()
        infoWindow!!.adapter = object :InfoWindow.DefaultViewAdapter(this) {
            override fun getContentView(p0: InfoWindow): View {
                val marker = infoWindow!!.marker
                val store = marker?.tag as Store
                var view = View.inflate(this@MapActivity, R.layout.store_info, null)
                view.findViewById<TextView>(R.id.store_id).text = "상호 : ${store.name}"
                view.findViewById<TextView>(R.id.store_distance).text = "거리 : ${store.distance}m"
                return view
            }
        }
        updateRange(text_range)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ACCESS_LOCATION_PERMISSION_REQUEST_CODE -> {
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)
                return
            }
        }
    }
/*
    val doubleClickListener = object : NaverMap.OnMapDoubleTapListener {
        override fun onMapDoubleTap(p0: PointF, p1: LatLng): Boolean {
            Toast.makeText(this@MapActivity, "Location : ${p1.latitude}, ${p1.longitude}", Toast.LENGTH_SHORT).show()

            return true
        }
    }
*/
    private fun distance(from : LatLng, to : LatLng) : Int {
        var result = floatArrayOf(0f, 0f)
        Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, result)
        return hypot(result[0], result[1]).toInt()
    }

    fun updateMapMakers(storeList : ArrayList<Store>) {
        resetMarkerList()
        for (store in storeList) {
            val mapCenter = mNaverMap.cameraPosition.target
            val distance = distance(mapCenter, store.latLng)
            if (distance > range) continue
            var marker = Marker()
            marker.position = store.latLng
            marker.tag = store
            if ("plenty".equals(store.occupy, ignoreCase = true)) {
                marker.icon = OverlayImage.fromResource(R.drawable.marker_green)
            } else if ("some".equals(store.occupy, ignoreCase = true)) {
                marker.icon = OverlayImage.fromResource(R.drawable.marker_yellow)
            } else if ("flew".equals(store.occupy, ignoreCase = true)) {
                marker.icon = OverlayImage.fromResource(R.drawable.marker_red)
            } else {
                marker.icon = OverlayImage.fromResource(R.drawable.marker_gray)
            }

            marker.anchor = PointF(0.5f, 1.0f)
            marker.map = mNaverMap
            marker.setOnClickListener(this)
            markerList.add(marker)
        }
    }

    override fun onMapClick(p0: PointF, p1: LatLng) {
        if (infoWindow?.marker != null) {
            infoWindow?.close()
        }
    }

    override fun onClick(overlay: Overlay): Boolean {
        if(overlay is Marker) {
            val marker = overlay
            if(marker.infoWindow != null) {
                Log.d("onClick", "${(marker.tag as Store).name} is Double Clicked?")
                val sikdangId = (marker.tag as Store).id
                var intent = Intent(this, BookActivity::class.java)
                val result = BookActivityBuilder(sikdangId, (marker.tag as Store).category, this).build()
                Log.d("Map clicked", "${result}")
                infoWindow?.close()
            } else {
                infoWindow!!.open(marker)
            }

            return true
        }

        return false
    }

    private fun resetMarkerList() {
        if (markerList != null && markerList.size > 0) {
            for (marker in markerList) {
                marker.map = null
            }
            markerList.clear()
        }
    }

    fun fetchStore (location : LatLng, range : Int) {
        resetMarkerList()
        results.clear()

        reference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(locationDB: DataSnapshot) {
                for (data in locationDB.children) {
                    var store_location = data.getValue(com.example.myapplication.dataclass.LocationWithID::class.java)
                    val dis = distance(location, LatLng(store_location!!.Lat!!, store_location!!.Lng!!))
                    if (dis < range) {
                        Log.d("store", store_location.toString())
                        if (catory.compareTo(store_location.store_type!!) == 0 || catory.compareTo("ALL") == 0)
                            results.add(locationToStore(store_location, dis))
                    }
                }
                updateMapMakers(results)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateRange(text : EditText) {
        if(TextUtils.isEmpty(text.text.toString()));
        else {range = text.text.toString().toInt()}
        if(range == 0) {
            Toast.makeText(this, "현재 위치를 가져옵니다. 거리를 입력하세요", Toast.LENGTH_SHORT).show()
            text.text.clear()
            return
        }
        text.text.clear()
        Toast.makeText(this, "현재 거리 반경은 ${range}m 입니다.", Toast.LENGTH_SHORT).show()
    }
}

data class Store (
    val id : String,
    val name : String,
    val latLng : LatLng,
    val occupy : String,
    val distance : Int,
    val category: String) {
}

fun locationToStore(location: LocationWithID, distance: Int): Store {
    val i = occupies.size
    val rand = Random.nextInt(0, i)
    return Store(location.id!!, location.name!!, LatLng(location.Lat!!, location.Lng!!), occupies[rand], distance, location.store_type!!)
}