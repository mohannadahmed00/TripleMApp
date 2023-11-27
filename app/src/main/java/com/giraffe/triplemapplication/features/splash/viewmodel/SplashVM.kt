package com.giraffe.triplemapplication.features.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashVM(private val repo: RepoInterface) : ViewModel() {
    private val _firstFlagFlow: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Loading)
    val firstFlagFlow: StateFlow<Resource<Boolean>> = _firstFlagFlow.asStateFlow()

    private val _currenciesFlow: MutableStateFlow<Resource<ExchangeRatesResponse>> =
        MutableStateFlow(Resource.Loading)
    val currenciesFlow: StateFlow<Resource<ExchangeRatesResponse>> = _currenciesFlow.asStateFlow()

    private val _exchangeRatesFlow: MutableStateFlow<Resource<Long>> =
        MutableStateFlow(Resource.Loading)
    val exchangeRatesFlow: StateFlow<Resource<Long>> = _exchangeRatesFlow.asStateFlow()


    fun getFirstTimeFlag() {
        viewModelScope.launch(Dispatchers.IO) {
            _firstFlagFlow.emit(safeCall { repo.getFirstTimeFlag() })

        }
    }

    fun setFirstTimeFlag(flag: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setFirstTimeFlag(flag)
        }
    }

    fun getCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            _currenciesFlow.emit(safeCall { repo.getCurrencies() })
        }
    }

    fun setExchangeRates(value: ExchangeRatesResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            _exchangeRatesFlow.emit(safeCall { repo.setExchangeRates(value) })
        }
    }
    fun isLoggedIn():Boolean = repo.isLoggedIn()



}