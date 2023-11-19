package com.giraffe.triplemapplication.model.repo

import com.giraffe.triplemapplication.network.RemoteSource
import com.giraffe.triplemapplication.database.LocalSource
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import kotlinx.coroutines.flow.Flow

class Repo private constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) : RepoInterface {
    companion object {
        @Volatile
        private var INSTANCE: Repo? = null
        fun getInstance(remoteSource: RemoteSource, localSource: LocalSource): Repo {
            return INSTANCE ?: synchronized(this) {
                val instance = Repo(remoteSource, localSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getAllProducts(): Flow<AllProductsResponse> {
        return remoteSource.getAllProducts()
    }


}