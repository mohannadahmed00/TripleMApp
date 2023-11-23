package com.giraffe.triplemapplication.model.cart

data class BillingAddress(
    val address1: String,
    val city: String,
    val country: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val province: String,
    val zip: String
)