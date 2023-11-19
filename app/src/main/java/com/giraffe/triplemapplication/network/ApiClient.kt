package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Constants
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
    private fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url()
            val url = originalHttpUrl.newBuilder()
                //.addQueryParameter("appid", "")
                .build()
            val requestBuilder: Request.Builder = original.newBuilder()
                .url(url)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClient.build()
    }

    var client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("93122f977e96731187a18ceaab5e7995",
        "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")).build()

    private val apiServices = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
//        .client(provideOkHttpClient())
        .client(client)
        .baseUrl(Constants.URL)
        .build().create(ApiServices::class.java)

    override suspend fun getProducts(): Flow<List<Product>> {
        return flow {
            emit(apiServices.getAllProducts().products)
        }
    }
}

class BasicAuthInterceptor(user: String?, password: String?) :
    Interceptor {
    private val credentials: String

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

    init {
        credentials = Credentials.basic(user!!, password!!)
    }
}