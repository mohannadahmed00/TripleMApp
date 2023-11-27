package com.giraffe.triplemapplication.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.discount.CouponsResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeApiCall
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeVM(private val repo:RepoInterface):ViewModel() {



    private val _allBrandsFlow: MutableStateFlow<Resource<AllBrandsResponse>> = MutableStateFlow(Resource.Loading)
    val allBrandsFlow: StateFlow<Resource<AllBrandsResponse>> = _allBrandsFlow.asStateFlow()

    private val _allCategoriesFlow: MutableStateFlow<Resource<AllCategoriesResponse>> = MutableStateFlow(Resource.Loading)
    val allCategoriesFlow: StateFlow<Resource<AllCategoriesResponse>> = _allCategoriesFlow.asStateFlow()

    private val _allProductsFlow: MutableStateFlow<Resource<AllProductsResponse>> = MutableStateFlow(Resource.Loading)
    val allProductsFlow: StateFlow<Resource<AllProductsResponse>> = _allProductsFlow.asStateFlow()

    private val _couponsFlow: MutableStateFlow<Resource<CouponsResponse>> = MutableStateFlow(Resource.Loading)
    val couponsFlow: StateFlow<Resource<CouponsResponse>> = _couponsFlow.asStateFlow()


    init {
        getAllBrands()
        getAllCategories()
        getAllProducts()
        getCoupons()
        getCustomerId()
    }




    private fun getAllBrands() {
        viewModelScope.launch {
            _allBrandsFlow.emit(safeCall { repo.getAllBrands() })
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            _allCategoriesFlow.emit(safeCall { repo.getAllCategories() })
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _allProductsFlow.emit(safeCall { repo.getAllProducts() })
        }
    }

    private fun getCoupons() {
        viewModelScope.launch(Dispatchers.IO) {
            _couponsFlow.emit(safeApiCall {repo.getCoupons()})
        }
    }
    private fun getCustomerId(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "getCustomerId: ${repo.getCustomerIdLocally()}")
            Log.i("TAG", "getCustomerId: ${repo.getCartId()}")

        }
    }
}