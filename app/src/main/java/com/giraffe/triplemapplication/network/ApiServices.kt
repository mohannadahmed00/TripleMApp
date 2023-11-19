package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import retrofit2.http.GET

interface ApiServices {

    @GET("products.json")
    suspend fun getAllProducts(): AllProductsResponse
}