package com.giraffe.triplemapplication.features.profile.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentAddressesBinding
import com.giraffe.triplemapplication.features.profile.view.adapters.AddressesAdapter
import com.giraffe.triplemapplication.features.profile.viewmodel.ProfileVM
import com.giraffe.triplemapplication.model.address.Address
import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddressesFragment : BaseFragment<ProfileVM, FragmentAddressesBinding>(),
    AddressesAdapter.OnAddressClick {

    companion object {
        private const val TAG = "AddressesFragment"
        const val REQUEST_CODE = 7007
    }

    private var address: Address? = null
    private var position: Int = -1


    private lateinit var adapter: AddressesAdapter
    private var deletedItemPosition: Int = -1
    override fun getViewModel(): Class<ProfileVM> = ProfileVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentAddressesBinding = FragmentAddressesBinding.inflate(inflater, container, false)

    override fun onResume() {
        super.onResume()
        mViewModel.getAddresses()
    }


    override fun handleView() {
        adapter = AddressesAdapter(mutableListOf(), this)
        binding.rvAddresses.adapter = adapter
        mViewModel.getAddresses()
        observeGetAddresses()
    }

    private fun observeGetAddresses() {
        lifecycleScope.launch {
            mViewModel.addressesFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Log.e(TAG, "observeGetAddresses: ${it.errorCode}: ${it.errorBody}")
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeGetAddresses: loading")
                    }

                    is Resource.Success -> {
                        adapter.updateList(it.value.addresses)
                    }
                }
            }
        }
    }

    override fun handleClicks() {
        binding.ivAddLocation.setOnClickListener {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    val action = AddressesFragmentDirections.actionAddressesFragmentToMapFragment()
                    findNavController().navigate(action)
                } else {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            } else {
                requestPermissions()
            }
        }
    }

    //===========================

    /*private fun getLocation() {
        Log.i(MapFragment.TAG, "getLocation: ")
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermissions()
        }
    }*/

    /*private val locationCallback: LocationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult) {
            location = locationResult.lastLocation
            lat = location?.latitude ?: 0.0
            lon = location?.longitude ?: 0.0
            Log.i(MapFragment.TAG, "onLocationResult: $lat , $lon ")
            fusedLocationProviderClient.removeLocationUpdates(this)
            val action = AddressesFragmentDirections.actionAddressesFragmentToMapFragment(lat.toFloat(),lon.toFloat())
            findNavController().navigate(action)
        }
    }*/

    /*@SuppressLint("MissingPermission")
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
    }*/

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val action = AddressesFragmentDirections.actionAddressesFragmentToMapFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onAddressClick(address: Address, position: Int) {
        this.address = address
        this.position = position

        deletedItemPosition = position
        mViewModel.deleteAddress(address.customer_id!!, address.id!!)
        observeDeleteAddress()
        Snackbar.make(
            requireView(),
            getString(R.string.do_you_want_to_perform_this_action),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.undo)) {
            // Perform the negative action (Undo)
            address.let {
                val addressRequest = AddressRequest(
                    address = it
                )
                mViewModel.addNewAddress(addressRequest)
                observeAddNewAddress()
            }
        }.show()
    }

    override fun onAddressLongPress(addressId: Long, position: Int,oldDefaultPosition:Int) {
        mViewModel.setDefaultAddress( addressId)
        observeSetDefaultAddress(position,oldDefaultPosition)
    }

    private fun observeSetDefaultAddress(position: Int,oldDefaultPosition:Int) {
        lifecycleScope.launch {
            mViewModel.defAddressFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        Log.e(TAG, "observeSetDefaultAddress: (Failure ${it.errorCode}) ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        Log.i(TAG, "observeSetDefaultAddress: (Loading)")
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "observeSetDefaultAddress: (Success)")
                        adapter.selectNewDefault(position,oldDefaultPosition)
                    }
                }
            }
        }
    }

    private fun observeAddNewAddress() {
        lifecycleScope.launch {
            mViewModel.addressFlow.collect{
                when(it){
                    is Resource.Failure -> {
                        Log.e(MapFragment.TAG, "observeAddNewAddress: ${it.errorCode}: ${it.errorBody}")
                    }
                    Resource.Loading -> {
                        Log.i(MapFragment.TAG, "observeAddNewAddress: ")
                    }
                    is Resource.Success -> {
                        Log.d(MapFragment.TAG, "observeAddNewAddress: ${it.value}")
                        val newList = mutableListOf<Address>()
                        newList.addAll(adapter.addresses)
                        newList.add(address!!)
                        adapter.updateList(newList)
                        //findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun observeDeleteAddress() {
        lifecycleScope.launch {
            mViewModel.delAddressFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        if (it.errorCode == 200) {
                            adapter.deleteItem(deletedItemPosition)
                        }
                        Log.e(TAG, "observeDeleteAddress: ${it.errorCode}: ${it.errorBody}")
                    }

                    Resource.Loading -> {
                        Log.i(TAG, "observeDeleteAddress: loading")
                    }

                    is Resource.Success -> {

                    }
                }
            }
        }
    }


}