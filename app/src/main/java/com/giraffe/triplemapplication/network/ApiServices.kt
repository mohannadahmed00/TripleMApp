package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("custom_collections.json")
    suspend fun getAllCategories(): AllCategoriesResponse

    @GET("smart_collections.json")
    suspend fun getAllBrands(): AllBrandsResponse

    @GET("collections/{categoryId}/products.json")
    suspend fun getProductsFromCategoryId(@Path("categoryId") categoryId: String): AllProductsResponse

//    @GET
//    suspend fun getCurrencies(): CurrencyResponse

    @POST("customers.json")
    suspend fun createCustomer(@Body customer : Request) : CustomerResponse



}