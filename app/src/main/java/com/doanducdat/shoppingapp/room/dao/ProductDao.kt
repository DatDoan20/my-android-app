package com.doanducdat.shoppingapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.doanducdat.shoppingapp.room.entity.ProductCacheEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<ProductCacheEntity>)

    @Query("SELECT * FROM product ORDER BY createdAt DESC LIMIT 10")
    suspend fun getNewProduct(): List<ProductCacheEntity>

    @Query("SELECT * FROM product WHERE discount != 0 ORDER BY createdAt DESC LIMIT 10")
    suspend fun getSaleProduct(): List<ProductCacheEntity>

    @Query("DELETE FROM product")
    suspend fun deleteAll()

}