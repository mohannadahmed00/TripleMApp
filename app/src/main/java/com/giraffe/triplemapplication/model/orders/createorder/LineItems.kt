package com.giraffe.triplemapplication.model.orders.createorder

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LineItems(
    var lineItems: List<LineItem>
): Parcelable
