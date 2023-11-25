package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineItem(
    val grams: String,
    val price: Double,
    val quantity: Int,
    val tax_lines: List<TaxLine>,
    val title: String
): Parcelable