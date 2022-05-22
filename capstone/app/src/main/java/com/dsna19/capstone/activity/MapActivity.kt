package com.dsna19.capstone.activity

import android.graphics.Color
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsna19.capstone.Adapter.FindStoreAdapter
import com.dsna19.capstone.Dataclass.Brand
import com.dsna19.capstone.Dataclass.BrandProperty
import com.dsna19.capstone.R
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource

class MapActivity : AppCompatActivity(),
        OnMapReadyCallback,
    NaverMap.OnCameraChangeListener,
    NaverMap.OnCameraIdleListener
{
    private var makerList: ArrayList<Marker> = arrayListOf()
    lateinit var locationSource: FusedLocationSource
    val ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100
    lateinit var mNaverMap : NaverMap
    private var isCamaraAnimated = false
    var currentLocation : LatLng? = null 
    var currentCatory : String? = null

    var my_device_position : LatLng? = null

    lateinit var rv_store_location : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val pair = intent.getSerializableExtra("Location") as Pair<Double, Double>
        currentCatory = intent.getStringExtra("store")
        if(currentCatory == null) currentCatory = "편의점"

        Log.d("onCreate", "${pair.first}, ${pair.second}")
        currentLocation = LatLng(pair.first, pair.second)

        rv_store_location = findViewById(R.id.recycler_view_store_location)
        rv_store_location.adapter = FindStoreAdapter(this, this)
        rv_store_location.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var mapFragment : MapFragment = supportFragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        // Naver Map 초기 세팅
        mNaverMap = naverMap
        mNaverMap.maxZoom = 19.0
        mNaverMap.minZoom = 16.0
        locationSource = FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE)
        mNaverMap.locationSource = locationSource
        if(currentLocation != null) {
            mNaverMap.cameraPosition = CameraPosition(currentLocation!!, 17.0)
        }
        var uiSettings = mNaverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true

        // 현재 사용자를 기준으로 setting
        mNaverMap.locationTrackingMode = LocationTrackingMode.Follow
        mNaverMap.addOnCameraIdleListener(this)
        mNaverMap.addOnCameraChangeListener(this)
        fetchStores()
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

    override fun onCameraIdle() {
        Log.d("onCameraIdle", "called")
        if(isCamaraAnimated) {
            currentLocation = mNaverMap.cameraPosition.target
            fetchStores()
        }
    }

    private fun getFromRegion() {
        Log.d("task", "called")
        val center = GeoLocation(currentLocation!!.latitude, currentLocation!!.longitude)
        val bounds = GeoFireUtils.getGeoHashQueryBounds(
                center,
                1000.0)

        val tasks = arrayListOf<Task<QuerySnapshot>>()
        for (brand in Brand.AllBrands) {
            for (b in bounds) {
                val query = FirebaseFirestore.getInstance().collection("Brand")
                        .document(brand.brand_name)
                        .collection("Location")
                        .orderBy("geo_hash")
                        .startAt(b.startHash)
                        .endAt(b.endHash)

                tasks.add(query.get())
            }
        }

        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener {
                    var matching = arrayListOf<StoreGeo>()

                    for (task in tasks) {
                        val snap = task.result
                        for(doc in snap!!.documents) {
                            Log.d("tasks", "${doc.id}")
                            val lat = doc.getDouble("store_geo.latitude")
                            val lng = doc.getDouble("store_geo.longitude")
                            val realDist = GeoFireUtils.getDistanceBetween(GeoLocation(lat!!, lng!!), center)
                            Log.d("task", "${realDist}")
                            if(realDist <= 1000) {
                                matching.add(StoreGeo(
                                        doc.getString("store_name")!!,
                                        doc.getString("brand_name")!!,
                                        LatLng(lat!!, lng!!),
                                ))
                            }
                        }
                    }
                    resetMarkerList()
                    matching.forEach {
                        Log.d("matching", "${it.geo}")
                        val marker = Marker()
                        marker.position = it.geo
                        marker.icon = OverlayImage.fromResource(R.drawable.location)
                        marker.iconTintColor = Color.parseColor(BrandProperty.GetPropetyByBrand(it.brand_name)!!.brand_primary_color)
                        marker.captionColor = Color.parseColor(BrandProperty.GetPropetyByBrand(it.brand_name)!!.brand_text_color)
                        marker.captionText = it.brand_name
                        marker.anchor = PointF(0.5f, 1.0f)
                        marker.map = mNaverMap
                        marker.isIconPerspectiveEnabled = true
                        makerList.add(marker)
                    }
                }

    }

    private fun getFromBrand(brand_name: String) {
        Log.d("task", "called")
        val center = GeoLocation(currentLocation!!.latitude, currentLocation!!.longitude)
        val bounds = GeoFireUtils.getGeoHashQueryBounds(
            center,
        1000.0)

        val tasks = arrayListOf<Task<QuerySnapshot>>()
        for (b in bounds) {
            val query = FirebaseFirestore.getInstance().collection("Brand")
                        .document(brand_name)
                        .collection("Location")
                        .orderBy("geo_hash")
                        .startAt(b.startHash)
                        .endAt(b.endHash)

            tasks.add(query.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                var matching = arrayListOf<StoreGeo>()

                for (task in tasks) {
                    val snap = task.result
                    for(doc in snap!!.documents) {
                        Log.d("tasks", "${doc.id}")
                        val lat = doc.getDouble("store_geo.latitude")
                        val lng = doc.getDouble("store_geo.longitude")
                        val realDist = GeoFireUtils.getDistanceBetween(GeoLocation(lat!!, lng!!), center)
                        Log.d("task", "${realDist}")
                        if(realDist <= 1000) {
                            matching.add(StoreGeo(
                                doc.getString("store_name")!!,
                                doc.getString("brand_name")!!,
                                LatLng(lat!!, lng!!),
                            ))
                        }
                    }
                }
                resetMarkerList()
                matching.forEach {
                    Log.d("matching", "${it.geo}")
                    val marker = Marker()
                    marker.position = it.geo
                    marker.icon = OverlayImage.fromResource(R.drawable.location)
                    marker.iconTintColor= Color.parseColor(BrandProperty.GetPropetyByBrand(it.brand_name)!!.brand_primary_color)
                    marker.captionText = it.brand_name
                    marker.captionColor = Color.parseColor(BrandProperty.GetPropetyByBrand(it.brand_name)!!.brand_text_color)
                    marker.anchor = PointF(0.5f, 1.0f)
                    marker.isIconPerspectiveEnabled = true
                    marker.map = mNaverMap
                    makerList.add(marker)
                }
            }
    }

    override fun onCameraChange(p0: Int, p1: Boolean) {
        isCamaraAnimated = p1
    }

    private fun resetMarkerList() {
        makerList.forEach {
            it.map = null
        }
        makerList.clear()
    }

    fun listen(brandName: String) {
        currentCatory = brandName
        fetchStores()
    }

    private fun fetchStores() {
        if(currentCatory == "편의점")
            getFromRegion()
        else
            getFromBrand(currentCatory!!)
    }

    data class StoreGeo(
        val store_name : String,
        val brand_name : String,
        val geo : LatLng,
    )
}