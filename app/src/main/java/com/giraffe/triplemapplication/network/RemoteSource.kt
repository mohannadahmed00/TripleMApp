package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    suspend fun getAllProducts(): Flow<AllProductsResponse>

    suspend fun signUpFirebase(
        email: String,
        password: String,

    ): Resource<FirebaseUser>

    suspend fun signInFirebase(email: String, password: String): Resource<FirebaseUser>

    suspend fun addUsername(name: String)
}