package com.giraffe.triplemapplication.model.orders

data class TotalPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)