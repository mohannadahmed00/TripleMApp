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
    private val _lastDeleted : MutableStateFlow<Product?> = MutableStateFlow(null)
    val lastDeleted :StateFlow<Product?> = _lastDeleted
    init {
        getAllFavorites()
    }
    private fun getAllFavorites(){
        viewModelScope.launch(Dispatchers.IO) {

            (repo.getWishListItems().collect{
                _allFavProducts.emit(it)
            })
        }
    }
    fun deleteFavourite(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            _lastDeleted.emit(product)
            repo.deleteWishListItem(product)
        }
    }
    fun deleteAllFavorites(){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAllWishListItem()
        }
    }

    fun returnLastDeleted(){
        viewModelScope.launch(Dispatchers.IO) {
            _lastDeleted.value?.let { repo.insertWishListItem(it) }
        }
    }
}