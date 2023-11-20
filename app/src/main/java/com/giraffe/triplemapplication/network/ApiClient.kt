package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.await
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient : RemoteSource {
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

    private val apiServices = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient())
        .baseUrl(Constants.URL)
        .build().create(ApiServices::class.java)

    override suspend fun getAllProducts() = flow {
        emit(apiServices.getAllProducts())
    }

    override suspend fun signUpFirebase(
        email: String,
        password: String,

    ): Resource<FirebaseUser> {
        return try{

            val result = createFirebaseAuth().createUserWithEmailAndPassword(email, password).await()
//            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
            Resource.Success(result.user!!)
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(true ,e.hashCode() , e.message)
        }

    }

    override suspend fun signInFirebase(
        email: String,
        password: String,
    ): Resource<FirebaseUser> {

        return try{
            val result = createFirebaseAuth().signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(true ,e.hashCode() , e)
        }
    }

    override suspend fun addUsername(name: String) {
        TODO("Not yet implemented")
    }


    private fun createFirebaseAuth(): FirebaseAuth = Firebase.auth

}