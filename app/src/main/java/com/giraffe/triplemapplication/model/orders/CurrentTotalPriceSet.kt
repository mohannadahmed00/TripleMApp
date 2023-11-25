package com.giraffe.triplemapplication.model.orders

data class CurrentTotalPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)