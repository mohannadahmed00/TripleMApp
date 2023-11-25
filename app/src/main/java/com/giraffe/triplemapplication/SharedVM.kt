package com.giraffe.triplemapplication
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SharedVM(val repo: RepoInterface) : ViewModel() {
    companion object{
        private const val TAG = "SharedVM"
    }
    private val _languageFlow: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Loading)
    val languageFlow: StateFlow<Resource<String>> = _languageFlow.asStateFlow()
    private val _currentProduct : MutableStateFlow<Product?> = MutableStateFlow(null)
    val currentProduct : StateFlow<Product?> = _currentProduct.asStateFlow()

    private val _cartIdFlow : MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val cartIdFlow : StateFlow<Resource<Long>> = _cartIdFlow.asStateFlow()

    private val _currencyFlow : MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading)
    val currencyFlow : StateFlow<Resource<String>> = _currencyFlow.asStateFlow()

    private val _exchangeRateFlow : MutableStateFlow<Pair<Double,Double>?> = MutableStateFlow(null)
    val exchangeRateFlow : StateFlow<Pair<Double,Double>?> = _exchangeRateFlow.asStateFlow()
    fun getLanguage() {
        viewModelScope.launch {
            _languageFlow.emit(safeCall { repo.getLanguage() })
        }
    }
    fun setCurrentProduct(product: Product){
        viewModelScope.launch {
            _currentProduct.emit(product)
        }
    }

    fun insertFavorite(product: Product){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertFavorite(product)
        }
    }

    fun getCartId(){
        viewModelScope.launch(Dispatchers.IO) {
            _cartIdFlow.emit(safeCall { repo.getCartId() })
        }
    }

    fun getSelectedCurrency(){
        viewModelScope.launch {
            _currencyFlow.emit(safeCall { repo.getCurrency() })
        }
    }

    fun getExchangeRateOf(currencyCode: Constants.Currencies){
        viewModelScope.launch {
            val pair = repo.getExchangeRateOf(currencyCode)
            Log.d(TAG, "getExchangeRateOf: (Success) ${pair.first()}")
            _exchangeRateFlow.emit(pair.first())
            /*when(val res = safeCall {  }){
                is Resource.Failure -> {
                    Log.e(TAG, "getExchangeRateOf: (Failure)")
                }
                Resource.Loading -> {
                    Log.i(TAG, "getExchangeRateOf: (Loading)")
                }
                is Resource.Success -> {

                }
            }*/
        }
    }
}