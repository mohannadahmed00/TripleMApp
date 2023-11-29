package com.giraffe.triplemapplication.database

import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.Flow


interface LocalSource {
    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(code:Constants.Languages)

    suspend fun getFirstTimeFlag(): Flow<Boolean>
    suspend fun setFirstTimeFlag(flag:Boolean)

    suspend fun setExchangeRates(exchangeRates: ExchangeRatesResponse): Flow<Long>

    suspend fun getCurrency(): Flow<String>

    suspend fun setCurrency(currency:Constants.Currencies)

    suspend fun setCartID(id:Long?)
    suspend fun setCustomerID(id:Long)
    suspend fun getCustomerID() : Long?
    suspend fun getCartID():Long?

    suspend fun setFullNameLocally(fullName:String)
    suspend fun getFullName():Flow<String?>

    suspend fun getCartItems(): Flow<List<CartItem>>

    suspend fun insertCartItem(cartItem: CartItem): Flow<Long>

    suspend fun deleteCartItem(cartItem: CartItem): Flow<Int>

    suspend fun deleteAllCartItems()

    suspend fun updateCartItem(cartItem: CartItem)

    suspend fun setWishListID(id:Long?)
    suspend fun getWishListID():Long?

    fun getWishListItems(): Flow<List<WishListItem>>

    suspend fun insertWishListItem(product: WishListItem): Flow<Long>

    suspend fun deleteWishListItem(product: WishListItem): Flow<Int>

    suspend fun deleteAllWishListItems()

    suspend fun updateWishListItem(product: WishListItem)

    suspend fun getExchangeRateOf(currencyCode:Constants.Currencies):Flow<Pair<Double,Double>>

    suspend fun clearData() : Flow<Unit>
}