package com.giraffe.triplemapplication.model.cart.request

data class DraftOrder(
    val id:Long?=null,//to modify specific draft
    val line_items: List<LineItem>
)