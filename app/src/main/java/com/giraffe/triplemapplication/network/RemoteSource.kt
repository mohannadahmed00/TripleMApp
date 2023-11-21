package com.giraffe.triplemapplication.network


import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request

import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface RemoteSource {


    suspend fun getAllProducts(): Flow<AllProductsResponse>
    fun signUpFirebase(

        email: String,
        password: String,
        ): Flow<Task<AuthResult>>


     fun signInFirebase(email: String, password: String): Flow<Task<AuthResult>>

    fun getCurrentUser(): FirebaseUser
    fun isLoggedIn(): Boolean
    fun logout()
    fun createCustomer(customer: Request):Flow<CustomerResponse>
    suspend fun getAllCategories(): Flow<AllCategoriesResponse>
    suspend fun getAllBrands(): Flow<AllBrandsResponse>

}