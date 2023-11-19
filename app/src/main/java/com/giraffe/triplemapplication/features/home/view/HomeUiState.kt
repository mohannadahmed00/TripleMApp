package com.giraffe.triplemapplication.features.home.view

import com.giraffe.triplemapplication.model.products.Product

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val categories: List<String?> = emptyList()
)
