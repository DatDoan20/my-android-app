package com.doanducdat.shoppingapp.room.converter

import androidx.room.TypeConverter
import java.util.*

class ConverterDate {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}