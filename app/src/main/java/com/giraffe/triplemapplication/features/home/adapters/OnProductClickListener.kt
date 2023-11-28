package com.giraffe.triplemapplication.features.home.adapters

import android.widget.ImageView
import com.giraffe.triplemapplication.model.wishlist.WishListItem

interface OnProductClickListener {
    fun onProductClickListener( isFav : ImageView, wishListItem: WishListItem)
}