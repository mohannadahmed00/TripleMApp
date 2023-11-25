package com.giraffe.triplemapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchangeRates(exchangeRates: ExchangeRatesResponse): Long

    @Query("SELECT * FROM exchange_rates_table")
    fun getExchangeRateOf(): Flow<List<ExchangeRatesResponse>>
}