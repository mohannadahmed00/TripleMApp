package com.giraffe.triplemapplication.network
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import kotlinx.coroutines.flow.Flow
interface RemoteSource {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
}