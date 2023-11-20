package com.giraffe.triplemapplication.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
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
}