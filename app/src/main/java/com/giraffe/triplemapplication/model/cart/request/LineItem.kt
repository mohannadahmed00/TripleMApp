package com.giraffe.triplemapplication.model.cart.request

data class LineItem(
    val quantity: Int,
    val variant_id: Long,
    val title: String,
    val price: Double
)