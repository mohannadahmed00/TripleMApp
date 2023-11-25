package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val currency: String,
    val line_items: List<LineItem>,
    val total_tax: Double,
    val transactions: List<Transaction>
): Parcelable