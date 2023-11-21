package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient: RemoteSource {

    private fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(Constants.SHOPIFY_HEADER, Constants.ACCESS_TOKEN)
                .build()
            chain.proceed(request)
        }
        return httpClient.build()
    }

    private fun getApiServices(url:String = Constants.URL):ApiServices {
        val apiServices = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
        if (url==Constants.URL){
            apiServices.client(provideOkHttpClient())
        }
        return apiServices.build().create(ApiServices::class.java)
    }

        
        
    override suspend fun getAllProducts() = flow {
        emit(getApiServices().getAllProducts())
    }

    override suspend fun getCurrencies()= flow {
        emit(getApiServices(Constants.CURRENCY_URL).getExchangeRates())
    }
    override suspend fun getAllCategories() = flow {
        emit(getApiServices().getAllCategories())
    }

    override suspend fun getAllBrands() = flow {
        emit(getApiServices().getAllBrands())
    }

    override suspend fun getProductsFromCategoryId(categoryId: String) = flow {
        emit(getApiServices().getProductsFromCategoryId(categoryId))
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