package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiscountCodes(
    val code: String,
    val amount: String,
    val type: String,

): Parcelable
