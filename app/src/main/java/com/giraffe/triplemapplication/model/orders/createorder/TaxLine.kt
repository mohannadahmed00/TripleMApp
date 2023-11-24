package com.giraffe.triplemapplication.model.orders.createorder

data class TaxLine(
    val price: Double,
    val rate: Double,
    val title: String
)