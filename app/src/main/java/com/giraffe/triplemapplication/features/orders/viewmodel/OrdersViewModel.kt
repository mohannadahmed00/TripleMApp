package com.giraffe.triplemapplication.features.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.giraffe.triplemapplication.model.orders.AllOrdersResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class OrdersViewModel(private val repo: RepoInterface) : ViewModel() {
    private val _ordersFlow: MutableStateFlow<Resource<AllOrdersResponse>> = MutableStateFlow(Resource.Loading)
    val ordersFlow: StateFlow<Resource<AllOrdersResponse>> = _ordersFlow.asStateFlow()

    var customerId: Long = 0L

    init {
        getOrders()
    }

    private fun getOrders() {
        viewModelScope.launch {
            customerId = repo.getCustomerIdLocally()!!
            _ordersFlow.emit(safeCall { repo.getOrders() })
        }
    }

    fun delOrder(orderId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delOrder(orderId)
        }
    }
}