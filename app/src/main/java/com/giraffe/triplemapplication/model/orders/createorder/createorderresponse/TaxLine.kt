package com.giraffe.triplemapplication.model.orders.createorder.createorderresponse

data class TaxLine(
    val channel_liable: Boolean,
    val price: String,
    val price_set: PriceSetX,
    val rate: Double,
    val title: String
)