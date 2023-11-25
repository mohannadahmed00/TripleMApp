package com.giraffe.triplemapplication.features.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartVM(private val repo: RepoInterface): ViewModel() {
    private val _cartIdFlow: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(
        Resource.Loading)
    val cartIdFlow: StateFlow<Resource<Boolean>> = _cartIdFlow.asStateFlow()

    private val _cartFlow: MutableStateFlow<Resource<List<CartItem>>> = MutableStateFlow(Resource.Loading)
    val cartFlow: StateFlow<Resource<List<CartItem>>> = _cartFlow.asStateFlow()
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

    fun getCartItems(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCartItems().collect{
                if (it.isEmpty()){
                    _cartFlow.emit(Resource.Failure(false,0,it))
                }else {
                    _cartFlow.emit(Resource.Success(it))
                }
            }
        }
    }

}