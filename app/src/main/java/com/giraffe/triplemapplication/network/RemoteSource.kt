package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getAllCategories(): Flow<AllCategoriesResponse>
    suspend fun getAllBrands(): Flow<AllBrandsResponse>
}