package com.giraffe.triplemapplication.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.network.ApiClient.getAllBrands
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
class HomeVM(private val repo:RepoInterface):ViewModel() {

    private val _allProductsFlow: MutableStateFlow<Resource<AllProductsResponse>> = MutableStateFlow(Resource.Loading)
    val allProductsFlow: StateFlow<Resource<AllProductsResponse>> = _allProductsFlow.asStateFlow()

    private val _allBrandsFlow: MutableStateFlow<Resource<AllBrandsResponse>> = MutableStateFlow(Resource.Loading)
    val allBrandsFlow: StateFlow<Resource<AllBrandsResponse>> = _allBrandsFlow.asStateFlow()

    init {
        getAllProducts()
        getAllBrands()
    }

    private fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _allProductsFlow.emit(safeCall { repo.getAllProducts() })
        }
    }

    private fun getAllBrands() {
        viewModelScope.launch {
            _allBrandsFlow.emit(safeCall { repo.getAllBrands() })
        }
    }
}