package com.doanducdat.shoppingapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doanducdat.shoppingapp.room.dao.ProductDao
import com.doanducdat.shoppingapp.room.entity.ProductCacheEntity

@Database(entities = [ProductCacheEntity::class], version = 1, exportSchema = false)
abstract class ProductDB : RoomDatabase() {
    abstract fun productDao(): ProductDao
    companion object{
        const val PRODUCT_DB = "product_db"
    }
}