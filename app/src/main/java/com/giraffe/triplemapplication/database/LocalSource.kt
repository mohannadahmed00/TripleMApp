package com.giraffe.triplemapplication.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.products.Product
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


    fun getAllFavorites(): Flow<List<Product>>
    suspend fun insertFavorite(product: Product): Long
    suspend fun deleteFavorite(product: Product): Int
    suspend fun deleteAllFavorites()
    suspend fun updateFavorite(product: Product)


}