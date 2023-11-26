package com.giraffe.triplemapplication.features.orderdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.orders.Order
import com.giraffe.triplemapplication.model.orders.OrderResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailsVM(private val repo: RepoInterface): ViewModel() {

    private val _orderFlow: MutableStateFlow<Resource<OrderResponse>> = MutableStateFlow(Resource.Loading)
    val orderFlow: StateFlow<Resource<OrderResponse>> = _orderFlow.asStateFlow()

    private val _productsFlow: MutableStateFlow<Resource<AllProductsResponse>> = MutableStateFlow(Resource.Loading)
    val productsFlow: StateFlow<Resource<AllProductsResponse>> = _productsFlow.asStateFlow()

    fun getOrder(orderId: Long) {
        viewModelScope.launch {
            _orderFlow.emit(safeCall { repo.getOrder(orderId) })
        }
    }

    fun getProductsInOrder(productsIds: String) {
        viewModelScope.launch {
            _productsFlow.emit(safeCall { repo.getAllProductsFromIds(productsIds) })
        }
    }
}