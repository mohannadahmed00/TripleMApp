package com.giraffe.triplemapplication.features.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.MultipleCustomerResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginVM(private val repo: RepoInterface) : ViewModel() {

    private val _firebaseUser: MutableStateFlow<Resource<AuthResult>> =
        MutableStateFlow(Resource.Loading)
    val currentUser: StateFlow<Resource<AuthResult>> = _firebaseUser.asStateFlow()

    //    private val _wishListId : MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
//    private val _cartId : MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
//    private val _customer : MutableStateFlow<Resource<CustomerResponse>> = MutableStateFlow(Resource.Loading)
    private var wishListId: Long? = null
    private var cartId: Long? = null
    private val _customerId: MutableStateFlow<Long?> = MutableStateFlow(null)
    val customerId: StateFlow<Long?> = _customerId.asStateFlow()
    private val _customer: MutableStateFlow<Resource<MultipleCustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val customer : StateFlow<Resource<MultipleCustomerResponse>> = _customer


    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _firebaseUser.emit(safeCall { repo.signInFirebase(email, password) })
        }
    }



    fun getCustomerByEmail(email : String){
        viewModelScope.launch(Dispatchers.IO){
            _customer.emit(safeCall { repo.getCustomerByEmail(email) })
        }
    }

    fun setData(customerId : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setCustomerIdLocally(customerId)
            repo.setCartIdLocally(cartId)
            repo.setWishListIdLocally(wishListId)
            Log.d("TAG", "setData: customer $customerId ")
            Log.d("TAG", "setData: cart $cartId ")
            Log.d("TAG", "setData: wish $wishListId ")

        }
    }


}