package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.products.Product
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getProducts(): Flow<List<Product>>
}