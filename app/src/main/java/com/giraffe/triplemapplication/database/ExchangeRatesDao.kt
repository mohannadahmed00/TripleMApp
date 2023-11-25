package com.giraffe.triplemapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse

@Dao
interface ExchangeRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchangeRates(exchangeRates: ExchangeRatesResponse): Long
}