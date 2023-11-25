package com.giraffe.triplemapplication.model.customers

data class AddressRequest(
    val address1: String,
    val city: String,
    val province: String,
    val phone: String,
    val zip: String,
    val last_name: String,
    val first_name: String,
    val country: String
)