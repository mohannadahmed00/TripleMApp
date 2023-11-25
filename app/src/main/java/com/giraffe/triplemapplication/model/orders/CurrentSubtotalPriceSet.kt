package com.giraffe.triplemapplication.model.orders

data class CurrentSubtotalPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)