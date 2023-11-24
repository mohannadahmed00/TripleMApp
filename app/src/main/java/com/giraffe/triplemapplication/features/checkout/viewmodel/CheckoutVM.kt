package com.giraffe.triplemapplication.features.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.repo.RepoInterface
import kotlinx.coroutines.launch

class CheckoutVM(private val repo: RepoInterface): ViewModel() {

    fun checkout() {
        viewModelScope.launch {
//            repo.createOrder(orderCreate)
        }
    }
}