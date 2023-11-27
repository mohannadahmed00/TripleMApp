package com.giraffe.triplemapplication.features.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.response.DraftOrder
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.createorderresponse.CreateOrderResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CheckoutVM(private val repo: RepoInterface): ViewModel() {

    private val _ordersFlow: MutableStateFlow<Resource<CreateOrderResponse>> = MutableStateFlow(
        Resource.Loading)
    val ordersFlow: StateFlow<Resource<CreateOrderResponse>> = _ordersFlow.asStateFlow()

    private val _draftOrderFlow: MutableStateFlow<Resource<DraftOrder>> = MutableStateFlow(
        Resource.Loading)
    val draftOrderFlow: StateFlow<Resource<DraftOrder>> = _draftOrderFlow.asStateFlow()

    fun checkout(orderCreate: OrderCreate) {
        viewModelScope.launch {
            _ordersFlow.emit(safeCall { repo.createOrder(orderCreate) })
        }
    }

    fun completeOrder() {
        viewModelScope.launch {
            Log.i("hahahahaha", "completeOrder: cart id - ${repo.getCartId().first()}")
            _draftOrderFlow.emit(safeCall { repo.completeOrder(repo.getCartId().first()) })
        }
    }

    fun removeCart() {
        viewModelScope.launch {
            repo.removeCartDraft(repo.getCartId().first())
            repo.setCartIdLocally(null)
            repo.uploadCartId(-1)
            repo.deleteAllCartItems()
        }
    }
}