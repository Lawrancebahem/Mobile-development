package com.example.madlevel5task1.databaase

import androidx.room.TypeConverter
import java.util.*

class Converter{


    @TypeConverter
    fun fromTimestamp(value: Long?):Date?{
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(value:Date?):Long?{
        return value?.time?.toLong()
    }
}