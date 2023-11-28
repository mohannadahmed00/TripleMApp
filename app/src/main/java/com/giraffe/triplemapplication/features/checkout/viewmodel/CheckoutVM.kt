package com.giraffe.triplemapplication.features.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.address.AddressesResponse
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.response.DraftOrder
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.customers.CustomerDetails
import com.giraffe.triplemapplication.model.orders.OrderResponse
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.createorderresponse.CreateOrderResponse
import com.giraffe.triplemapplication.model.payment.ephemeralkey.EphemeralKeyResponse
import com.giraffe.triplemapplication.model.payment.paymentintent.PaymentIntentResponse
import com.giraffe.triplemapplication.model.payment.stripecustomer.StripeCustomerResponse
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeApiCall
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CheckoutVM(private val repo: RepoInterface): ViewModel() {

    private val _ordersFlow: MutableStateFlow<Resource<CreateOrderResponse>> = MutableStateFlow(
        Resource.Loading)
    val ordersFlow: StateFlow<Resource<CreateOrderResponse>> = _ordersFlow.asStateFlow()

    private val _draftOrderFlow: MutableStateFlow<Resource<DraftOrder>> = MutableStateFlow(
        Resource.Loading)
    val draftOrderFlow: StateFlow<Resource<DraftOrder>> = _draftOrderFlow.asStateFlow()

    private val _addressesFlow: MutableStateFlow<Resource<AddressesResponse>> = MutableStateFlow(
        Resource.Loading)
    val addressesFlow: StateFlow<Resource<AddressesResponse>> = _addressesFlow.asStateFlow()

    private val _updateCartFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    val updateCartFlow: StateFlow<Resource<DraftResponse>> = _updateCartFlow.asStateFlow()

    private val _orderFlow: MutableStateFlow<Resource<OrderResponse>> =
        MutableStateFlow(Resource.Loading)
    val orderFlow: StateFlow<Resource<OrderResponse>> = _orderFlow.asStateFlow()

//    private val _customerName: MutableStateFlow<Resource<CustomerDetails>> = MutableStateFlow(Resource.Loading)
//    val customerName: StateFlow<Resource<CustomerDetails>> = _customerName.asStateFlow()
    var customerId: Long = 0L

    init {
//        getCustomerName()
//        getOrderDetails()
        getCustomerId()
    }

    fun getCustomerId() {
        viewModelScope.launch {
            customerId = repo.getCustomerIdLocally() ?: 0L
        }
    }

//    private fun getCustomerName() {
//        viewModelScope.launch {
//            val customerId = repo.getCustomerIdLocally() ?: 0
//            Log.i("hahahahahaha", "getCustomerName: $customerId")
//            _customerName.emit(safeCall { repo.getCustomerById(customerId) })
//        }
//    }

    fun checkout(orderCreate: OrderCreate) {
        viewModelScope.launch {
            _ordersFlow.emit(safeCall { repo.createOrder(orderCreate) })
        }
    }

    fun getOrderDetails(cartId: Long) {
        viewModelScope.launch {
            Log.i("package:mine CheckoutFragment\n", "getOrderDetails cart idoha: $cartId")
            _orderFlow.emit(safeCall { repo.getOrder(5276043771979) })
        }
    }

    fun completeOrder() {
        viewModelScope.launch {
            Log.i("hahahahaha", "completeOrder: cart id - ${repo.getCartId().first()}")
            _draftOrderFlow.emit(safeCall { repo.completeOrder(repo.getCartId().first()) })
        }
    }

    fun modifyOrder(draftOrder: com.giraffe.triplemapplication.model.cart.request.DraftOrder) {
        viewModelScope.launch {
            _updateCartFlow.emit(safeApiCall { repo.modifyCartDraft(DraftRequest(draftOrder)) })
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

    fun getAddresses(){
        viewModelScope.launch(Dispatchers.IO) {
            val customerId = repo.getCustomerIdLocally().toString()
            Log.i("hahahahahahahaha", "getAddresses: $customerId")
          _addressesFlow.emit(safeApiCall{ repo.getAddresses(customerId) })
        }
    }

    private val _stripeCustomerFlow: MutableStateFlow<Resource<StripeCustomerResponse>> =
        MutableStateFlow(Resource.Loading)
    val stripeCustomerFlow: StateFlow<Resource<StripeCustomerResponse>> =
        _stripeCustomerFlow.asStateFlow()

    private val _ephemeralFlow: MutableStateFlow<Resource<EphemeralKeyResponse>> =
        MutableStateFlow(Resource.Loading)
    val ephemeralFlow: StateFlow<Resource<EphemeralKeyResponse>> = _ephemeralFlow.asStateFlow()

    private val _paymentIntentFlow: MutableStateFlow<Resource<PaymentIntentResponse>> =
        MutableStateFlow(Resource.Loading)
    val paymentIntentFlow: StateFlow<Resource<PaymentIntentResponse>> =
        _paymentIntentFlow.asStateFlow()


    fun createStripeCustomer() {
        viewModelScope.launch {
            _stripeCustomerFlow.emit(safeApiCall { repo.createStripeCustomer() })
        }
    }

    fun createEphemeralKey(customerId: String) {
        viewModelScope.launch {
            _ephemeralFlow.emit(safeApiCall { repo.createEphemeralKey(customerId) })
        }
    }

    fun createPaymentIntent(
        customerId: String,
        amount: String,
        currency: String
    ) {
        viewModelScope.launch {
            _paymentIntentFlow.emit(safeApiCall {repo.createPaymentIntent(customerId, amount,currency)})
        }
    }
}