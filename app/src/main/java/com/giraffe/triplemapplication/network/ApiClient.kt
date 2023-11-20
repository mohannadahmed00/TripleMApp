package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    private val apiServices = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient())
        .baseUrl(Constants.URL)
        .build().create(ApiServices::class.java)

    override suspend fun getAllProducts() = flow {
        emit(apiServices.getAllProducts())
    }

    override suspend fun getAllCategories() = flow {
        emit(apiServices.getAllCategories())
    }
}