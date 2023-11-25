package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderCreate(
    val order: Order
): Parcelable