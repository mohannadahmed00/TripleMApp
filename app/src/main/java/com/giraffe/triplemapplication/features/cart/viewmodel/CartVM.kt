package com.giraffe.triplemapplication.features.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeApiCall
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartVM(private val repo: RepoInterface) : ViewModel() {
    private val _cartFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val cartFlow: StateFlow<Resource<Long>> = _cartFlow.asStateFlow()
    private val _updateCartFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val updateCartFlow: StateFlow<Resource<DraftResponse>> = _updateCartFlow.asStateFlow()

    private val _creationCartFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val creationCartFlow: StateFlow<Resource<DraftResponse>> = _creationCartFlow.asStateFlow()

    private val _variantsFlow: MutableStateFlow<List<LineItem>> = MutableStateFlow(listOf())
    val variantsFlow: StateFlow<List<LineItem>> = _variantsFlow.asStateFlow()

    private val _cartIdFlow: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(
        Resource.Loading
    )
    val cartIdFlow: StateFlow<Resource<Boolean>> = _cartIdFlow.asStateFlow()

    private val _cartItemsFlow: MutableStateFlow<Resource<List<CartItem>>> = MutableStateFlow(
        Resource.Loading
    )
    val cartItemsFlow: StateFlow<Resource<List<CartItem>>> = _cartItemsFlow.asStateFlow()

    private val _delCartItemFlow: MutableStateFlow<Resource<Int>> = MutableStateFlow(
        Resource.Loading
    )
    val delCartItemFlow: StateFlow<Resource<Int>> = _delCartItemFlow.asStateFlow()

    fun insertCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            _cartFlow.emit(safeCall { repo.insertCartItem(cartItem) })
        }
    }

    fun getLocallyCartItems() {

        viewModelScope.launch {
            _cartItemsFlow.emit(Resource.Loading)
            _cartItemsFlow.emit(safeCall { repo.getCartItems() })
            //_cartItemsFlow.emit(Resource.Loading)
            _cartItemsFlow.emit(safeCall { repo.getCartItems() })
        }
    }

    fun updateCartDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _updateCartFlow.emit(safeApiCall { repo.modifyCartDraft(DraftRequest(DraftOrder(line_items = lineItems))) })
        }
    }

    fun createCartDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _creationCartFlow.emit(safeApiCall { repo.createCartDraft(DraftRequest(DraftOrder(line_items = lineItems))) })
        }
    }

    fun uploadCartId(cartId: Long) {
        viewModelScope.launch {
            repo.uploadCartId(cartId).let {
                if (it != null && it.isSuccessful) {
                    _cartIdFlow.emit(Resource.Success(true))
                } else {
                    _cartIdFlow.emit(Resource.Failure(false, 0, it?.exception?.message))
                }
            }
        }
    }

    fun insertCartIdLocally(cartId: Long) {
        viewModelScope.launch {
            repo.setCartIdLocally(cartId)
        }
    }

    fun deleteCartItemLocally(cartItem: CartItem) {
        viewModelScope.launch {
            _delCartItemFlow.emit(safeCall { repo.deleteCartItem(cartItem) })
        }
    }

}