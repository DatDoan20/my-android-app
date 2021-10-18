package com.doanducdat.shoppingapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doanducdat.shoppingapp.room.dao.KeyWordDao
import com.doanducdat.shoppingapp.room.entity.KeyWordCacheEntity

@Database(entities = [KeyWordCacheEntity::class], version = 1, exportSchema = false)
abstract class KeyWordDB : RoomDatabase() {
    abstract fun keyWordDao(): KeyWordDao

    companion object {
        const val KEYWORD_DB = "key_word_db"
    }
}