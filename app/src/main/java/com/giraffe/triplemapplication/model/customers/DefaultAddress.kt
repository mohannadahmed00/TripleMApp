package com.giraffe.triplemapplication.model.customers

data class DefaultAddress(
    val id: Long,

    val customer_id: Long,

    val first_name: String,

    val last_name: String,
    val company: Any?,
    val address1: String,
    val address2: Any?,
    val city: String,
    val province: String,
    val country: String,
    val zip: String,
    val phone: String,
    val name: String,

    val province_code: String,

    val country_code: String,

    val country_name: String,
    val default: Boolean,
)
