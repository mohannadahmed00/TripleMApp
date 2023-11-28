package com.giraffe.triplemapplication.features.search.viewmodel

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.repo.RepoInterface
import com.giraffe.triplemapplication.utils.Resource
import com.giraffe.triplemapplication.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchVM(private val repo: RepoInterface) : ViewModel() {


    private val TAG: String = "SEARCH_VM"
    private val _allProductsFlow: MutableStateFlow<Resource<AllProductsResponse>> =
        MutableStateFlow(Resource.Loading)
    val allProductsFlow: StateFlow<Resource<AllProductsResponse>> = _allProductsFlow.asStateFlow()

    private val _allProducts: MutableStateFlow<List<Product>?> = MutableStateFlow(null)
    val allProducts: StateFlow<List<Product>?> = _allProducts.asStateFlow()

    init {

    }



    private fun filterColor(selectedColor: String = "", products: List<Product>): List<Product> {
        val filteredProducts = products.filter { product ->
            val colorMatches =
                product.variants?.first()?.option2.equals(selectedColor, ignoreCase = true)
            colorMatches
        }
        return filteredProducts
    }

    private fun filterCategory(category: String = "", products: List<Product>): List<Product> {
        val filteredProducts = products.filter { product ->
            val categoryMatches = product.tags?.contains(category) == true
            categoryMatches
        }

        return filteredProducts
    }

    private fun filterBrands(brands: List<String>, products: List<Product>): List<Product> {
        val filteredProducts = products.filter { product ->
            val brandsMatches = brands.any { it.equals(product.vendor, ignoreCase = true) }
            brandsMatches
        }

        return filteredProducts

    }

    private fun filterPrice(minPrice: Double?, maxPrice: Double?, products: List<Product>): List<Product> {
        val filteredProducts = products.filter { product ->
            val priceMatches =
                (minPrice == null || product.variants!!.first()!!.price!!.toDouble() >= minPrice) &&
                        (maxPrice == null || product.variants!!.first()!!.price!!.toDouble() <= maxPrice)

            priceMatches

        }

        return filteredProducts

    }
    fun filterSearch(
        brands: List<String> = listOf(),
        selectedColor: String = "",
        category: String = "",
        minPrice: Double? = null,
        maxPrice: Double? = null,
        products: List<Product>
    ): List<Product> {
        var filteredProducts = products.toList() // Create a copy of the original list

        if (!brands.isNullOrEmpty()) {
            filteredProducts = filterBrands(brands, filteredProducts)
            Log.i(TAG, "filterSearch: brands ${filteredProducts.size}")

        }

        if (selectedColor!="") {
            filteredProducts = filterColor(selectedColor, filteredProducts)
            Log.i(TAG, "filterSearch: color ${filteredProducts.size}")

        }

        if (category!="") {

            filteredProducts = filterCategory(category, filteredProducts)
            Log.i(TAG, "filterSearch: category ${filteredProducts.size}")

        }

        if (minPrice != null && maxPrice != null) {
            filteredProducts = filterPrice(minPrice, maxPrice, filteredProducts)
            Log.i(TAG, "filterSearch: price ${filteredProducts.size}")

        }

        Log.i(TAG, "filterSearch: $selectedColor , $brands , $category , $minPrice , $maxPrice")
        return filteredProducts
    }





}