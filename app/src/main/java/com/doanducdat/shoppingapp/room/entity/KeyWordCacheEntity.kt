package com.doanducdat.shoppingapp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "key_word")
data class KeyWordCacheEntity(
    @ColumnInfo
    val keyWord: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}