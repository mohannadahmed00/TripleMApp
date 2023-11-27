package com.giraffe.triplemapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.giraffe.triplemapplication.model.cart.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_table")
    fun getCartItems(): List<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem): Long

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem): Int

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllCartItems()

    @Update
    suspend fun updateCartItem(cartItem: CartItem)
}