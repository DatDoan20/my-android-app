package com.doanducdat.shoppingapp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//val reviews: List<Review>
//private val images: List<String>
@Entity(tableName = "product")
data class ProductCacheEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val imageCover: String,
    @ColumnInfo val slug: String,
    @ColumnInfo val price: Int,
    @ColumnInfo val brand: String,
    @ColumnInfo val size: String,
    @ColumnInfo val color: String,
    @ColumnInfo val material: String,
    @ColumnInfo val pattern: String,
    @ColumnInfo val discount: Int,
    @ColumnInfo val outOfStock: Boolean,
    @ColumnInfo val type: String,
    @ColumnInfo val category: String,
    @ColumnInfo val ratingsAverage: Float,
    @ColumnInfo val ratingsQuantity: Int,
    @ColumnInfo val createdAt: Date
)