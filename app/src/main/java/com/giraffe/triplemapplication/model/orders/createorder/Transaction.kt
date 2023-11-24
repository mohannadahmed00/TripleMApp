package com.giraffe.triplemapplication.model.orders.createorder

data class Transaction(
    val amount: Double,
    val kind: String,
    val status: String
)