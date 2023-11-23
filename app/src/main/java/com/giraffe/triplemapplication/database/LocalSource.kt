package com.giraffe.triplemapplication.database

import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
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

    suspend fun setDraftID(id:Long)
    suspend fun getDraftID():Long?

    fun getCartItems(): Flow<List<CartItem>>

    suspend fun insertCartItem(cartItem: CartItem): Flow<Long>

    suspend fun deleteCartItem(cartItem: CartItem): Flow<Int>

    suspend fun deleteAllCartItems()

    suspend fun updateCartItem(cartItem: CartItem)

}