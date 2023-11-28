package com.giraffe.triplemapplication.database

import android.content.Context
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ConcreteLocalSource(context: Context) : LocalSource {
    private val shared: SharedHelper = SharedHelper.getInstance(context)
    private val favoritesDao: FavoritesDao = AppDataBase.getInstance(context).getFavoritesDao()
    private val exchangeRatesDao: ExchangeRatesDao =
        AppDataBase.getInstance(context).getExchangeRatesDao()
    private val cartDao: CartDao = AppDataBase.getInstance(context).getCartDao()

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

    override suspend fun getExchangeRateOf(currencyCode: Constants.Currencies) = flow {
        exchangeRatesDao.getExchangeRateOf().collect {
            if (it.isNotEmpty()) {
                val exchangeRates = it[0]
                when (currencyCode.value) {
                    Constants.Currencies.EGP.value -> {
                        emit(Pair(exchangeRates.rates.EGP, exchangeRates.rates.EGP))
                    }

                    Constants.Currencies.USD.value -> {
                        emit(Pair(exchangeRates.rates.EGP, exchangeRates.rates.USD))
                    }

                    Constants.Currencies.EUR.value -> {
                        emit(Pair(exchangeRates.rates.EGP, exchangeRates.rates.EUR))
                    }

                    Constants.Currencies.GBP.value -> {
                        emit(Pair(exchangeRates.rates.EGP, exchangeRates.rates.GBP))
                    }
                }
            }
        }
    }

    override suspend fun clearData() :Flow<Unit> = flow{

        shared.store(Constants.CUSTOMER_ID, null)
        shared.store(Constants.CART_ID, null)
        shared.store(Constants.WISH_LIST_ID, null)

        emit(cartDao.deleteAllCartItems())
        emit(favoritesDao.deleteAllFavorites())

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

    override suspend fun setCartID(id: Long?) {
        shared.store(Constants.CART_ID, id.toString())
    }

    override suspend fun setCustomerID(id: Long) {


        shared.store(Constants.CUSTOMER_ID, id.toString())

    }

    override suspend fun getCustomerID(): Long? {

       return  shared.read(Constants.CUSTOMER_ID)?.toLong()
    }

    override suspend fun getCartID(): Long? {

        return shared.read(Constants.CART_ID)?.toLong()
    }

    override suspend fun setFullNameLocally(fullName: String) {
        shared.store(Constants.FULL_NAME,fullName)
    }

    override suspend fun getFullName(): Flow<String?> {
        return flow { emit(shared.read(Constants.FULL_NAME)) }
    }

    override suspend fun getCartItems(): Flow<List<CartItem>> {
        return flow { emit(cartDao.getCartItems()) }
    }

    override suspend fun insertCartItem(cartItem: CartItem): Flow<Long> {
        return flow {
            emit(cartDao.insertCartItem(cartItem))
        }
    }

    override suspend fun deleteCartItem(cartItem: CartItem): Flow<Int> {
        return flow {
            emit(cartDao.deleteCartItem(cartItem))
        }
    }

    override suspend fun deleteAllCartItems() {
        cartDao.deleteAllCartItems()
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem)
    }

    override suspend fun setWishListID(id: Long?){
        shared.store(Constants.WISH_LIST_ID, id.toString())
    }

    override suspend fun getWishListID(): Long? {
        return shared.read(Constants.WISH_LIST_ID)?.toLong()
    }

    override fun getWishListItems(): Flow<List<WishListItem>> {
        return favoritesDao.getAllFavorites()

    }

    override  fun insertWishListItem(product: WishListItem): Flow<Long> = flow {
        emit(favoritesDao.insertFavorite(product))
    }

    override suspend fun deleteWishListItem(product: WishListItem): Flow<Int> = flow {
        emit(favoritesDao.deleteFavorite(product))
    }

    override suspend fun deleteAllWishListItems() {
        favoritesDao.deleteAllFavorites()
    }

    override suspend fun updateWishListItem(product: WishListItem) {
        favoritesDao.updateFavorite(product)
    }

}