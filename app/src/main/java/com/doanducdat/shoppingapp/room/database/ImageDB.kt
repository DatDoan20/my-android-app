package com.doanducdat.shoppingapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doanducdat.shoppingapp.room.dao.ImageDao
import com.doanducdat.shoppingapp.room.dao.ProductDao
import com.doanducdat.shoppingapp.room.entity.ImageCacheEntity

@Database(entities = [ImageCacheEntity::class], version = 1, exportSchema = false)
abstract class ImageDB : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    companion object{
        const val IMAGE_DB = "image_db"
    }
}