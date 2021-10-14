package com.doanducdat.shoppingapp.room.entity

import androidx.room.Entity

@Entity(tableName = "image", primaryKeys = ["id", "name"])
data class ImageCacheEntity(
    val id: String, val name: String
)