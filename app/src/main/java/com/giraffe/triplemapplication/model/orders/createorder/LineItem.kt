package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineItem(
    var variantId: Long,
    var quantity: Int,
    val price: Double,
    val name: String,
    val title: String
): Parcelable