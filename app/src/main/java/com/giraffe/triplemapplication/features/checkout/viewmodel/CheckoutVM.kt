package com.giraffe.triplemapplication.features.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.orders.AllOrdersResponse
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutVM(private val repo: RepoInterface): ViewModel() {

    private val _ordersFlow: MutableStateFlow<Resource<AllOrdersResponse>> = MutableStateFlow(
        Resource.Loading)
    val ordersFlow: StateFlow<Resource<AllOrdersResponse>> = _ordersFlow.asStateFlow()

    fun checkout(orderCreate: OrderCreate) {
        viewModelScope.launch {
            _ordersFlow.emit(safeCall { repo.createOrder(orderCreate) })
        }
    }
}