package com.giraffe.triplemapplication.features.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentMapBinding
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.giraffe.triplemapplication.model.address.Address
import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class MapFragment : BaseFragment<ProfileVM, FragmentMapBinding>(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnCameraIdleListener {


    companion object {
        const val TAG = "MapFragment"
    }



    private var mGoogleApiClient: GoogleApiClient? = null
    private var gMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location: Location? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0


    private var postalCode:String?= null
    private var street:String?= null
    private var city:String?= null
    private var area:String?= null
    private var province:String?= null
    private var country:String?= null
    private var countryCode:String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLocation()
    }



    override fun getViewModel(): Class<ProfileVM> = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)

    override fun handleView() {

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun handleClicks() {
        binding.btnConfirm.setOnClickListener {
            Log.i(TAG, "handleClicks: ")
            val tag = binding.edtTag.text
            if (!tag.isNullOrBlank()){
                Log.i(TAG, "handleClicks: isNotNullOrBlank")
                val addressRequest = AddressRequest(
                    address = Address(
                        address1 = binding.tvAddress.text.toString(),
                        address2 = street,
                        city = city,
                        //first_name = tag.trim().toString(),
                        //last_name = " ",
                        province = area?.replace(" Governorate",""),
                        zip = postalCode,
                        name = tag.trim().toString(),
                        country_code = countryCode,
                    )
                )
                mViewModel.addNewAddress("6666401546315",addressRequest)
                observeAddNewAddress()
            }
        }
    }

    private fun observeAddNewAddress() {
        lifecycleScope.launch {
            mViewModel.addressFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        Log.e(TAG, "observeAddNewAddress: ${it.errorCode}: ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        Log.i(TAG, "observeAddNewAddress: ")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "observeAddNewAddress: ${it.value}")
                        findNavController().navigateUp()
                    }
                }
            }
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
        try {
            val geoCoder = Geocoder(context)
            val address = geoCoder.getFromLocation(latitude, longitude, 1)
            if (!address.isNullOrEmpty()) {
                postalCode = address[0].postalCode
                street = address[0].thoroughfare
                city = address[0].subAdminArea
                province = address[0].subAdminArea
                area = address[0].adminArea
                country = address[0].countryName
                countryCode = address[0].countryCode
                var mAddress = "${address[0].postalCode}, ${address[0].thoroughfare}, ${address[0].subAdminArea}, ${address[0].adminArea}, ${address[0].countryName}"
                mAddress = mAddress.replace("null, ","")
                binding.tvAddress.text= mAddress
                Log.i(TAG, mAddress)
            }
        }catch (e:Exception){
            Log.e(TAG, "getAddress: ${e.message}")
        }

    }


    private fun getLocation() {
        Log.i(TAG, "getLocation: ")
        requestNewLocationData()
        /*if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermissions()
        }*/
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            location = locationResult.lastLocation
            lat = location?.latitude ?: 0.0
            lon = location?.longitude ?: 0.0
            Log.i(TAG, "onLocationResult: $lat , $lon ")
            fusedLocationProviderClient.removeLocationUpdates(this)
            getAddress(requireContext(),lat,lon)
            moveCamera(LatLng(lat, lon))
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        Log.i(MapFragment.TAG, "requestNewLocationData: ")
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }


}