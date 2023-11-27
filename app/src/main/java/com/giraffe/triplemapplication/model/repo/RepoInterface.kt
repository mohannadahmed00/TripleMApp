package com.giraffe.triplemapplication.model.repo



import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.address.AddressResponse
import com.giraffe.triplemapplication.model.address.AddressesResponse
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.MultipleCustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.discount.CouponsResponse
import com.giraffe.triplemapplication.model.orders.AllOrdersResponse
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.createorderresponse.CreateOrderResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getAllCategories(): Flow<AllCategoriesResponse>
    suspend fun getAllBrands(): Flow<AllBrandsResponse>
    suspend fun getProductsFromCategoryId(categoryId: String): Flow<AllProductsResponse>
    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(code: Constants.Languages)

    fun signUpFirebase(email: String, password: String, confirmPassword: String): Flow<AuthResult>
    fun signInFirebase(email: String, password: String): Flow<AuthResult>

    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean
    fun isDataValid(email: String, password: String, confirmPassword: String): Boolean
    fun getCurrentUser(): FirebaseUser
    fun isLoggedIn(): Boolean
    fun logout() : Flow<Unit>

    fun createCustomer(customer: Request): Flow<CustomerResponse>

    suspend fun getFirstTimeFlag(): Flow<Boolean>
    suspend fun setFirstTimeFlag(flag: Boolean)
    suspend fun getCurrencies(): Flow<ExchangeRatesResponse>

    suspend fun setExchangeRates(exchangeRates: ExchangeRatesResponse): Flow<Long>

    suspend fun getCurrency(): Flow<String>

    suspend fun setCurrency(currency: Constants.Currencies)

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


    suspend fun uploadCartId(cartId: Long): Task<Void?>?
    suspend fun uploadCustomerId(cartId: Long): Task<Void?>?

    suspend fun insertCartItem(cartItem: CartItem): Flow<Long>

    suspend fun modifyCartDraft(variants: List<LineItem>): Flow<Response<DraftResponse>>
    suspend fun createCartDraft(variants: List<LineItem>): Flow<Response<DraftResponse>>

    suspend fun getCartItems(): Flow<List<CartItem>>

    fun getCartId(): Flow<Long>
    fun getCustomerIdFromFirebase(): Flow<Long>
    fun getCustomerIdLocally(): Long?
    suspend fun uploadWishListId(wishListId: Long): Task<Void?>?

    suspend fun insertWishListItem(product: Product): Flow<Long>
    suspend fun deleteWishListItem(product: Product): Flow<Int>
    suspend fun deleteAllWishListItem()

    suspend fun modifyWishListDraft(variants: List<LineItem>): Flow<Response<DraftResponse>>
    suspend fun createWishListDraft(variants: List<LineItem>): Flow<Response<DraftResponse>>

    suspend fun getWishListItems(): Flow<List<Product>>
    fun getCustomerByEmail(email: String): Flow<MultipleCustomerResponse>


    fun getWishListId(): Flow<Long>

    suspend fun getExchangeRateOf(currencyCode: Constants.Currencies): Flow<Pair<Double, Double>>


    suspend fun createOrder(orderCreate: OrderCreate): Flow<CreateOrderResponse>
    suspend fun getOrders(): Flow<AllOrdersResponse>
    suspend fun delOrder(orderId: Long)
    suspend fun getCoupons(): Flow<Response<CouponsResponse>>

    fun setCartIdLocally(cartId: Long?) :Flow<Unit>
    fun setWishListIdLocally(cartId: Long?) : Flow<Unit>
    fun setCustomerIdLocally(cartId: Long) : Flow<Unit>

    suspend fun clearData() : Flow<Unit>
}