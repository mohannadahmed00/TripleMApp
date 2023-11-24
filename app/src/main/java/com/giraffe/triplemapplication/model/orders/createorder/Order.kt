package com.giraffe.triplemapplication.model.orders.createorder

data class Order(
    val currency: String,
    val line_items: List<LineItem>,
    val total_tax: Double,
    val transactions: List<Transaction>
)