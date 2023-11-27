package com.giraffe.triplemapplication.model.cart.request

data class DraftOrder(
    val id: Long? = null,//to modify specific draft
    val line_items: List<LineItem>,
    val use_customer_default_address: Boolean,
    val appliedDiscount: AppliedDiscount,
    val customer: Customer
)