package com.giraffe.triplemapplication.network


import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.address.AddressResponse
import com.giraffe.triplemapplication.model.address.AddressesResponse
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.response.DraftOrder
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.MultipleCustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.discount.CouponsResponse
import com.giraffe.triplemapplication.model.orders.AllOrdersResponse
import com.giraffe.triplemapplication.model.orders.OrderResponse
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.createorderresponse.CreateOrderResponse
import com.giraffe.triplemapplication.model.payment.ephemeralkey.EphemeralKeyResponse
import com.giraffe.triplemapplication.model.payment.paymentintent.PaymentIntentResponse
import com.giraffe.triplemapplication.model.payment.stripecustomer.StripeCustomerResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {


    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getCurrencies(): Flow<ExchangeRatesResponse>

    fun getCustomerByEmail(email :String ):Flow<MultipleCustomerResponse>
    fun signUpFirebase(
        email: String,
        password: String,
    ): Flow<AuthResult>

    fun signInFirebase(email: String, password: String): Flow<AuthResult>

    fun getCurrentUser(): FirebaseUser
    fun isLoggedIn(): Boolean
    fun logout():Flow<Unit>
    fun createCustomer(customer: Request): Flow<CustomerResponse>
    suspend fun getAllCategories(): Flow<AllCategoriesResponse>
    suspend fun getAllBrands(): Flow<AllBrandsResponse>

    suspend fun addNewAddress(
        customerId: String,
        address: AddressRequest,
    ): Flow<Response<AddressResponse>>

    suspend fun getAddresses(
        customerId: String,
    ): Flow<Response<AddressesResponse>>

    suspend fun deleteAddress(
        customerId: String,
        addressId: String,
    ): Flow<Response<Void>>

    suspend fun getProductsFromCategoryId(categoryId: String): Flow<AllProductsResponse>
    suspend fun getAllProductsFromIds(ids: String): Flow<AllProductsResponse>

    suspend fun createOrder(orderCreate: OrderCreate): Flow<CreateOrderResponse>
    suspend fun getOrders(): Flow<AllOrdersResponse>
    suspend fun getOrder(orderId: Long): Flow<OrderResponse>
    suspend fun delOrder(orderId: Long)
    suspend fun completeOrder(orderId: Long): Flow<DraftOrder>

    suspend fun createNewCartDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>>

    suspend fun modifyCartDraft(
        draftOrderId: Long,
        draftRequest: DraftRequest,
    ): Flow<Response<DraftResponse>>

    suspend fun removeCartDraft(
        draftOrderId: Long,
    ): Flow<Response<Void>>

    suspend fun uploadCartId(cartId: Long): Task<Void?>?
    fun getCartId():Flow<Long>

    suspend fun getCoupons(): Flow<Response<CouponsResponse>>

  suspend fun uploadCustomerId(cartId: Long): Task<Void?>?
  
  suspend fun createNewWishListDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>>

    fun getCustomerIdFromFirebase(): Flow<Long>

  suspend fun modifyWishListDraft(
        draftOrderId: Long,
        draftRequest: DraftRequest,
    ): Flow<Response<DraftResponse>>

    suspend fun removeWishListDraft(
        draftOrderId: Long,
    ): Flow<Response<Void>>

    suspend fun uploadWishListId(wishListId: Long): Task<Void?>?
    suspend fun getWishListId(): Flow<Long>

    suspend fun setDefaultAddress(customerId:Long, addressId:Long):Flow<Response<AddressResponse>>


    suspend fun createStripeCustomer():Flow<Response<StripeCustomerResponse>>

    suspend fun createEphemeralKey(customerId: String):Flow<Response<EphemeralKeyResponse>>


    suspend fun createPaymentIntent(
        customerId: String,
        amount: String,
        currency: String
    ):Flow<Response<PaymentIntentResponse>>
}