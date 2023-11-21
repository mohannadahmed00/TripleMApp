package com.giraffe.triplemapplication.features.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentMapBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapFragment : BaseFragment<ProfileVM, FragmentMapBinding>(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnCameraIdleListener {


    companion object {
        const val TAG = "MapFragment"
    }



    private var mGoogleApiClient: GoogleApiClient? = null
    private var gMap: GoogleMap? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private val args: MapFragmentArgs by navArgs()


    override fun getViewModel(): Class<ProfileVM> = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)

    override fun handleView() {
        lat = args.lat.toDouble()
        lon = args.lon.toDouble()
        getAddress(requireContext(),lat,lon)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun handleClicks() {
        binding.btnConfirm.setOnClickListener {

        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(gMap: GoogleMap) {
        //Log.i(TAG, "onMapReady: ")
        this.gMap = gMap
        buildGoogleApiClient()
        gMap.isMyLocationEnabled = true
    }

    private fun buildGoogleApiClient() {
        //Log.i(TAG, "buildGoogleApiClient: ")
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient?.apply { connect() }
    }

    override fun onConnected(bundle: Bundle?) {
        moveCamera(LatLng(lat, lon))
        gMap?.setOnCameraIdleListener(this)
    }

    private fun moveCamera(latLng: LatLng) {
        gMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        gMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun onConnectionSuspended(p0: Int) {}

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onCameraIdle() {
        lat = gMap?.cameraPosition?.target?.latitude ?: 0.0
        lon = gMap?.cameraPosition?.target?.longitude ?: 0.0
        getAddress(requireContext(),lat,lon)
    }


    private fun getAddress(context: Context, latitude: Double, longitude: Double){
        val geoCoder = Geocoder(context)
        val address = geoCoder.getFromLocation(latitude, longitude, 1)
        if (!address.isNullOrEmpty()) {
            var mAddress = "${address[0].postalCode}, ${address[0].thoroughfare}, ${address[0].subAdminArea}, ${address[0].adminArea}, ${address[0].countryName}"
            mAddress = mAddress.replace("null, ","")

            Log.i(TAG, mAddress)
            /*Log.i(TAG, "adminArea: ${address[0].adminArea}")
            Log.i(TAG, "countryName: ${address[0].countryName}")
            Log.i(TAG, "countryCode: ${address[0].countryCode}")
            Log.i(TAG, "extras: ${address[0].extras}")
            Log.i(TAG, "featureName: ${address[0].featureName}")
            Log.i(TAG, "locale: ${address[0].locale}")
            Log.i(TAG, "postalCode: ${address[0].postalCode}")
            Log.i(TAG, "premises: ${address[0].premises}")
            Log.i(TAG, "subAdminArea: ${address[0].subAdminArea}")
            Log.i(TAG, "subLocality: ${address[0].subLocality}")
            Log.i(TAG, "subThoroughfare: ${address[0].subThoroughfare}")
            Log.i(TAG, "thoroughfare: ${address[0].thoroughfare}")
            Log.i(TAG, "url: ${address[0].url}")*/
            //"${address[0].adminArea}, ${address[0].countryName}"
        }
    }


}