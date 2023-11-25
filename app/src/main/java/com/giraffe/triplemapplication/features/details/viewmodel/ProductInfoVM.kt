package com.giraffe.triplemapplication.features.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.CartItem
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

class ProductInfoVM(private val repo: RepoInterface) : ViewModel() {
    private val _cartFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val cartFlow: StateFlow<Resource<Long>> = _cartFlow.asStateFlow()
    private val _updateCartFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val updateCartFlow: StateFlow<Resource<DraftResponse>> = _updateCartFlow.asStateFlow()

    private val _variantsFlow: MutableStateFlow<List<LineItem>> = MutableStateFlow(listOf())
    val variantsFlow: StateFlow<List<LineItem>> = _variantsFlow.asStateFlow()

    private val _cartIdFlow: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(
        Resource.Loading)
    val cartIdFlow: StateFlow<Resource<Boolean>> = _cartIdFlow.asStateFlow()

    fun insertCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            _cartFlow.emit(safeCall { repo.insertCartItem(cartItem) })
        }
    }

    fun updateCartDraft() {
        viewModelScope.launch {
            repo.getCartItems().collect{
                val lineItems = it.map {cartItem ->
                    LineItem(cartItem.quantity,cartItem.variantId)
                }
                if (lineItems.size<=1){
                    _updateCartFlow.emit(safeApiCall { repo.createCartDraft(lineItems) })
                }else{
                    _updateCartFlow.emit(safeApiCall { repo.modifyCartDraft(lineItems) })
                }
                //_variantsFlow.emit(lineItems)
            }
        }
        viewModelScope.launch {
            //_updateCartFlow.emit(safeApiCall { repo.modifyCartDraft() })
        }
    }

    fun uploadCartId(cartId:Long){
        viewModelScope.launch {
            repo.uploadCartId(cartId).let {
                if (it!=null && it.isSuccessful ){
                    _cartIdFlow.emit(Resource.Success(true))
                }else{
                    _cartIdFlow.emit(Resource.Failure(false,0,it?.exception?.message))
                }
            }
        }
    }
}