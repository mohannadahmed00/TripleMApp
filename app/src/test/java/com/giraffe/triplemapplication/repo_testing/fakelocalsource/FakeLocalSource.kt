package com.giraffe.triplemapplication.repo_testing.fakelocalsource

import com.giraffe.triplemapplication.database.LocalSource
import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.repo_testing.FakeData
import com.giraffe.triplemapplication.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalSource : LocalSource {
    override suspend fun getLanguage(): Flow<String> {
        return flow {

            val currentLanguage = FakeData.currentLanguage ?: Constants.Languages.ENGLISH
            emit(currentLanguage.value)
        }
    }

    override suspend fun setLanguage(code: Constants.Languages) {
        FakeData.currentLanguage = code
    }

    override suspend fun getFirstTimeFlag(): Flow<Boolean> {
        return flow { emit(FakeData.firstTimeFlag) }
    }

    override suspend fun setFirstTimeFlag(flag: Boolean) {
        FakeData.firstTimeFlag = flag
    }

    override suspend fun setExchangeRates(exchangeRates: ExchangeRatesResponse): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrency(): Flow<String> {
        return flow {
            val currency = FakeData.currency ?: Constants.Currencies.EGP.value
            emit(currency)
        }
    }

    override suspend fun setCurrency(currency: Constants.Currencies) {
        FakeData.currency = currency.value
    }

    override suspend fun setCartID(id: Long?) {
        FakeData.CartId = id
    }

    override suspend fun setCustomerID(id: Long) {
        FakeData.CustomerId = id
    }

    override suspend fun getCustomerID(): Long? {
        return FakeData.CustomerId
    }

    override suspend fun getCartID(): Long? {
        return FakeData.CartId
    }

    override suspend fun setFullNameLocally(fullName: String) {
        FakeData.fullName = fullName
    }

    override suspend fun getFullName(): Flow<String?> {
        return flow { emit(FakeData.fullName) }
    }

    override suspend fun getCartItems(): Flow<List<CartItem>> {
        return flow {
            emit(FakeData.listOfCartItems)
        }
    }

    override suspend fun insertCartItem(cartItem: CartItem): Flow<Long> {
        return flow {
            if (FakeData.listOfCartItems.add(cartItem)) {
                emit(1L)
            } else {
                emit(0L)
            }
        }
    }

    override suspend fun deleteCartItem(cartItem: CartItem): Flow<Int> {
        return flow {
            if(FakeData.listOfCartItems.remove(cartItem)){
                emit(1)
            }else{
                emit(0)
            }
        }
    }

    override suspend fun deleteAllCartItems() {
        FakeData.listOfCartItems.clear()
    }

    override suspend fun updateCartItem(cartItem: CartItem) {
        FakeData.FakeCartItem = cartItem
    }

    override suspend fun setWishListID(id: Long?) {
        FakeData.wishListId = id
    }

    override suspend fun getWishListID(): Long? {
        return FakeData.wishListId
    }

    override fun getWishListItems(): Flow<List<WishListItem>> {
        return flow { emit(FakeData.listOfWishListItems) }
    }

    override suspend fun insertWishListItem(product: WishListItem): Flow<Long> {
        return flow {
            if (FakeData.listOfWishListItems.add(product)) {
                emit(1L)
            } else {
                emit(0L)
            }
        }
    }

    override suspend fun deleteWishListItem(product: WishListItem): Flow<Int> {
        return flow {
            if (FakeData.listOfWishListItems.remove(product)) {
                emit(1)
            } else {
                emit(0)
            }
        }
    }

    override suspend fun deleteAllWishListItems() {
        FakeData.listOfWishListItems.clear()
    }

    override suspend fun updateWishListItem(product: WishListItem) {
        FakeData.FakeWishListItem = product
    }

    override suspend fun getExchangeRateOf(currencyCode: Constants.Currencies): Flow<Pair<Double, Double>> {
        TODO("Not yet implemented")
    }

    override suspend fun clearData(): Flow<Unit> {
        return flow {
            emit(FakeData.listOfWishListItems.clear())
            emit(FakeData.listOfCartItems.clear())
        }
    }
}