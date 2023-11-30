package com.giraffe.triplemapplication.features.details.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.model.wishlist.WishListItem
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
    private val _wishListFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val wishListFlow: StateFlow<Resource<Long>> = _wishListFlow.asStateFlow()
    private val _updateCartFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val updateCartFlow: StateFlow<Resource<DraftResponse>> = _updateCartFlow.asStateFlow()
    private val _updateWishListFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val updateWishListFlow: StateFlow<Resource<DraftResponse>> = _updateWishListFlow.asStateFlow()

    private val _creationCartFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val creationCartFlow: StateFlow<Resource<DraftResponse>> = _creationCartFlow.asStateFlow()

    private val _creationWishListFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val creationWishListFlow: StateFlow<Resource<DraftResponse>> =
        _creationWishListFlow.asStateFlow()

    private val _variantsFlow: MutableStateFlow<List<LineItem>> = MutableStateFlow(listOf())
    val variantsFlow: StateFlow<List<LineItem>> = _variantsFlow.asStateFlow()

    private val _cartIdFlow: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(
        Resource.Loading
    )
    val cartIdFlow: StateFlow<Resource<Boolean>> = _cartIdFlow.asStateFlow()

    private val _wishListIdFlow: MutableStateFlow<Resource<Boolean>> = MutableStateFlow(
        Resource.Loading
    )
    val wishListIdFlow: StateFlow<Resource<Boolean>> = _wishListIdFlow.asStateFlow()

    private val _cartItemsFlow: MutableStateFlow<Resource<List<CartItem>>> =
        MutableStateFlow(Resource.Loading)
    val cartItemsFlow: StateFlow<Resource<List<CartItem>>> = _cartItemsFlow.asStateFlow()

    private val _wishListItemsFlow: MutableStateFlow<List<WishListItem>> =
        MutableStateFlow(listOf())
    val wishListItemsFlow: StateFlow<List<WishListItem>> =
        _wishListItemsFlow.asStateFlow()

    fun insertCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            _cartFlow.emit(safeCall { repo.insertCartItem(cartItem) })
        }
    }

    fun insertWishListItem(product: WishListItem) {
        viewModelScope.launch {
            _wishListFlow.emit(safeCall { repo.insertWishListItem(product) })
        }
    }

    fun getLocallyCartItems() {
        viewModelScope.launch {
            _cartItemsFlow.emit(safeCall { repo.getCartItems() })
        }
    }

    fun getLocallyWishListItems() {
        viewModelScope.launch {
             repo.getWishListItems().collect{
                 _wishListItemsFlow.emit(it)
             }
        }
    }

    fun updateCartDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _updateCartFlow.emit(safeApiCall {
                repo.modifyCartDraft(
                    DraftRequest(
                        DraftOrder(
                            line_items = lineItems
                        )
                    )
                )
            })
        }
    }

    fun updateWishListDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _updateWishListFlow.emit(safeApiCall {
                repo.modifyWishListDraft(
                    DraftRequest(
                        DraftOrder(
                            line_items = lineItems
                        )
                    )
                )
            })
        }
    }

    fun createCartDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _creationCartFlow.emit(safeApiCall {
                repo.createCartDraft(
                    DraftRequest(
                        DraftOrder(
                            line_items = lineItems
                        )
                    )
                )
            })
        }
    }

    fun createWishListDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _creationWishListFlow.emit(safeApiCall {
                repo.createWishListDraft(
                    DraftRequest(
                        DraftOrder(line_items = lineItems)
                    )
                )
            })
        }
    }
    /*repo.getCartItems().collect{
        val lineItems = it.map {cartItem ->
            LineItem(cartItem.quantity,cartItem.variantId, "Custom Tee", 20.00)
        }
        val customerId = repo.getCustomerIdLocally()!!
        Log.i("hahahahaha", "updateCartDraft: custimer id $customerId")
        if (lineItems.size<=1){
            val draftRequest = DraftRequest(DraftOrder(line_items = lineItems, use_customer_default_address = true, appliedDiscount = AppliedDiscount("", "", 0.0, 0.0, "Custom Tee"), customer =  Customer(customerId)))
            _updateCartFlow.emit(safeApiCall { repo.createCartDraft(draftRequest) })
        }else{
            val draftRequest = DraftRequest(DraftOrder(line_items = lineItems, use_customer_default_address = true, appliedDiscount = AppliedDiscount("", "", 0.0, 0.0, "Custom Tee"), customer =  Customer(customerId)))
            _updateCartFlow.emit(safeApiCall { repo.modifyCartDraft(draftRequest) })
        }
    }
}*/


    fun uploadCartId(cartId: Long) {
        viewModelScope.launch {
            Log.i("hahahahaha", "uploadCartId: ==cart id $cartId")
            repo.uploadCartId(cartId)
        }
    }

    fun uploadWishListId(wishListId: Long) {
        Log.i("Cs", "uploadWishListId:$wishListId ")

        viewModelScope.launch {
            Log.i("Cs", "uploadWishListId:$wishListId ")
            repo.uploadWishListId(wishListId).let {

                _wishListIdFlow.emit(Resource.Success(true))


            }
        }
    }

    fun insertCartIdLocally(cartId: Long) {
        viewModelScope.launch {
            repo.setCartIdLocally(cartId)
        }
    }

    fun insertWishListIdLocally(wishListId: Long) {
        viewModelScope.launch {
            repo.setWishListIdLocally(wishListId)
        }
    }

}
