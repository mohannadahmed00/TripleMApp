package com.giraffe.triplemapplication.model.orders

data class TotalTaxSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)