package com.doanducdat.shoppingapp.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.doanducdat.shoppingapp.room.entity.KeyWordCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyWordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(keyWord: KeyWordCacheEntity)

    @Query("SELECT * FROM key_word ORDER BY id DESC")
    fun getAll(): Flow<List<KeyWordCacheEntity>>

    @Query("DELETE FROM key_word")
    suspend fun deleteAll()

}