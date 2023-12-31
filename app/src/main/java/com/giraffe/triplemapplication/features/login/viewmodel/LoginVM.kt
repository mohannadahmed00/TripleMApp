package com.giraffe.triplemapplication.features.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.customers.MultipleCustomerResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeApiCall
import com.giraffe.triplemapplication.utils.safeCall
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginVM(private val repo: RepoInterface) : ViewModel() {

    private var cartId: Long? = null
    private var wishListId: Long? = null

    private val _firebaseUser: MutableStateFlow<Resource<AuthResult>> =
        MutableStateFlow(Resource.Loading)
    val currentUser: StateFlow<Resource<AuthResult>> = _firebaseUser.asStateFlow()
    private val _wishListId : MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)

    private val _cartId : MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val cartIdFlow : StateFlow<Resource<Long>> = _cartId.asStateFlow()


    val wishListIdFlow : StateFlow<Resource<Long>> = _wishListId.asStateFlow()

    private val _singleCartFlow : MutableStateFlow<Resource<DraftResponse>> = MutableStateFlow(Resource.Loading)
    val singleCartFlow : StateFlow<Resource<DraftResponse>> = _singleCartFlow.asStateFlow()

    private val _singleWishFlow : MutableStateFlow<Resource<DraftResponse>> = MutableStateFlow(Resource.Loading)
    val singleWishFlow : StateFlow<Resource<DraftResponse>> = _singleWishFlow.asStateFlow()

    private val _productsFlow : MutableStateFlow<Resource<AllProductsResponse>> = MutableStateFlow(Resource.Loading)
    val productsFlow : StateFlow<Resource<AllProductsResponse>> = _productsFlow.asStateFlow()

    private val _productsWishListFlow : MutableStateFlow<Resource<AllProductsResponse>> = MutableStateFlow(Resource.Loading)
    val productsWishListFlow : StateFlow<Resource<AllProductsResponse>> = _productsWishListFlow.asStateFlow()



    private val _customer: MutableStateFlow<Resource<MultipleCustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val customer : StateFlow<Resource<MultipleCustomerResponse>> = _customer


    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _firebaseUser.emit(safeCall { repo.signInFirebase(email, password) })
//            repo.uploadCustomerId()

        }
    }


    private fun getWishListIdFromFirebase(){
        viewModelScope.launch(Dispatchers.IO) {
            _wishListId.emit(safeCall{repo.getWishListId()} )
        }
    }
    private fun getCartIdFromFirebase(){
        viewModelScope.launch(Dispatchers.IO) {
            _cartId.emit(safeCall{repo.getCartId()} )
        }
    }

    private fun collectCartId(){
        viewModelScope.launch(Dispatchers.IO) {
            _cartId.collectLatest {
                when(it){
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        cartId = it.value
                    }
                }
            }
        }
    }
    private fun collectWishListId(){
        viewModelScope.launch(Dispatchers.IO) {
            _wishListId.collectLatest {
                when(it){
                    is Resource.Failure -> {}
                    Resource.Loading -> {}
                    is Resource.Success -> {
                        wishListId = it.value
                    }
                }
            }
        }
    }



    fun getCustomerByEmail(email : String){
        viewModelScope.launch(Dispatchers.IO){
            getCartIdFromFirebase()
            getWishListIdFromFirebase()
            collectCartId()
            collectWishListId()
            _customer.emit(safeCall { repo.getCustomerByEmail(email) })
        }
    }

    fun setData(customerId : Long ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.setCustomerIdLocally(customerId)
            repo.setCartIdLocally(cartId)
            repo.setWishListIdLocally(wishListId)

            Log.d("TAG", "setData: customer $customerId ")
            Log.d("TAG", "setData: cart $cartId ")
            Log.d("TAG", "setData: wish $wishListId ")

        }
    }

    fun setFullNameLocally(fullName:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.setFullNameLocally(fullName)
        }
    }


    fun getSingleCart(){
        viewModelScope.launch(Dispatchers.IO) {
            _singleCartFlow.emit(safeApiCall { repo.getSingleCart(cartId?:0) })
        }
    }


    fun getListOfProducts(ids: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _productsFlow.emit(safeApiCall { repo.getListOfProducts(ids) })
        }
    }

    private val _cartFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val cartFlow: StateFlow<Resource<Long>> = _cartFlow.asStateFlow()
    fun insertCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            _cartFlow.emit(safeCall { repo.insertCartItem(cartItem) })
        }
    }

    fun getSingleWishList(){
        viewModelScope.launch(Dispatchers.IO) {
            _singleWishFlow.emit(safeApiCall { repo.getSingleWish(wishListId?:0) })
        }
    }


    fun getWishListOfProducts(ids: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _productsWishListFlow.emit(safeApiCall { repo.getListOfProducts(ids) })
        }
    }
    private val _wishListFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val wishListFlow: StateFlow<Resource<Long>> = _wishListFlow.asStateFlow()
    fun insertWishListItem(wishListItem: WishListItem) {
        viewModelScope.launch {
            _wishListFlow.emit(safeCall { repo.insertWishListItem(wishListItem) })
        }
    }

}