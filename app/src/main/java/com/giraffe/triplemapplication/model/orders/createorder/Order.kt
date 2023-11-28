package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import com.giraffe.triplemapplication.model.cart.BillingAddress
import com.giraffe.triplemapplication.model.cart.ShippingAddress
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val line_items: List<LineItem>,
    val email: String?,
    val phone: String?,
    val billingAddress: BillingAddress,
    val shippingAddress: ShippingAddress,
    val transactions: List<Transaction>,
    val financial_status: String,
    val discount_codes: DiscountCodes?
): Parcelable