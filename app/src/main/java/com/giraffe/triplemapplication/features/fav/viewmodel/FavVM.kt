package com.giraffe.triplemapplication.features.fav.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeApiCall
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavVM(private val repo: RepoInterface) : ViewModel() {
    private val _wishListFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val wishListFlow: StateFlow<Resource<Long>> = _wishListFlow.asStateFlow()
    private val _updateWishListFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val updateWishListFlow: StateFlow<Resource<DraftResponse>> = _updateWishListFlow.asStateFlow()

    private val _creationWishListFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val creationWishListFlow: StateFlow<Resource<DraftResponse>> = _creationWishListFlow.asStateFlow()

    private val _variantsFlow: MutableStateFlow<List<LineItem>> = MutableStateFlow(listOf())
    val variantsFlow: StateFlow<List<LineItem>> = _variantsFlow.asStateFlow()

    private val _wishIdFlow: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(
        Resource.Loading
    )
    val wishIdFlow: StateFlow<Resource<Boolean>> = _wishIdFlow.asStateFlow()

    private val _wishListItemsFlow: MutableStateFlow<List<WishListItem>> = MutableStateFlow(
        listOf()
    )
    val wishListItemsFlow: StateFlow<List<WishListItem>> = _wishListItemsFlow.asStateFlow()

    private val _delWishListItemFlow: MutableStateFlow<Resource<Int>> = MutableStateFlow(
        Resource.Loading
    )
    val delWishListItemFlow: StateFlow<Resource<Int>> = _delWishListItemFlow.asStateFlow()
    private val _lastDeleted : MutableStateFlow<WishListItem?> = MutableStateFlow(null)

    init {
        getLocallyWishListItems()
    }
    fun insertWishListItem(wishListItem: WishListItem) {
        viewModelScope.launch {
            _wishListFlow.emit(safeCall { repo.insertWishListItem(wishListItem) })
        }
    }

    private fun getLocallyWishListItems() {
        viewModelScope.launch {
             repo.getWishListItems().collect{
                 _wishListItemsFlow.emit(it)
            }
        }
    }

    fun updateWishListDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _updateWishListFlow.emit(safeApiCall { repo.modifyWishListDraft(DraftRequest(DraftOrder(line_items = lineItems))) })
        }
    }

    fun createWishListDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _creationWishListFlow.emit(safeApiCall { repo.createWishListDraft(DraftRequest(DraftOrder(line_items = lineItems))) })
        }
    }

    fun uploadWishListId(wishListId: Long) {
        viewModelScope.launch {
            repo.uploadCartId(wishListId).let {

                    _wishIdFlow.emit(Resource.Success(true))


            }
        }
    }

    fun insertWishListItemIdLocally(wishListId: Long) {
        viewModelScope.launch {
            repo.setWishListIdLocally(wishListId)
        }
    }

    fun deleteWishListItemLocally(wishListItem: WishListItem) {
        viewModelScope.launch {
            _lastDeleted.emit(wishListItem)
            _delWishListItemFlow.emit(safeCall { repo.deleteWishListItem(wishListItem) })
        }
    }

    fun returnLastDeleted(){
        viewModelScope.launch(Dispatchers.IO) {
            _wishListFlow.emit(safeCall { repo.insertWishListItem(_lastDeleted.value!!) })
        }
    }
}