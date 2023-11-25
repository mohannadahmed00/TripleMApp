package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    val amount: Double,
    val kind: String,
    val status: String
): Parcelable