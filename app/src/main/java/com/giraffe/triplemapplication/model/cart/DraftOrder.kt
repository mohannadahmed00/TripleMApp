package com.giraffe.triplemapplication.model.cart

data class DraftOrder(
    val applied_discount: AppliedDiscount,
    val billing_address: BillingAddress,
    val customer: Customer,
    val line_items: List<LineItem>,
    val shipping_address: ShippingAddress,
    val use_customer_default_address: Boolean
)