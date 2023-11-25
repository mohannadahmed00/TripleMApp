package com.giraffe.triplemapplication.model.orders

data class CurrentTotalTaxSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)