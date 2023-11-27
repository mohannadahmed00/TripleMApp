package com.giraffe.triplemapplication.model.orders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentTotalPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
): Parcelable