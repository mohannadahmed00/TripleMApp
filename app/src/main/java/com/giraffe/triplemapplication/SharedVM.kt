package com.giraffe.triplemapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.discount.PriceRule
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeApiCall
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SharedVM(val repo: RepoInterface) : ViewModel() {
    companion object {
        private const val TAG = "SharedVM"
    }

    private val _languageFlow: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Loading)
    val languageFlow: StateFlow<Resource<String>> = _languageFlow.asStateFlow()
    private val _currentProduct: MutableStateFlow<Product?> = MutableStateFlow(null)
    val currentProduct: StateFlow<Product?> = _currentProduct.asStateFlow()
    private val _allProducts: MutableStateFlow<List<Product>?> = MutableStateFlow(null)
    val allProducts: StateFlow<List<Product>?> = _allProducts.asStateFlow()
    private val _filteredProducts: MutableStateFlow<List<Product>?> = MutableStateFlow(null)
    val filteredProducts: StateFlow<List<Product>?> = _filteredProducts.asStateFlow()

    private val _cartIdFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val cartIdFlow: StateFlow<Resource<Long>> = _cartIdFlow.asStateFlow()

    private val _currencyFlow: MutableStateFlow<Resource<String>> =
        MutableStateFlow(Resource.Loading)
    val currencyFlow: StateFlow<Resource<String>> = _currencyFlow.asStateFlow()

    private val _exchangeRateFlow: MutableStateFlow<Pair<Double, Double>?> = MutableStateFlow(null)
    val exchangeRateFlow: StateFlow<Pair<Double, Double>?> = _exchangeRateFlow.asStateFlow()

    private val _currencySymFlow: MutableStateFlow<Int> = MutableStateFlow(R.string.egp_sym)
    val currencySymFlow: StateFlow<Int> = _currencySymFlow.asStateFlow()


    private val _wishListItemsFlow: MutableStateFlow<List<WishListItem>> =
        MutableStateFlow(listOf())
    private val _isFav: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFav: StateFlow<Boolean> = _isFav

    private val _delWishListItemFlow: MutableStateFlow<Resource<Int>> = MutableStateFlow(
        Resource.Loading
    )
    val delWishListItemFlow: StateFlow<Resource<Int>> = _delWishListItemFlow.asStateFlow()
    private val _lastDeleted: MutableStateFlow<WishListItem?> = MutableStateFlow(null)
    private val _wishListFlow: MutableStateFlow<Resource<Long>> = MutableStateFlow(Resource.Loading)
    val wishListItem: StateFlow<Resource<Long>> = _wishListFlow.asStateFlow()

    private val _isLoggedFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoggedFlow: StateFlow<Boolean> = _isLoggedFlow.asStateFlow()

    private val _creationWishListFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    private val _updateWishListFlow: MutableStateFlow<Resource<DraftResponse>> =
        MutableStateFlow(Resource.Loading)
    private val _uploadWishListIdFlow: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.Loading)
    val uploadWishListIdFlow: StateFlow<Resource<Boolean>> = _uploadWishListIdFlow.asStateFlow()

    init {
        getLocallyWishListItems()
    }

    fun deleteWishListItemLocally(wishListItem: WishListItem) {
        viewModelScope.launch {
            _lastDeleted.emit(wishListItem)
            _delWishListItemFlow.emit(safeCall { repo.deleteWishListItem(wishListItem) })
            updateWishList()
        }
    }

    fun returnLastDeleted() {
        viewModelScope.launch(Dispatchers.IO) {
            _wishListFlow.emit(safeCall { repo.insertWishListItem(_lastDeleted.value!!) })
            updateWishList()
        }
    }

    fun setAllProduct(products: List<Product>) {

        viewModelScope.launch(Dispatchers.IO) {
            _allProducts.emit(products)
            _filteredProducts.emit(products)
        }
    }

    fun getLanguage() {
        viewModelScope.launch {
            _languageFlow.emit(safeCall { repo.getLanguage() })
        }
    }

    fun setCurrentProduct(product: Product) {
        viewModelScope.launch {
            _currentProduct.emit(product)
        }
    }

    fun insertFavorite(product: WishListItem) {

        viewModelScope.launch(Dispatchers.IO) {

            _wishListFlow.emit(safeCall { repo.insertWishListItem(product) })
            updateWishList()
        }
    }

    private fun updateWishList() {
        viewModelScope.launch(Dispatchers.IO) {
            getLocallyWishListItems()
            _wishListItemsFlow.collect {
                val lineItems = it.map { wishListItem ->
                    LineItem(1, wishListItem.variantId)
                }
                if (it.size == 1) {
                    createWishListDraft(lineItems)
                    observeCreateWishListDraft()
                } else {
                    updateWishListDraft(lineItems)
                    observeUpdateWishListDraft()
                }
            }
        }

    }

    private fun observeUpdateWishListDraft() {
        viewModelScope.launch(Dispatchers.IO) {
            _updateWishListFlow.collectLatest {
                when (it) {
                    is Resource.Failure -> {
                    }

                    Resource.Loading -> {}
                    is Resource.Success -> {
                        repo.setWishListIdLocally(it.value.draft_order.id)
                        uploadWishListId(it.value.draft_order.id)

                    }
                }
            }
        }
    }

    private fun updateWishListDraft(lineItems: List<LineItem>) {

        viewModelScope.launch {
            _updateWishListFlow.emit(safeApiCall {
                repo.modifyWishListDraft(
                    DraftRequest(
                        DraftOrder(
                            line_items = lineItems
                        )
                    )
                )
            })
        }


    }

    private fun observeCreateWishListDraft() {
        viewModelScope.launch(Dispatchers.IO) {
            _creationWishListFlow.collectLatest {
                when (it) {
                    is Resource.Failure -> {

                    }

                    Resource.Loading -> {}
                    is Resource.Success -> {
                        repo.setWishListIdLocally(it.value.draft_order.id)
                        uploadWishListId(it.value.draft_order.id)
                    }
                }
            }
        }

    }

    private fun uploadWishListId(wishListId: Long) {
        Log.i("Cs", "uploadWishListId:$wishListId ")

        viewModelScope.launch {
            Log.i("Cs", "uploadWishListId:$wishListId ")
            repo.uploadWishListId(wishListId).let {

                _uploadWishListIdFlow.emit(Resource.Success(true))


            }
        }
    }

    private fun createWishListDraft(lineItems: List<LineItem>) {
        viewModelScope.launch {
            _creationWishListFlow.emit(safeApiCall {
                repo.createWishListDraft(
                    DraftRequest(
                        DraftOrder(line_items = lineItems)
                    )
                )
            })
        }
    }

    fun getCartId() {
        viewModelScope.launch(Dispatchers.IO) {
            _cartIdFlow.emit(safeCall { repo.getCartId() })
        }
    }

    fun getSelectedCurrency() {
        viewModelScope.launch {
            _currencyFlow.emit(safeCall { repo.getCurrency() })
        }
    }

    fun getExchangeRateOf(currencyCode: Constants.Currencies) {
        viewModelScope.launch {
            val pair = repo.getExchangeRateOf(currencyCode)
            _exchangeRateFlow.emit(pair.first())
        }
    }

    fun setCurrencySymbol(currencySymRes: Int) {
        viewModelScope.launch {
            _currencySymFlow.emit(currencySymRes)
        }
    }


    private fun getLocallyWishListItems() {
        viewModelScope.launch {
            repo.getWishListItems().collect {
                _wishListItemsFlow.emit(it)
            }
        }
    }

    fun isFav() {
        viewModelScope.launch(Dispatchers.IO) {

            val productToCheck = _currentProduct.value

            _wishListItemsFlow.collect { wishListItems ->
                val productExistsInWishlist =
                    wishListItems.map { it.product }.any { it == productToCheck }

                if (productExistsInWishlist) {
                    _isFav.emit(true)
                } else {
                    _isFav.emit(false)
                }
            }
        }
    }

    fun isFav(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {


            _wishListItemsFlow.collect { wishListItems ->
                val productExistsInWishlist =
                    wishListItems.map { it.product }.any { it == product }

                if (productExistsInWishlist) {
                    _isFav.emit(true)
                } else {
                    _isFav.emit(false)
                }
            }
        }
    }


    fun setFiltered(filteredProducts: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            _filteredProducts.emit(filteredProducts)
        }
    }

    fun setIsLoggedFlag(isLoggedFlag: Boolean) {
        _isLoggedFlow.value = isLoggedFlag
    }

    private val _couponsFlow: MutableStateFlow<List<PriceRule>> = MutableStateFlow(listOf())
    val couponsFlow: StateFlow<List<PriceRule>> = _couponsFlow.asStateFlow()

    fun setCoupons(coupons: List<PriceRule>){
        _couponsFlow.value = coupons
    }

}