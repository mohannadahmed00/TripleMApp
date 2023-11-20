package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.await
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Credentials
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object ApiClient: RemoteSource {
    //http://api.exchangeratesapi.io/v1/latest?access_key=4ee6d3381b90ee1d4e7a0c551205269f
    private fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Shopify-Access-Token", Constants.ACCESS_TOKEN)
                .build()
            chain.proceed(request)
        }
        return httpClient.build()
    }
    private fun getApiServices(url: String = Constants.URL) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient())
        .baseUrl(url)
        .build().create(ApiServices::class.java)
        
        
    override suspend fun getAllProducts() = flow {
        emit(apiServices.getAllProducts())
    }

    override suspend fun getAllCategories() = flow {
        emit(apiServices.getAllCategories())
    }

    override suspend fun getAllBrands() = flow {
        emit(apiServices.getAllBrands())
    }

    override suspend fun signUpFirebase(
        email: String,
        password: String,

        ): Flow<Task<AuthResult>> = flow {
        val result = createFirebaseAuth().createUserWithEmailAndPassword(email, password)
        emit(result)
    }

    override suspend fun signInFirebase(
        email: String,
        password: String,
    ): Flow<Task<AuthResult>> = flow {
        val result = createFirebaseAuth().signInWithEmailAndPassword(email, password)
        emit(result)
    }


    override fun getCurrentUser(): FirebaseUser {

        return createFirebaseAuth().currentUser!!
    }

    override fun isLoggedIn(): Boolean {
        return createFirebaseAuth().currentUser != null
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }


    private fun createFirebaseAuth(): FirebaseAuth = Firebase.auth

}