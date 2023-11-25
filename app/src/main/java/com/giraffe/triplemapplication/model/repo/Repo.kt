package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.database.LocalSource
import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.network.RemoteSource
import com.giraffe.triplemapplication.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class Repo private constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) : RepoInterface {
    companion object {
        private const val TAG = "Repository"
        @Volatile
        private var INSTANCE: Repo? = null
        fun getInstance(remoteSource: RemoteSource, localSource: LocalSource): Repo {
            return INSTANCE ?: synchronized(this) {
                val instance = Repo(remoteSource, localSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getAllProducts() = remoteSource.getAllProducts()

    override suspend fun getAllCategories() = remoteSource.getAllCategories()

    override suspend fun getAllBrands() = remoteSource.getAllBrands()
    override suspend fun getProductsFromCategoryId(categoryId: String) =
        remoteSource.getProductsFromCategoryId(categoryId)

    override suspend fun getLanguage() = localSource.getLanguage()

    override suspend fun setLanguage(code: Constants.Languages) = localSource.setLanguage(code)

    override fun signUpFirebase(
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<AuthResult> = remoteSource.signUpFirebase(email, password)


    override fun isDataValid(email: String, password: String, confirmPassword: String): Boolean {
        return isEmailValid(email) && isPasswordValid(password) && doPasswordsMatch(
            password,
            confirmPassword
        )

    }

    override fun getCurrentUser(): FirebaseUser {
        return remoteSource.getCurrentUser()
    }

    override fun isLoggedIn(): Boolean {
        return remoteSource.isLoggedIn()
    }

    override fun logout() {
        remoteSource.logout()
    }

    override fun createCustomer(customer: Request): Flow<CustomerResponse> =
        remoteSource.createCustomer(customer)


    override fun signInFirebase(email: String, password: String): Flow<AuthResult> =
        remoteSource.signInFirebase(email, password)

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

    override suspend fun getFirstTimeFlag() = localSource.getFirstTimeFlag()

    override suspend fun setFirstTimeFlag(flag: Boolean) = localSource.setFirstTimeFlag(flag)
    override suspend fun getCurrencies() = remoteSource.getCurrencies()
    override suspend fun setExchangeRates(exchangeRates: ExchangeRatesResponse) =
        localSource.setExchangeRates(exchangeRates)

    override suspend fun getCurrency() = localSource.getCurrency()
    override suspend fun setCurrency(currency: Constants.Currencies) =
        localSource.setCurrency(currency)

    override suspend fun addNewAddress(
        customerId: String,
        address: AddressRequest
    ) = remoteSource.addNewAddress(customerId, address)

    override suspend fun getAddresses(customerId: String) = remoteSource.getAddresses(customerId)
    override suspend fun deleteAddress(
        customerId: String,
        addressId: String
    ) = remoteSource.deleteAddress(customerId, addressId)

    override suspend fun uploadCartId(cartId: Long): Task<Void?>? {
        localSource.setCartID(cartId)//create fun here
        return remoteSource.uploadCartId(cartId)
    }

    override suspend fun insertCartItem(cartItem: CartItem):Flow<Long> {
        //localSource.deleteAllCartItems()
        return localSource.insertCartItem(cartItem)
    }
    override suspend fun modifyCartDraft(variants :List<LineItem>): Flow<Response<DraftResponse>> {
        return remoteSource.modifyCartDraft(localSource.getCartID()?:0, variants)
    }

    override suspend fun createCartDraft(variants: List<LineItem>): Flow<Response<DraftResponse>> {
        return remoteSource.createNewCartDraft(variants)
    }

    override suspend fun getCartItems(): Flow<List<CartItem>>{
        return localSource.getCartItems()
    }

    override suspend fun getCartId(): Flow<Long> {
        return remoteSource.getCartId()
    }

    override suspend fun getExchangeRateOf(currencyCode: Constants.Currencies): Flow<Pair<Double,Double>> {
        return localSource.getExchangeRateOf(currencyCode)
    }
    override fun getAllFavorites(): Flow<List<Product>>  = localSource.getAllFavorites()


    override suspend fun insertFavorite(product: Product): Long = localSource.insertFavorite(product)

    override suspend fun deleteFavorite(product: Product): Int = localSource.deleteFavorite(product)

    override suspend fun deleteAllFavorites() = localSource.deleteAllFavorites()

    override suspend fun updateFavorite(product: Product) = localSource.updateFavorite(product)

    override suspend fun createOrder(orderCreate: OrderCreate) = remoteSource.createOrder(orderCreate)

    override suspend fun getOrders() = remoteSource.getOrders()

    override suspend fun delOrder(orderId: Long) = remoteSource.delOrder(orderId)


}