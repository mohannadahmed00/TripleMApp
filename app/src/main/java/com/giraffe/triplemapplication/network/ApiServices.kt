package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.currency.CurrencyResponse
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiServices {

    @GET("products.json")
    suspend fun getAllProducts(): AllProductsResponse
    @GET
    suspend fun getCurrencies(): CurrencyResponse

    @POST("customers.json")
    suspend fun createCustomer(@Body customer : Request) : CustomerResponse

}