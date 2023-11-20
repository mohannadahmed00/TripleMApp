package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface RemoteSource {
    suspend fun getAllProducts(): Flow<AllProductsResponse>

    suspend fun signUpFirebase(
        email: String,
        password: String,

        ): Flow<Task<AuthResult>>

    suspend fun signInFirebase(email: String, password: String): Flow<Task<AuthResult>>

    fun getCurrentUser(): FirebaseUser
    fun isLoggedIn(): Boolean
    fun logout()
}