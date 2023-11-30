package com.giraffe.triplemapplication.repo_testing.fakeremotesource

import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.address.AddressResponse
import com.giraffe.triplemapplication.model.address.AddressesResponse
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.response.DraftOrder
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.customers.CustomerDetails
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
import com.giraffe.triplemapplication.network.RemoteSource
import com.giraffe.triplemapplication.repo_testing.FakeData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeRemoteSource : RemoteSource {
    override suspend fun getAllProducts(): Flow<AllProductsResponse> {
        return flow{
            emit(FakeData.FakeAllProductsResponse)
        }
    }

    override suspend fun getCurrencies(): Flow<ExchangeRatesResponse> {
        TODO("Not yet implemented")
    }

    override fun getCustomerByEmail(email: String): Flow<MultipleCustomerResponse> {
        TODO("Not yet implemented")
    }

    override fun signUpFirebase(email: String, password: String): Flow<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun signInFirebase(email: String, password: String): Flow<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): Flow<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        return FakeData.isLoggedIn
    }

    override fun logout(): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun createCustomer(customer: Request): Flow<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategories(): Flow<AllCategoriesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBrands(): Flow<AllBrandsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewAddress(
        customerId: String,
        address: AddressRequest,
    ): Flow<Response<AddressResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddresses(customerId: String): Flow<Response<AddressesResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAddress(
        customerId: String,
        addressId: String,
    ): Flow<Response<Void>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsFromCategoryId(categoryId: String): Flow<AllProductsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductsFromIds(ids: String): Flow<AllProductsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createOrder(orderCreate: OrderCreate): Flow<CreateOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(): Flow<AllOrdersResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrder(orderId: Long): Flow<OrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun delOrder(orderId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun completeOrder(orderId: Long): Flow<DraftOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun createNewCartDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyCartDraft(
        draftOrderId: Long,
        draftRequest: DraftRequest,
    ): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeCartDraft(draftOrderId: Long): Flow<Response<Void>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadCartId(cartId: Long): Task<Void?>? {
        TODO("Not yet implemented")
    }

    override fun getCartId(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getCoupons(): Flow<Response<CouponsResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadCustomerId(cartId: Long): Task<Void?>? {
        TODO("Not yet implemented")
    }

    override suspend fun createNewWishListDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override fun getCustomerIdFromFirebase(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyWishListDraft(
        draftOrderId: Long,
        draftRequest: DraftRequest,
    ): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeWishListDraft(draftOrderId: Long): Flow<Response<Void>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadWishListId(wishListId: Long): Task<Void?>? {
        TODO("Not yet implemented")
    }

    override suspend fun getWishListId(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long,
    ): Flow<Response<AddressResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerById(customerId: Long): Flow<CustomerDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun createStripeCustomer(): Flow<Response<StripeCustomerResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun createEphemeralKey(customerId: String): Flow<Response<EphemeralKeyResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun createPaymentIntent(
        customerId: String,
        amount: String,
        currency: String,
    ): Flow<Response<PaymentIntentResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleCart(cartId: Long): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleWishList(cartId: Long): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListOfProducts(ids: String): Flow<Response<AllProductsResponse>> {
        TODO("Not yet implemented")
    }

}