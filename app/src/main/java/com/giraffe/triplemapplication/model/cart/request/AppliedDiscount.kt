package com.giraffe.triplemapplication.model.cart.request

data class AppliedDiscount(
    val description: String,
    val value_type: String,
    val value: Double,
    val amount: Double,
    val title: String
)
