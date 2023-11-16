package com.giraffe.triplemapplication.utils

import android.content.Context
import android.location.Geocoder


fun getAddress(context: Context, latitude: Double, longitude: Double): String {
    val geoCoder = Geocoder(context)
    val address = geoCoder.getFromLocation(latitude, longitude, 1)
    return if (!address.isNullOrEmpty() && address[0].adminArea != null && address[0].countryName != null) {
        "${address[0].adminArea}, ${address[0].countryName}"
    } else {
        Constants.UNKNOWN_AREA
    }
}



