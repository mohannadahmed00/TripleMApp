package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiServices {

    @GET("products.json")
    suspend fun getAllProducts(): AllProductsResponse
    @GET("v1/latest")
    suspend fun getExchangeRates(
        @Query("access_key") accessKey: String="4ee6d3381b90ee1d4e7a0c551205269f",
        @Query("symbols") symbols: String = "USD,EUR,GBP,EGP",
        @Query("format") format: Int = 1
    ): ExchangeRatesResponse
}