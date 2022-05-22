package com.dsna19.capstone.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.dsna19.capstone.Fragment.FindBrandFragment
import com.dsna19.capstone.Fragment.HomeFragment
import com.dsna19.capstone.Fragment.ProductSearchFragment
import com.dsna19.capstone.Fragment.ProfileFragment
import com.dsna19.capstone.R
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOError
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    val REQUEST_FINE_LOCATION = 2
    lateinit var function_name : TextView
    lateinit var edit_location : RelativeLayout
    lateinit var main_bottom_bar : BottomNavigationView
    lateinit var location_text : TextView

    private var current_lng : Double = 0.0
    private var current_lat : Double = 0.0
    lateinit var locationClient: FusedLocationProviderClient

    var selectedFragment : Fragment? = HomeFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init_ui()
        init_setting()
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, selectedFragment!!)
                .commit()

        /*
        var keys = arrayListOf<String>()
        var list = arrayListOf<Item>()
        var result = arrayListOf<HashMap<String, Any>>()
        val read = FirebaseFirestore.getInstance().collection("Product")
                .get()
                .addOnSuccessListener { it ->
                    for (data in it.documents) {
                        val product = data.toObject(Item::class.java)
                        if (product == null) continue
                        list.add(product)
                        keys.add(product.item_name!!)
                    }

                    for (item in list) {
                        var hash = HashMap<String, Any>()
                        if (item.item_sale_info == null) {
                            hash.put("item_name", item.item_name!!)
                            hash.put("item_catory", item.item_catory!!)
                            hash.put("item_price", item.item_price!!)
                            hash.put("item_image", item.item_image!!)
                        } else {
                            val query = item.item_sale_info.filter { it -> it.brand_name == cat }
                            if (query.isEmpty()) {
                                hash.put("item_name", item.item_name!!)
                                hash.put("item_catory", item.item_catory!!)
                                hash.put("item_price", item.item_price!!)
                                hash.put("item_image", item.item_image!!)
                            } else {
                                hash.put("item_name", item.item_name!!)
                                hash.put("item_catory", item.item_catory!!)
                                hash.put("item_price", item.item_price!!)
                                hash.put("item_image", item.item_image!!)
                                hash.put("item_sale_info", query)

                            }

                        }
                        result.add(hash)
                    }
                    for (i in keys) {
                        val ref = FirebaseFirestore.getInstance().collection("Brand")
                                .document(cat)
                                .collection(cat)
                                .document(i)
                                .set(result[keys.indexOf(i)])
                    }


                }

         */

        data class GEO(var store_name: String, var lat: Double, var lng: Double) {
            lateinit var hash: String

            fun build(): HashMap<String, Any> {
                hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat, lng))
                return hashMapOf(
                        "store_name" to store_name,
                        "store_geo" to GeoLocation(lat, lng),
                        "geo_hash" to hash,
                        "brand_name" to "CU"
                )
            }
        }
    }


// Listener Implement

    /*
        Listener for bottomBar

     */
    val bottomBarListener
    = BottomNavigationView.OnNavigationItemSelectedListener { selected ->
        when (selected.itemId) {
            R.id.nav_home -> {
                selectedFragment = HomeFragment()
            }

            R.id.nav_find_by_product -> {
                selectedFragment = ProductSearchFragment()
            }

            R.id.nav_find_by_brand -> {
                selectedFragment = FindBrandFragment()
            }

            R.id.nav_find_by_map -> {
                val _intent = Intent(this, MapActivity::class.java)
                _intent.putExtra("Location", Pair(current_lat, current_lng))
                startActivity(_intent)
            }

            R.id.nav_mypage -> {
                selectedFragment = ProfileFragment()
            }

            else -> {
                selectedFragment = HomeFragment()
            }
        }

    supportFragmentManager.beginTransaction()
        .replace(R.id.main_fragment_container, selectedFragment!!)
        .commit()

    return@OnNavigationItemSelectedListener true
    }

// Private Methods

    private fun init_ui() {
        main_bottom_bar = findViewById(R.id.main_bottom_bar)
        main_bottom_bar.setOnNavigationItemSelectedListener(bottomBarListener)
        function_name = findViewById(R.id.function_name)

        location_text = findViewById(R.id.location_text)
        location_text.setOnClickListener {
            update_location()
        }

    }

    private fun init_setting() {
        locationClient = getFusedLocationProviderClient(this)
        edit_location = findViewById(R.id.edit_location)
        update_location()
    }

    private fun update_location() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            this.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
                Log.d("permission", "fail")
            return
        }
        locationClient.lastLocation
            .addOnSuccessListener {
                if(it == null) {
                    var request = LocationRequest()
                    request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    request.interval = 100
                    request.interval = 20 * 1000

                    val callback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            if (locationResult == null) return
                            for (location in locationResult.locations) {
                                current_lat = location.latitude
                                current_lng = location.longitude
                                location_to_address(location)
                            }
                            locationClient.removeLocationUpdates(this)
                            Log.d("callback", "그만!!!")
                        }

                        override fun onLocationAvailability(p0: LocationAvailability?) {
                            super.onLocationAvailability(p0)
                        }
                    }

                    locationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
                    return@addOnSuccessListener
                }

                current_lat = it.latitude
                current_lng = it.longitude
                Log.d("onLocationChanged", "위도: ${current_lat}, 경도 : ${current_lng}")
                location_to_address(it)
            }


    }

    private fun location_to_address(location :Location) {
        val geocoder = Geocoder(this, Locale.KOREA)

        try {
            val address = geocoder.getFromLocation(current_lat, current_lng, 1)

            if(address == null) {
                Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
                return
            }
            if (address.isEmpty()) return
            location_text.text = address[0].getAddressLine(0).substringAfter(' ')

        } catch (e: IOError) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
        }
    }
}