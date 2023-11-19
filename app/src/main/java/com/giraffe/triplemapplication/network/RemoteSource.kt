package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.products.Product
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    suspend fun getProducts(): Flow<List<Product>>
}