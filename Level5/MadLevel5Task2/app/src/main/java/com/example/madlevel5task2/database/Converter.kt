package com.example.madlevel5task2.database
import androidx.room.TypeConverter
import java.time.LocalDate

class Converter{

    @TypeConverter
    fun toTimeTamp(value:Long):LocalDate{
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun toLong(releaseDate:LocalDate):Long{
        return releaseDate.toEpochDay()
    }
}