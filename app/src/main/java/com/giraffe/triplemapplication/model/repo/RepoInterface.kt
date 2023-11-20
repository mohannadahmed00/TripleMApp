package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(code: Constants.Languages)
    suspend fun signUpFirebase(email: String, password: String , confirmPassword : String): Resource<FirebaseUser>
    suspend fun signInFirebase(email: String, password: String): Resource<FirebaseUser>

    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
    fun doPasswordsMatch(password: String , confirmPassword : String): Boolean
}