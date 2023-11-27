package com.giraffe.triplemapplication.features.search.viewmodel

import android.graphics.Color
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


    fun setCategory(category:String){
        FilterOptions.currentCategory = category
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