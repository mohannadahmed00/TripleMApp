package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getAllCategories(): Flow<AllCategoriesResponse>
    suspend fun getAllBrands(): Flow<AllBrandsResponse>
    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(code: Constants.Languages)

    suspend fun getFirstTimeFlag(): Flow<Boolean>
    suspend fun setFirstTimeFlag(flag:Boolean)
}