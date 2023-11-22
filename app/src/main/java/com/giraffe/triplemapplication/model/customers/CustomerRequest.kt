package com.giraffe.triplemapplication.model.customers

data class CustomerRequest(
    val first_name: String,
    val last_name: String? = null,
    val email: String,
    val phone: String,
    val verified_email: Boolean,
    val addresses: List<AddressRequest>? = null ,
    val password: String,
    val password_confirmation: String,
    val send_email_welcome: Boolean
)