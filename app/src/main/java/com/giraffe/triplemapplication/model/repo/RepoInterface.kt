package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(code: Constants.Languages)

}