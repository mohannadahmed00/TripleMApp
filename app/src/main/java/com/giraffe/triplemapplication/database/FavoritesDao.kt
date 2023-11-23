package com.giraffe.triplemapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.giraffe.triplemapplication.model.products.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM products_table")
    fun getAllFavorites(): Flow<Product>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(product: Product): Long

    @Delete
    suspend fun deleteFavorite(product: Product): Int

    @Query("DELETE FROM products_table")
    suspend fun deleteAllFavorites()

    @Update
    suspend fun updateFavorite(product: Product)
}