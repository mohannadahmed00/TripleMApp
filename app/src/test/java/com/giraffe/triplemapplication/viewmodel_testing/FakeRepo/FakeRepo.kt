package com.giraffe.triplemapplication.viewmodel_testing.FakeRepo

import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.address.AddressResponse
import com.giraffe.triplemapplication.model.address.AddressesResponse
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.cart.CartItem
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
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class FakeRepo : RepoInterface {
    override suspend fun getAllProducts(): Flow<AllProductsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategories(): Flow<AllCategoriesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBrands(): Flow<AllBrandsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsFromCategoryId(categoryId: String): Flow<AllProductsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProductsFromIds(ids: String): Flow<AllProductsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getLanguage(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun setLanguage(code: Constants.Languages) {
        TODO("Not yet implemented")
    }

    override fun signUpFirebase(
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun signInFirebase(email: String, password: String): Flow<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        return email.matches(emailPattern)
    }

    override fun isPasswordValid(password: String): Boolean {
        val minLength = 8 // Minimum length required for the password
        val uppercasePattern = Regex("[A-Z]") // At least one uppercase letter
        val lowercasePattern = Regex("[a-z]") // At least one lowercase letter
        val digitPattern = Regex("[0-9]") // At least one digit

        val containsUppercase = uppercasePattern.containsMatchIn(password)
        val containsLowercase = lowercasePattern.containsMatchIn(password)
        val containsDigit = digitPattern.containsMatchIn(password)
        val isLengthValid = password.length >= minLength

        return containsUppercase && containsLowercase && containsDigit && isLengthValid

    }

    override fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    override fun isDataValid(email: String, password: String, confirmPassword: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password) && doPasswordsMatch(
            password,
            confirmPassword
        )
    }

    override fun getCurrentUser(): Flow<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun logout(): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun createCustomer(customer: Request): Flow<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFirstTimeFlag(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setFirstTimeFlag(flag: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrencies(): Flow<ExchangeRatesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun setExchangeRates(exchangeRates: ExchangeRatesResponse): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrency(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrency(currency: Constants.Currencies) {
        TODO("Not yet implemented")
    }

    override suspend fun addNewAddress(address: AddressRequest): Flow<Response<AddressResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddresses(): Flow<Response<AddressesResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAddress(
        customerId: String,
        addressId: String,
    ): Flow<Response<Void>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadCartId(cartId: Long): Task<Void?>? {
        TODO("Not yet implemented")
    }

    override suspend fun uploadCustomerId(cartId: Long): Task<Void?>? {
        TODO("Not yet implemented")
    }

    override suspend fun insertCartItem(cartItem: CartItem): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyCartDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun createCartDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartItems(): Flow<List<CartItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCartItems() {
        TODO("Not yet implemented")
    }

    override suspend fun removeCartDraft(draftOrderId: Long): Flow<Response<Void>> {
        TODO("Not yet implemented")
    }

    override fun getCartId(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun getCustomerIdFromFirebase(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerIdLocally(): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun uploadWishListId(wishListId: Long): Task<Void?>? {
        TODO("Not yet implemented")
    }

    override suspend fun insertWishListItem(product: WishListItem): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWishListItem(product: WishListItem): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWishListItem() {
        TODO("Not yet implemented")
    }

    override suspend fun modifyWishListDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun createWishListDraft(draftRequest: DraftRequest): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override fun getWishListItems(): Flow<List<WishListItem>> {
        TODO("Not yet implemented")
    }

    override fun getCustomerByEmail(email: String): Flow<MultipleCustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getWishListId(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getExchangeRateOf(currencyCode: Constants.Currencies): Flow<Pair<Double, Double>> {
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

    override suspend fun getCoupons(): Flow<Response<CouponsResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun setCartIdLocally(cartId: Long?) {
        TODO("Not yet implemented")
    }

    override suspend fun setWishListIdLocally(cartId: Long?) {
        TODO("Not yet implemented")
    }

    override suspend fun setCustomerIdLocally(cartId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun setFullNameLocally(fullName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getFullName(): Flow<String?> {
        TODO("Not yet implemented")
    }

    override suspend fun setDefaultAddress(addressId: Long): Flow<Response<AddressResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCartItem(cartItem: CartItem): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun clearData(): Flow<Unit> {
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

    override suspend fun getSingleWish(cartId: Long): Flow<Response<DraftResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getListOfProducts(ids: String): Flow<Response<AllProductsResponse>> {
        TODO("Not yet implemented")
    }
}