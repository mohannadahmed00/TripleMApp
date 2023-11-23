package com.giraffe.triplemapplication.features.fav.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavVM(private val repo: RepoInterface) : ViewModel() {
    private val _allFavProducts: MutableStateFlow<List<Product>?> =
        MutableStateFlow(null)
    val allFavProducts : StateFlow<List<Product>?> = _allFavProducts.asStateFlow()
    init {
        getAllFavorites()
    }
    private fun getAllFavorites(){
        viewModelScope.launch(Dispatchers.IO) {

            (repo.getAllFavorites().collect{
                _allFavProducts.emit(it)
            })
        }
    }
    fun deleteFavourite(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavorite(product)
        }
    }
    fun deleteAllFavorites(){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAllFavorites()
        }
    }
    fun updateFavourite(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateFavorite(product)
        }
    }
}