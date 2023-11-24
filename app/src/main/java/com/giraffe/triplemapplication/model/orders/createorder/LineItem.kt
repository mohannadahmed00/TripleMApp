package com.giraffe.triplemapplication.model.orders.createorder

data class LineItem(
    val grams: String,
    val price: Double,
    val quantity: Int,
    val tax_lines: List<TaxLine>,
    val title: String
)