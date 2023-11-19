package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
}