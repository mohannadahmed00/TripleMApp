package com.giraffe.triplemapplication.model.orders.createorder.createorderresponse

data class TotalPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)