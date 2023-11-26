package com.giraffe.triplemapplication.model.cart

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giraffe.triplemapplication.model.products.Product


@Entity(tableName = "cart_table")
data class CartItem(
    @PrimaryKey val variantId: Long,
    val product: Product,
    var quantity: Int,
    val isUploaded:Boolean
)
