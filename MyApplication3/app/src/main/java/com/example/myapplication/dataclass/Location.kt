package com.example.myapplication.dataclass

import com.naver.maps.geometry.LatLng

data class Location(val name : String, val Lat : Double, val Lng : Double)
data class LocationWithID(val id : String? = "", val name : String? = "", val Lat: Double? = 0.0, val Lng: Double? = 0.0, val store_type: String? = null)