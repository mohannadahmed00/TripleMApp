package com.giraffe.triplemapplication.model.currency

import androidx.room.Entity

@Entity(tableName = "exchange_rates_table", primaryKeys = ["base"])
data class ExchangeRatesResponse(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int,
)