package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
<<<<<<< HEAD
    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(code: Constants.Languages)

=======
>>>>>>> 88a4754c9af20e507c54559d6b32060473a9de04
}