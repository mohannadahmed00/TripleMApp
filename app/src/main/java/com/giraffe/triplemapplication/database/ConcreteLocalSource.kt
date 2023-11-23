package com.giraffe.triplemapplication.database

import android.content.Context
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ConcreteLocalSource(context: Context) : LocalSource {
    private val shared: SharedHelper = SharedHelper.getInstance(context)
    private val favoritesDao: FavoritesDao = AppDataBase.getInstance(context).getFavoritesDao()
    private val exchangeRatesDao: ExchangeRatesDao =
        AppDataBase.getInstance(context).getExchangeRatesDao()

    override suspend fun getLanguage(): Flow<String> {
        return flow {
            val lang = shared.read(Constants.LANGUAGE) ?: Constants.Languages.ENGLISH.value
            emit(lang)
        }
    }

    override suspend fun setLanguage(code: Constants.Languages) {
        shared.store(Constants.LANGUAGE, code.value)
    }

    override suspend fun getFirstTimeFlag(): Flow<Boolean> {
        return flow {
            val flag = shared.read(Constants.FIRST_TIME_FLAG)
            emit(flag == null)
        }
    }

    override suspend fun setFirstTimeFlag(flag: Boolean) {
        shared.store(Constants.FIRST_TIME_FLAG, flag.toString())
    }

    override suspend fun setExchangeRates(exchangeRates: ExchangeRatesResponse): Flow<Long> {
        return flow {
            emit(exchangeRatesDao.insertExchangeRates(exchangeRates))
        }
    }

    override suspend fun getCurrency(): Flow<String> {
        return flow {
            val currency = shared.read(Constants.CURRENCY) ?: Constants.Currencies.EGP.value
            emit(currency)
        }
    }

    override suspend fun setCurrency(currency: Constants.Currencies) {
        shared.store(Constants.CURRENCY, currency.value)
    }

    override fun getAllFavorites(): Flow<Product> = favoritesDao.getAllFavorites()


    override suspend fun insertFavorite(product: Product): Long = favoritesDao.insertFavorite(product)
    override suspend fun deleteFavorite(product: Product): Int =favoritesDao.deleteFavorite(product)

    override suspend fun deleteAllFavorites() =favoritesDao.deleteAllFavorites()

    override suspend fun updateFavorite(product: Product) = favoritesDao.updateFavorite(product)
}