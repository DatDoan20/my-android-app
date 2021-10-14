package com.doanducdat.shoppingapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.doanducdat.shoppingapp.room.entity.ImageCacheEntity
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(imagesOfProduct: MutableList<ImageCacheEntity>)

    @Query("SELECT * FROM image")
    suspend fun getAll(): List<ImageCacheEntity>

    @Query("DELETE FROM image")
    suspend fun deleteAll()

}