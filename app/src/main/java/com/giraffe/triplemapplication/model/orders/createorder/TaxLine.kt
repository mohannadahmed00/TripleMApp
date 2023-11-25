package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaxLine(
    val price: Double,
    val rate: Double,
    val title: String
): Parcelable