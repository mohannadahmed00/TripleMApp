package com.giraffe.triplemapplication.model.orders.createorder.createorderresponse

data class TaxLineX(
    val channel_liable: Boolean,
    val price: String,
    val price_set: PriceSetXX,
    val rate: Double,
    val title: String
)