package com.giraffe.triplemapplication.model.orders.createorder.createorderresponse

data class TotalShippingPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)