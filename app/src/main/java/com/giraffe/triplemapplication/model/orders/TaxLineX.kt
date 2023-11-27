package com.giraffe.triplemapplication.model.orders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaxLineX(
    val channel_liable: Boolean,
    val price: String,
    val price_set: PriceSetXX,
    val rate: Double,
    val title: String
): Parcelable