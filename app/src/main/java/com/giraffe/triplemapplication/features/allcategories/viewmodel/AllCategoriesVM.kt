package com.giraffe.triplemapplication.features.allcategories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllCategoriesVM(private val repo: RepoInterface): ViewModel() {

    private val _allCategoriesFlow: MutableStateFlow<Resource<AllCategoriesResponse>> = MutableStateFlow(Resource.Loading)
    val allCategoriesFlow: StateFlow<Resource<AllCategoriesResponse>> = _allCategoriesFlow.asStateFlow()

    private val _allBrandsFlow: MutableStateFlow<Resource<AllBrandsResponse>> = MutableStateFlow(Resource.Loading)
    val allBrandsFlow: StateFlow<Resource<AllBrandsResponse>> = _allBrandsFlow.asStateFlow()

    private val _allProductsFlow: MutableStateFlow<Resource<AllProductsResponse>> = MutableStateFlow(Resource.Loading)
    val allProductsFlow = _allProductsFlow.asStateFlow()

    init {
        getAllCategories()
        getAllBrands()
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            _allCategoriesFlow.emit(safeCall { repo.getAllCategories() })
        }
    }

    private fun getAllBrands() {
        viewModelScope.launch {
            _allBrandsFlow.emit(safeCall { repo.getAllBrands() })
        }
    }

    fun getAllProducts(categoryId: String) {
        viewModelScope.launch {
            _allProductsFlow.emit(safeCall { repo.getProductsFromCategoryId(categoryId) })
        }
    }
}