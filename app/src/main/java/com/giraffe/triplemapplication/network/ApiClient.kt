package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient: RemoteSource {
    private fun provideOkHttpClient(header:String=Constants.SHOPIFY_HEADER,value:String=Constants.ACCESS_TOKEN): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(header, value)
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
}