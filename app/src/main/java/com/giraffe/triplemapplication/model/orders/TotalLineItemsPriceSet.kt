package com.giraffe.triplemapplication.model.orders

data class TotalLineItemsPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)