package com.giraffe.triplemapplication.model.cart.request

import com.giraffe.triplemapplication.model.cart.ShippingAddress

data class DraftOrder(
    val id: Long? = null,//to modify specific draft
    val line_items: List<LineItem>,
//    val shipping_address: ShippingAddress?= null,
    val use_customer_default_address: Boolean = true,
    val appliedDiscount: AppliedDiscount? = null,
    val customer: Customer? = null
)