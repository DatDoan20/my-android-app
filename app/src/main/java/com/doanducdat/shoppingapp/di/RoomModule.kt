package com.doanducdat.shoppingapp.di

import android.content.Context
import androidx.room.Room
import com.doanducdat.shoppingapp.room.dao.ImageDao
import com.doanducdat.shoppingapp.room.dao.KeyWordDao
import com.doanducdat.shoppingapp.room.dao.ProductDao
import com.doanducdat.shoppingapp.room.database.ImageDB
import com.doanducdat.shoppingapp.room.database.KeyWordDB
import com.doanducdat.shoppingapp.room.database.ProductDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideProductDB(@ApplicationContext appContext: Context): ProductDB {
        return Room.databaseBuilder(appContext, ProductDB::class.java, ProductDB.PRODUCT_DB).build()
    }

    @Provides
    fun provideProductDao(productDB: ProductDB): ProductDao {
        return productDB.productDao()
    }

    @Provides
    @Singleton
    fun provideImageDB(@ApplicationContext appContext: Context): ImageDB {
        return Room.databaseBuilder(appContext, ImageDB::class.java, ImageDB.IMAGE_DB).build()
    }

    @Provides
    fun provideImageDao(imageDB: ImageDB): ImageDao {
        return imageDB.imageDao()
    }

    @Provides
    @Singleton
    fun provideKeyWordDB(@ApplicationContext appContext: Context): KeyWordDB {
        return Room.databaseBuilder(appContext, KeyWordDB::class.java, KeyWordDB.KEYWORD_DB).build()
    }

    @Provides
    fun provideKeyWordDao(keyWordDB: KeyWordDB): KeyWordDao {
        return keyWordDB.keyWordDao()
    }
}