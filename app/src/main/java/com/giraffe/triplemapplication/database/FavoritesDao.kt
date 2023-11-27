package com.giraffe.triplemapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM wish_list_table")
    fun getAllFavorites(): Flow<List<WishListItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(product: WishListItem): Long

    @Delete
    suspend fun deleteFavorite(product: WishListItem): Int

    @Query("DELETE FROM wish_list_table")
    suspend fun deleteAllFavorites()

    @Update
    suspend fun updateFavorite(product: WishListItem)
}