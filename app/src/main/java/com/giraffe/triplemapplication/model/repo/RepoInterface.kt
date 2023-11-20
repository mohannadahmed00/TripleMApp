package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(code: Constants.Languages)

    suspend fun signUpFirebase(email: String, password: String , confirmPassword : String): Flow<Task<AuthResult>>
    suspend fun signInFirebase(email: String, password: String): Flow<Task<AuthResult>>

    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
    fun doPasswordsMatch(password: String , confirmPassword : String): Boolean
    fun isDataValid(email: String, password: String, confirmPassword: String): Boolean
    fun getCurrentUser() : FirebaseUser
    fun isLoggedIn() : Boolean
    fun logout()

    suspend fun getFirstTimeFlag(): Flow<Boolean>
    suspend fun setFirstTimeFlag(flag:Boolean)

}