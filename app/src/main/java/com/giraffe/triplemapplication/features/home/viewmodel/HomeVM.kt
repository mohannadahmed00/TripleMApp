package com.giraffe.triplemapplication.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giraffe.triplemapplication.features.home.view.Category
import com.giraffe.triplemapplication.features.home.view.HomeUiState
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.repo.RepoInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeVM(private val repo:RepoInterface):ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            repo.getProducts().collect { products ->
                _uiState.value = _uiState.value.copy(products = products)
                setCategories()
            }
        }
    }

    private fun setCategories() {
        val map: Map<String?, List<Product>> = _uiState.value.products.groupBy { it.vendor }
        val keysByProductCount = map
            .filterKeys { it != null }
            .entries
            .groupBy { it.value.size }
            .toSortedMap(compareByDescending { it })
            .values

        val keyWithMoreProducts = mutableListOf<String?>()
        val maxProductCount = keysByProductCount.firstOrNull()?.size ?: 0

        keysByProductCount.forEach { entries ->
            if (entries.firstOrNull()?.value?.size == maxProductCount) {
                keyWithMoreProducts.addAll(entries.map { it.key })
            } else {
                keyWithMoreProducts.addAll(entries.sortedBy { it.key }.map { it.key })
            }
        }

        _uiState.value = _uiState.value.copy(categories = keyWithMoreProducts.toList())
    }
}