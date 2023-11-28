package com.giraffe.triplemapplication.features.search.viewmodel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchVM(private val repo: RepoInterface): ViewModel() {


    private val TAG: String = "SEARCH_VM"

    fun setCategory(category:String){
        Log.i(TAG, "setCategory: $category")
        FilterOptions.currentCategory = category.lowercase()
    }
    fun setOnSale(onSale:Boolean){
        FilterOptions.onSale = onSale
    }
    fun setBrands(brands : List<String>){
        FilterOptions.selectedBrands = brands
    }
    fun setColor(selectedColor: String){
        FilterOptions.selectedColor = selectedColor
    }
}