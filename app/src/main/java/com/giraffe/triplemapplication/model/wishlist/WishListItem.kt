package com.giraffe.triplemapplication.model.wishlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giraffe.triplemapplication.model.products.Product

@Entity(tableName = "wish_list_table")
data class WishListItem(
    @PrimaryKey val variantId: Long,
    val product: Product,
    val isUploaded:Boolean
)
