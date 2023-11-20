package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import retrofit2.http.GET


interface ApiServices {

    @GET("products.json")
    suspend fun getAllProducts(): AllProductsResponse

    @GET("custom_collections.json")
    suspend fun getAllCategories(): AllCategoriesResponse

    @GET("smart_collections.json")
    suspend fun getAllBrands(): AllBrandsResponse
}