package com.giraffe.triplemapplication.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.address.AddressResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeApiCall
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileVM(private val repo: RepoInterface) : ViewModel() {
    private val _currencyFlow: MutableStateFlow<Resource<String>> = MutableStateFlow(
        Resource.Loading)
    val currencyFlow: StateFlow<Resource<String>> = _currencyFlow.asStateFlow()

    private val _addressFlow: MutableStateFlow<Resource<AddressResponse>> = MutableStateFlow(
        Resource.Loading)
    val addressFlow: StateFlow<Resource<AddressResponse>> = _addressFlow.asStateFlow()
    fun setLanguage(code: Constants.Languages) {
        viewModelScope.launch {
            repo.setLanguage(code)
        }
    }

    fun getCurrency(){
        viewModelScope.launch(Dispatchers.IO) {
            _currencyFlow.emit(safeCall { repo.getCurrency() })
        }
    }
    fun setCurrency(currencies: Constants.Currencies){
        viewModelScope.launch(Dispatchers.IO) {
            repo.setCurrency(currencies)
        }
    }

    fun addNewAddress(
        customerId:String,
        address: AddressRequest
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _addressFlow.emit(safeApiCall{ repo.addNewAddress(customerId, address) })
        }
    }
}